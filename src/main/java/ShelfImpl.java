import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;

@Setter
public class ShelfImpl extends Thread implements Shelf {
	private static final Logger logger = LoggerFactory.getLogger(ShelfImpl.class);

	private String shelfName;
	private int capacity;
	private int decayModifier;
	private boolean enforcePlace = false;

	private Queue<Integer> availableQ = Queues.newConcurrentLinkedQueue();
	private volatile Integer runtIndex = 0;
	private volatile ShelfItem[] items;
	private volatile ShelfSlotStatus[] slots;
	private volatile boolean stopFlag = false;
	private volatile boolean waitSlot = false;

	private Map<String, Integer> shelfStat = Maps.newHashMap();

	private OrdersStat ordersStat;

	public void init() {
		items = new ShelfItem[capacity];
		slots = new ShelfSlotStatus[capacity];
		for (int i = 0; i < capacity; i++) {
			slots[i] = ShelfSlotStatus.AVAILABLE;
		}

		setName(shelfName);
		start();
	}

	@Override
	public void run() {
		for (; !stopFlag || availableQ.size() != capacity;) {
			double ttwMin = Double.MAX_VALUE;
			Integer ttwMinIdx = 0;
			for (int i = 0; i < items.length; i++) {
				switch (slots[i]) {
					case AVAILABLE:
						slots[i] = ShelfSlotStatus.ASSIGNED;
						availableQ.offer(i);
						break;
					case ASSIGNED:
						break;
					case USING:
						items[i].update(decayModifier);
						double ttw = items[i].getTimeToWaste();
						if (ttw <= 0) {
							slots[i] = ShelfSlotStatus.ASSIGNED;
							availableQ.offer(i);

							shelfStat.merge("waste", 1, (v1, v2) -> v1 + v2);
							ordersStat.delOrder(items[i].getOrder());
							logger.info("waste {}", items[i]);
						} else {
							long ttd = items[i].getTimeToDeliver();
							if (ttd <= 0) {
								slots[i] = ShelfSlotStatus.ASSIGNED;
								availableQ.offer(i);

								shelfStat.merge("deliver", 1, (v1, v2) -> v1 + v2);
								ordersStat.delOrder(items[i].getOrder());
								logger.info("deliver {}", items[i]);
							} else {
								if (ttw < ttwMin) {
									ttwMin = ttw;
									ttwMinIdx = i;
								}
							}
						}
						break;
				}
			}

			if (waitSlot) {
				runtIndex = ttwMinIdx;

				slots[runtIndex] = ShelfSlotStatus.ASSIGNED;
				items[runtIndex].update(decayModifier);

				shelfStat.merge("discard", 1, (v1, v2) -> v1 + v2);
				ordersStat.delOrder(items[runtIndex].getOrder());
				logger.info("discard {}", items[runtIndex]);

				waitSlot = false;
			}
		}
	}

	@Override
	public boolean place(ShelfItem item) {
		Integer index = availableQ.poll();
		if (index == null) {
			if (enforcePlace) {
				waitSlot = true;
				while (waitSlot);
				index = runtIndex;
			} else {
				return false;
			}
		}

		items[index] = item;
		slots[index] = ShelfSlotStatus.USING;

		return true;
	}

	@Override
	public void shouldStop() {
		stopFlag = true;
	}

	@Override
	public void waitQuit() {
		try {
			join();
			logger.info("wait shelf [{}] exit, total={}, {}",
					shelfName, shelfStat.values().stream().mapToInt(v -> v).sum(), shelfStat.toString());
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}
}
