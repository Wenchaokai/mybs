package com.best.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:ShippingOrderData Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-18
 */
public class ShippingOrderData implements Serializable {

	private static final long serialVersionUID = 7178246064947968871L;

	private Integer id;
	private String relationNo;
	private String whCode;
	private String wareHouseName;
	private String consigneeName;
	private String logisticsProviderName;
	private String consigneePhone;
	private String shippingOrderNo;
	private String shippingTime;
	private String status;
	private String statusName;
	private Integer ldoId;
	private String ldoCode;
	private Float cubic;
	private String consumable;
	private Float netWeight;
	private Float grossWeight;
	private List<SalesShipping> salesShippings = new ArrayList<SalesShipping>();
	private List<IdoStatistic> idoStatistics = new ArrayList<IdoStatistic>();

	public Integer getLdoId() {
		return ldoId;
	}

	public void setLdoId(Integer ldoId) {
		this.ldoId = ldoId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRelationNo() {
		return relationNo;
	}

	public void setRelationNo(String relationNo) {
		this.relationNo = relationNo;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getLogisticsProviderName() {
		return logisticsProviderName;
	}

	public void setLogisticsProviderName(String logisticsProviderName) {
		this.logisticsProviderName = logisticsProviderName;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getShippingOrderNo() {
		return shippingOrderNo;
	}

	public void setShippingOrderNo(String shippingOrderNo) {
		this.shippingOrderNo = shippingOrderNo;
	}

	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void addSalesShipping(SalesShipping salesShipping) {
		salesShippings.add(salesShipping);
	}

	public List<SalesShipping> getSalesShippings() {
		return salesShippings;
	}

	public List<IdoStatistic> getIdoStatistics() {
		return idoStatistics;
	}

	public void addIdoStatistics(IdoStatistic idoStatistic) {
		idoStatistics.add(idoStatistic);
	}

	public void setIdoStatistics(List<IdoStatistic> idoStatistics) {
		this.idoStatistics = idoStatistics;
	}

	public String getLdoCode() {
		return ldoCode;
	}

	public void setLdoCode(String ldoCode) {
		this.ldoCode = ldoCode;
	}

	public Float getCubic() {
		if (cubic == null)
			cubic = 0.0F;
		return cubic;
	}

	public void setCubic(Float cubic) {
		this.cubic = cubic;
	}

	public String getConsumable() {
		if (consumable == null)
			consumable = "";
		return consumable;
	}

	public void setConsumable(String consumable) {
		this.consumable = consumable;
	}

	public Float getNetWeight() {
		if (netWeight == null)
			netWeight = 0.0F;
		return netWeight;
	}

	public void setNetWeight(Float netWeight) {
		this.netWeight = netWeight;
	}

	public Float getGrossWeight() {
		if (grossWeight == null)
			grossWeight = 0.0F;
		return grossWeight;
	}

	public void setGrossWeight(Float grossWeight) {
		this.grossWeight = grossWeight;
	}

	public void setSalesShippings(List<SalesShipping> salesShippings) {
		this.salesShippings = salesShippings;
	}

}
