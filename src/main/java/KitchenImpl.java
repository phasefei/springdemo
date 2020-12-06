import com.google.common.util.concurrent.RateLimiter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

@Setter
public class KitchenImpl implements Kitchen {
	private static final Logger logger = LoggerFactory.getLogger(KitchenImpl.class);
	private static final Random random = new Random();

	private RateLimiter rateLimiter;
	private Double receiveRate = 2.0;
	private Integer courierDelayMin = 2;
	private Integer courierDelayMax = 6;
	private Map<String, Shelf> shelves;
	private Shelf overflowShelf;

	public void init() {
		rateLimiter = RateLimiter.create(receiveRate);
	}

	@Override
	public void receiveOrder(Order order) {
		rateLimiter.acquire();

		Integer courierDelay = courierDelayMin + random.nextInt(courierDelayMax - courierDelayMin + 1);
		ShelfItem item = new ShelfItem(order, courierDelay);
		logger.info("receive {}", order);

		Shelf shelf = shelves.get(order.getTemp());
		if (shelf == null) {
			return;
		}
		if (!shelf.place(item)) {
			overflowShelf.place(item);
		}
	}

	@Override
	public void stop() {
		shelves.forEach((k, v) -> v.shouldStop());
		overflowShelf.shouldStop();

		shelves.forEach((k, v) -> v.waitQuit());
		overflowShelf.waitQuit();
	}
}
