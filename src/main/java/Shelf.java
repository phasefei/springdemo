public interface Shelf {
	boolean place(ShelfItem item);
	void shouldStop();
	void waitQuit();
}
