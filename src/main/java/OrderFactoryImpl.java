/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author feisi.fs
 * @version $Id: OrderFactoryImpl, v0.1 2020Äê11ÔÂ28ÈÕ 2:50 PM feisi.fs Exp $
 */
public class OrderFactoryImpl implements OrderFactory {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Logger logger = LoggerFactory.getLogger(OrderFactoryImpl.class);
	private List<Order> orders;
	private int index = 0;

	@Override
	public boolean loadOrders(String orderFile) {
		try {
			orders = mapper.readValue(this.getClass().getResourceAsStream(orderFile),
					mapper.getTypeFactory().constructParametricType(List.class, Order.class));
			if (orders != null && orders.size() > 0) {
				logger.info("load {} orders", orders.size());
				return true;
			} else {
				logger.info("load 0 orders");
				return false;
			}
		} catch	(Exception e) {
			logger.error("load orders failed, file = {}, {}", orderFile, e.toString());
			return false;
		}
	}

	@Override
	public Order fetchOrder() {
		return index < orders.size() ? orders.get(index++) : null;
	}

	public static void main(String[] args) {
		OrderFactoryImpl orderFactory = new OrderFactoryImpl();
		orderFactory.loadOrders("orders.json");
	}
}
