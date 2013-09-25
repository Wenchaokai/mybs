package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:BillInfo Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-29
 */
public class BillInfo implements Serializable {

	private static final long serialVersionUID = -3163195583477031673L;

	private Long billId;
	private Integer billType;
	private String billDate;
	private Integer billStatus;
	private Long billAccount;
	private String billWarehouseCode;
	private String billWarehouseName;
	private String billCustomerCode;
	private String billCustomerName;
	private String billSummaryPath;
	private String billDetailPath;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public Long getBillAccount() {
		return billAccount;
	}

	public void setBillAccount(Long billAccount) {
		this.billAccount = billAccount;
	}

	public String getBillWarehouseCode() {
		return billWarehouseCode;
	}

	public void setBillWarehouseCode(String billWarehouseCode) {
		this.billWarehouseCode = billWarehouseCode;
	}

	public String getBillWarehouseName() {
		return billWarehouseName;
	}

	public void setBillWarehouseName(String billWarehouseName) {
		this.billWarehouseName = billWarehouseName;
	}

	public String getBillCustomerCode() {
		return billCustomerCode;
	}

	public void setBillCustomerCode(String billCustomerCode) {
		this.billCustomerCode = billCustomerCode;
	}

	public String getBillCustomerName() {
		return billCustomerName;
	}

	public void setBillCustomerName(String billCustomerName) {
		this.billCustomerName = billCustomerName;
	}

	public String getBillSummaryPath() {
		return billSummaryPath;
	}

	public void setBillSummaryPath(String billSummaryPath) {
		this.billSummaryPath = billSummaryPath;
	}

	public String getBillDetailPath() {
		return billDetailPath;
	}

	public void setBillDetailPath(String billDetailPath) {
		this.billDetailPath = billDetailPath;
	}

}
