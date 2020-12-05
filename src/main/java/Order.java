/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author feisi.fs
 * @version $Id: Order, v0.1 2020Äê11ÔÂ28ÈÕ 2:38 PM feisi.fs Exp $
 */
@Getter
@Setter
@ToString
public class Order {
	private String id;
	private String name;
	private String temp;
	private Integer shelfLife;
	private Double decayRate;
}
