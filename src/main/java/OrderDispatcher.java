import lombok.Setter;

/**
 * The workhorse of order process
 */
@Setter
public class OrderDispatcher extends Thread {
	private String orderFile = "orders.json";
	private Kitchen kitchen;
	private OrderFactory orderFactory;
	private OrdersStat ordersStat;

	public OrderDispatcher() {
		super();
		setName(OrderDispatcher.class.getSimpleName());
	}

	/**
	 * process all orders then stop the loop
	 */
	@Override
	public void run() {
		if (orderFactory.loadOrders(orderFile)) {
			for (;;) {
				Order order = orderFactory.fetchOrder();
				if (order == null) {
					break;
				}
				ordersStat.addOrder(order);
				kitchen.receiveOrder(order);
			}
		}
		kitchen.stop();
		ordersStat.logAbnormalOrders();
	}
}
