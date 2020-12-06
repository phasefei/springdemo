import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.Map;
import java.util.Queue;

public class OverflowShelfImpl extends ShelfImpl {
	private Map<String, Queue<ShelfItem>> moves = Maps.newHashMap();

	@Override
	public void addToMoves(ShelfItem item, int index) {
		item.setTmpIndex(index);
		String shelfName = item.getOrder().getTemp();
		Queue<ShelfItem> queue = moves.get(shelfName);
		if (queue == null) {
			queue = Queues.newPriorityQueue();
			moves.put(shelfName, queue);
		}
		queue.offer(item);
	}

	@Override
	public Integer moveItems() {
		Queue<ShelfItem> tmpQ = Queues.newPriorityQueue();
		moves.values().forEach(q -> {
			for (ShelfItem item = q.poll(); item != null; item = q.poll()) {
				if (item.getTargetShelf().place(item)) {
					statAndLog("move", item, false);
					slots[item.getTmpIndex()] = ShelfSlotStatus.ASSIGNED;
					availableQ.offer(item.getTmpIndex());
				} else {
					tmpQ.offer(item);
				}
			}
		});
		ShelfItem item = tmpQ.poll();
		return item == null ? null : item.getTmpIndex();
	}
}
