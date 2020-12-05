/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

/**
 * @author feisi.fs
 * @version $Id: OrderFactory, v0.1 2020Äê11ÔÂ28ÈÕ 3:45 PM feisi.fs Exp $
 */
public interface OrderFactory {
	boolean loadOrders(String orderFile);
	Order fetchOrder();
}
