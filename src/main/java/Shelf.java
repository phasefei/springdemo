/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

/**
 * @author feisi.fs
 * @version $Id: Shelf, v0.1 2020Äê11ÔÂ29ÈÕ 8:30 PM feisi.fs Exp $
 */
public interface Shelf {
	boolean place(ShelfItem item);
	void shouldStop();
	void waitQuit();
}
