package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:SalesOrder Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-18
 */
public class SalesOrder implements Serializable {

	private static final long serialVersionUID = 7124974981231067864L;
	private Integer id;
	private String orderCode;
	private String refOrderCode;
	private String orderSource;
	private String orderTime;
	private String note;
	private String status;
	private String statusName;
	private String ldoCode;
	private Integer totalAmount;
	private String userName;
	private String phone;
	private String postalCode;
	private String email;
	private String shippingAddress;
	private String province;
	private String city;
	private String district;
	private Integer customerId;
	private String customerName;
	private String wareHouseName;
	private Integer wareHouseId;
	private String mobilePhone;
	private String acceptFailNotifyStatus;
	private String deliveredNotiifyStatus;
	private String canceledNotifyStatus;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getMobilePhone() {
		if (mobilePhone == null)
			mobilePhone = "";
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderCode() {
		if (orderCode == null)
			orderCode = "";
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getRefOrderCode() {
		if (refOrderCode == null)
			refOrderCode = "";
		return refOrderCode;
	}

	public void setRefOrderCode(String refOrderCode) {
		this.refOrderCode = refOrderCode;
	}

	public String getOrderSource() {
		if (orderSource == null)
			orderSource = "";
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getOrderTime() {
		if (orderTime == null)
			orderTime = "";
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getNote() {
		if (note == null)
			note = "";
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLdoCode() {
		if (ldoCode == null)
			ldoCode = "";
		return ldoCode;
	}

	public void setLdoCode(String ldoCode) {
		this.ldoCode = ldoCode;
	}

	public Integer getTotalAmount() {
		if (totalAmount == null)
			totalAmount = 0;
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getUserName() {
		if (userName == null)
			userName = "";
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		if (phone == null)
			phone = "";
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostalCode() {
		if (postalCode == null)
			postalCode = "";
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEmail() {
		if (email == null)
			email = "";
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShippingAddress() {
		if (shippingAddress == null)
			shippingAddress = "";
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getProvince() {
		if (province == null)
			province = "";
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		if (city == null)
			city = "";
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		if (district == null)
			district = "";
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStatusName() {
		if (statusName == null)
			statusName = "";
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCustomerName() {
		if (customerName == null)
			customerName = "";
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWareHouseName() {
		if (wareHouseName == null)
			wareHouseName = "";
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public String getAcceptFailNotifyStatus() {
		return acceptFailNotifyStatus;
	}

	public void setAcceptFailNotifyStatus(String acceptFailNotifyStatus) {
		this.acceptFailNotifyStatus = acceptFailNotifyStatus;
	}

	public String getDeliveredNotiifyStatus() {
		return deliveredNotiifyStatus;
	}

	public void setDeliveredNotiifyStatus(String deliveredNotiifyStatus) {
		this.deliveredNotiifyStatus = deliveredNotiifyStatus;
	}

	public String getCanceledNotifyStatus() {
		return canceledNotifyStatus;
	}

	public void setCanceledNotifyStatus(String canceledNotifyStatus) {
		this.canceledNotifyStatus = canceledNotifyStatus;
	}

}
