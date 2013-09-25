package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:Statistic Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-10
 */
public class IdoStatistic implements Serializable {

	private static final long serialVersionUID = -3345443060489273681L;

	private Integer numCount;
	private String dateTime;
	private String wareHouseCode;
	private String wareHouseName;
	private String province;
	private String skuCode;
	private String skuDesc;
	private Integer price;
	private Float qtyOrdered;
	private String customerName;
	private Integer id;

	public String getCustomerName() {
		if (customerName == null)
			customerName = "";
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWareHouseName() {
		if (null == wareHouseName)
			wareHouseName = "";
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getQtyOrdered() {
		return qtyOrdered;
	}

	public void setQtyOrdered(Float qtyOrdered) {
		this.qtyOrdered = qtyOrdered;
	}

	public String getSkuDesc() {
		if (null == skuDesc)
			skuDesc = "";
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getNumCount() {
		return numCount;
	}

	public void setNumCount(Integer numCount) {
		this.numCount = numCount;
	}

	public String getDateTime() {
		if (null == dateTime)
			dateTime = "";
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getWareHouseCode() {
		return wareHouseCode;
	}

	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSkuCode() {
		if (null == skuCode)
			skuCode = "";
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

}
