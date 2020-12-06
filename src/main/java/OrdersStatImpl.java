import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrdersStatImpl implements OrdersStat {
	private static Logger logger = LoggerFactory.getLogger(OrdersStatImpl.class);

	private ConcurrentMap<String, AtomicInteger> orders = Maps.newConcurrentMap();

	@Override
	public void addOrder(Order order) {
		AtomicInteger newV = new AtomicInteger(1);
		AtomicInteger oldV = orders.putIfAbsent(order.getId(), newV);
		if (oldV != null) {
			oldV.incrementAndGet();
		}
	}

	@Override
	public void delOrder(Order order) {
		AtomicInteger newV = new AtomicInteger(-1);
		AtomicInteger oldV = orders.putIfAbsent(order.getId(), newV);
		if (oldV != null) {
			oldV.decrementAndGet();
		}
	}

	@Override
	public void logAbnormalOrders() {
		logger.info("abnormal orders: {}",
				orders.entrySet().stream().filter(e -> e.getValue().get() != 0)
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString());
	}
}
