package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:ShippingOrder Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-7
 */
public class ShippingOrder implements Serializable {

	private static final long serialVersionUID = -6771005486345811719L;

	private Integer numCount;
	private String dateTime;
	private String shippingOrderName;
	private String diffTime = "1";

	public Integer getNumCount() {
		return numCount;
	}

	public void setNumCount(Integer numCount) {
		this.numCount = numCount;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getShippingOrderName() {
		return shippingOrderName;
	}

	public void setShippingOrderName(String shippingOrderName) {
		this.shippingOrderName = shippingOrderName;
	}

	public String getDiffTime() {
		if (diffTime == null)
			diffTime = "0";
		return diffTime;
	}

	public void setDiffTime(String diffTime) {
		this.diffTime = diffTime;
	}

}
