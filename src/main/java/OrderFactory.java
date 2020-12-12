/**
 * The interface Order factory.
 */
public interface OrderFactory {
	/**
	 * load orders from a local file
	 * @param orderFile the file which stores all orders
	 * @return true - loaded successfully, false - load failed
	 */
	boolean loadOrders(String orderFile);

	/**
	 * fetch an order from factory
	 * @return an order or null when all orders were processed
	 */
	Order fetchOrder();
}
