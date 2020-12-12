/**
 * The interface Orders stat.
 * trace the processing of orders
 */
public interface OrdersStat {
	/**
	 * trace when an order was created
	 * @param order order
	 */
	void addOrder(Order order);

	/**
	 * trace when an order was delivered / rotted / discarded
	 * @param order order
	 */
	void delOrder(Order order);

	/**
	 * log abnormal orders, which processed more than once, or not processed at all
	 */
	void logAbnormalOrders();
}
