/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

import lombok.Setter;

/**
 * @author feisi.fs
 * @version $Id: OrderDispatcher, v0.1 2020Äê11ÔÂ28ÈÕ 4:38 PM feisi.fs Exp $
 */
@Setter
public class OrderDispatcher extends Thread {
	private String orderFile = "orders.json";
	private Kitchen kitchen;
	private OrderFactory orderFactory;

	public OrderDispatcher() {
		super();
		setName(OrderDispatcher.class.getSimpleName());
	}

	@Override
	public void run() {
		if (orderFactory.loadOrders(orderFile)) {
			for (;;) {
				Order order = orderFactory.fetchOrder();
				if (order == null) {
					break;
				}
				kitchen.receiveOrder(order);
			}
		}
		kitchen.stop();
	}
}
