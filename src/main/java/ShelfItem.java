/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author feisi.fs
 * @version $Id: ShelfItem, v0.1 2020Äê11ÔÂ28ÈÕ 10:08 PM feisi.fs Exp $
 */
@Getter
@Setter
@ToString
public class ShelfItem {
	private Order order;
	private long cookedTime;
	private long courierAvailableTime;
	private long timeToDeliver;
	private double timeToWaste;

	public ShelfItem(Order order, Integer courierDelay) {
		this.order = order;
		long currentTime = currentTimeSeconds();
		cookedTime = currentTime;
		courierAvailableTime = currentTime + courierDelay;
	}

	public void update(int decayModifier) {
		long currentTime = currentTimeSeconds();
		timeToDeliver = courierAvailableTime - currentTime;
		long orderAge = currentTime - cookedTime;
		timeToWaste = (order.getShelfLife() - orderAge - order.getDecayRate() * orderAge * decayModifier) / order.getShelfLife();
	}

	private long currentTimeSeconds() {
		return System.currentTimeMillis() / 1000;
	}
}
