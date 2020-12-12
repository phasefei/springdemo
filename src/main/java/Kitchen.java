/**
 * The interface Kitchen.
 */
public interface Kitchen {
	/**
	 * A kitchen can receive an order, cook it and deliver it
	 * @param order the order which kitchen receives, cooks, delivers
	 */
	void receiveOrder(Order order);

	/**
	 * stop the kitchen from running
	 */
	void stop();
}
