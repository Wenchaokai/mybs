package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:OrderData Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-11
 */
public class OrderData implements Serializable {

	private static final long serialVersionUID = 8563376388360892389L;

	private Integer orderSum = 0;
	private Integer totalMount = 0;
	private Integer deliveredCount = 0;
	private Integer cancelCount = 0;
	private Integer paymentCount = 0;
	private String month;

	public Integer getOrderSum() {
		return orderSum == null ? 0 : orderSum;
	}

	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
	}

	public Integer getTotalMount() {
		return totalMount == null ? 0 : totalMount;
	}

	public void setTotalMount(Integer totalMount) {
		this.totalMount = totalMount;
	}

	public Integer getDeliveredCount() {
		return deliveredCount == null ? 0 : deliveredCount;
	}

	public void setDeliveredCount(Integer deliveredCount) {
		this.deliveredCount = deliveredCount;
	}

	public Integer getCancelCount() {
		return cancelCount == null ? 0 : cancelCount;
	}

	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}

	public Integer getPaymentCount() {
		return paymentCount == null ? 0 : paymentCount;
	}

	public void setPaymentCount(Integer paymentCount) {
		this.paymentCount = paymentCount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

}
