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
	/** overflow shelf must accept an order */
	private boolean enforcePlace = false;
	/** loop break control */
	private long sleepInterval = -1;

	/** available shelf slots */
	protected Queue<Integer> availableQ = Queues.newConcurrentLinkedQueue();
	/** index pointed to rottenest order on the shelf */
	private volatile Integer runtIndex = 0;
	private volatile ShelfItem[] items;
	protected volatile ShelfSlotStatus[] slots;
	private volatile boolean stopFlag = false;
	private volatile boolean waitSlot = false;

	private Map<String, Integer> shelfStat = Maps.newHashMap();

	private OrdersStat ordersStat;

	public void init() {
		items = new ShelfItem[capacity];
		slots = new ShelfSlotStatus[capacity];
		for (int i = 0; i < capacity; i++) {
			slots[i] = ShelfSlotStatus.INITIAL;
		}

		setName(shelfName);
		start();
	}

	/**
	 * no operation for ordinary shelf
	 * overflow shelf: collect orders in priority queues for later movement
	 * @param item item
	 * @param index slot index
	 */
	public void addToMoves(ShelfItem item, int index) {
	}

	/**
	 * no operation for ordinary shelf
	 * overflow shelf: moves orders to hot/cold/frozen shelf whenever possible, find the rottenest order
	 * @return the index of rottenest order or null if not found
	 */
	public Integer moveItems() {
		return null;
	}

	@Override
	public void run() {
		for (; !stopFlag || availableQ.size() != capacity;) {
			// loop over the shelf items for delivering, rotting, discarding ...
			for (int i = 0; i < capacity; i++) {
				switch (slots[i]) {
					case INITIAL:
						slots[i] = ShelfSlotStatus.AVAILABLE;
						availableQ.offer(i);
						break;
					case AVAILABLE:
						break;
					case USING:
						items[i].update(decayModifier);
						if (items[i].getTimeToWaste() <= 0) {
							statAndLog("waste", items[i], true);
							slots[i] = ShelfSlotStatus.AVAILABLE;
							availableQ.offer(i);
						} else {
							if (items[i].getTimeToDeliver() <= 0) {
								statAndLog("deliver", items[i], true);
								slots[i] = ShelfSlotStatus.AVAILABLE;
								availableQ.offer(i);
							} else {
								addToMoves(items[i], i);
							}
						}
						break;
				}
			}

			Integer ttwMinIdx = moveItems();

			// if it's an enforcement, hand out the available or rottenest order index
			if (waitSlot) {
				runtIndex = ttwMinIdx != null ? ttwMinIdx : availableQ.poll();

				logger.info("---------- discard print begin ----------");
				for (int i = 0; i < capacity; i++) {
					logger.info("{}", items[i]);
				}
				logger.info("---------- discard print end ----------");

				statAndLog("discard", items[runtIndex], true);
				slots[runtIndex] = ShelfSlotStatus.AVAILABLE;

				waitSlot = false;
			}

			try {
				if (sleepInterval >= 0) {
					Thread.sleep(sleepInterval);
				}
			} catch (InterruptedException e) {
				logger.warn(e.toString());
			}
		}
	}

	@Override
	public boolean place(ShelfItem item) {
		if (item.getTargetShelf() == null) {
			item.setTargetShelf(this);
		}

		Integer index = availableQ.poll();
		// if currently there's no available slot
		if (index == null) {
			if (enforcePlace) {
				// overflow shelf discards the rottenest order and store the new one
				waitSlot = true;
				while (waitSlot) { // wait for the signal
					try {
						if (sleepInterval >= 0) {
							Thread.sleep(sleepInterval);
						}
					} catch (InterruptedException e) {
						logger.warn(e.toString());
					}
				}
				index = runtIndex;
			} else {
				// ordinary shelf can reject placement
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
		} catch (InterruptedException e) {
			logger.warn(e.toString());
		}
	}

	protected void statAndLog(String action, ShelfItem item, boolean del) {
		shelfStat.merge(action, 1, (v1, v2) -> v1 + v2);
		if (del) {
			ordersStat.delOrder(item.getOrder());
		}
		logger.info("{} {}", action, item);
	}
}
