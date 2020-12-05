public interface OrderFactory {
	boolean loadOrders(String orderFile);
	Order fetchOrder();
}
