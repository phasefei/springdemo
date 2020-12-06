import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShelfItem implements Comparable<ShelfItem> {
	private Order order;
	private long cookedTime;
	private long courierAvailableTime;
	private long timeToDeliver;
	private double timeToWaste;
	private Shelf targetShelf;
	private int tmpIndex;

	public ShelfItem(Order order, Integer courierDelay) {
		this.order = order;
		long currentTime = currentTimeSeconds();
		cookedTime = currentTime;
		courierAvailableTime = currentTime + courierDelay;
	}

	public void update(int decayModifier) {
		long currentTime = currentTimeSeconds();
		timeToDeliver = courierAvailableTime - currentTime;
		long orderAge = currentTime - cookedTime;
		timeToWaste = (order.getShelfLife() - orderAge - order.getDecayRate() * orderAge * decayModifier)
				/ order.getShelfLife();
	}

	private long currentTimeSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	@Override
	public int compareTo(ShelfItem item) {
		return (int) ((timeToWaste - item.getTimeToWaste()) * 10000);
	}
}
