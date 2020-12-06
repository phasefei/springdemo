public interface OrdersStat {
	void addOrder(Order order);
	void delOrder(Order order);
	void logAbnormalOrders();
}
