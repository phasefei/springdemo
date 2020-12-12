import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The item placed on shelf
 * encapsulate order structure plus more information
 */
@Getter
@Setter
@ToString
public class ShelfItem implements Comparable<ShelfItem> {
	private Order order;
	private long cookedTime;
	private long courierAvailableTime;
	/** how many seconds left when courier will be available, <= 0 mean deliverable */
	private long timeToDeliver;
	/** an age alike value, <= 0.0 mean rotted */
	private double timeToWaste;
	/** then original shelf to be placed, matches the temperature */
	private Shelf targetShelf;
	/** slot index in overflow shelf */
	private int tmpIndex;

	public ShelfItem(Order order, Integer courierDelay) {
		this.order = order;
		long currentTime = currentTimeSeconds();
		cookedTime = currentTime;
		courierAvailableTime = currentTime + courierDelay;
	}

	/**
	 * updates order info, timeToDeliver and timeToWaste
	 * @param decayModifier shelf decayModifier
	 */
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
		return (int) ((timeToWaste - item.getTimeToWaste()) * 1000000000);
	}
}
