/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author feisi.fs
 * @version $Id: App, v0.1 2020Äê10ÔÂ27ÈÕ 10:37 AM feisi.fs Exp $
 */
public class App {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		OrderDispatcher orderDispatcher = ctx.getBean(OrderDispatcher.class);
		orderDispatcher.start();
		orderDispatcher.join();

		ctx.close();
	}
}
