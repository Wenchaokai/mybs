package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:BillType Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-1
 */
public class BillType implements Serializable {

	private static final long serialVersionUID = -918576625844015592L;

	private Integer id;
	private Integer billType;
	private String billName;
	private Boolean checked = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
