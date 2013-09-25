package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:SalesShipping Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-18
 */
public class SalesShipping implements Serializable {

	private static final long serialVersionUID = 313136900330878238L;

	private Integer id;
	private String lastUpdateTime;
	private String remark;
	private String acceptAddress;
	private Integer shippingOrderId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastUpdateTime() {
		if (null == lastUpdateTime)
			lastUpdateTime = "";
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getRemark() {
		if (null == remark)
			remark = "";
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAcceptAddress() {
		if (null == acceptAddress)
			acceptAddress = "";
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public Integer getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Integer shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

}
