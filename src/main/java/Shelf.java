/**
 * The interface Shelf.
 */
public interface Shelf {
	/**
	 * place an order on the shelf waiting for delivery
	 * if it's an ordinary shelf, only accepts order when it has empty slot
	 * if it's an overflow shelf, always accepts order
	 * @param item the order to be placed on the shelf
	 * @return true - placed successfully, false - shelf is full, can't be placed
	 */
	boolean place(ShelfItem item);

	/**
	 * signal the shelf to stop
	 */
	void shouldStop();

	/**
	 * await the shelf processes orders
	 */
	void waitQuit();
}
