package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:ECBOSSCustomer Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-6
 */
public class Customer implements Serializable {

	private static final long serialVersionUID = -2697473851668138950L;

	private Integer id;
	private String customerName;
	private String customerCode;
	private Boolean checked = Boolean.FALSE;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		if (customerName.length() > 10) {
			int index = customerName.indexOf("(");
			if (index < 0)
				return customerName.substring(0, 10);
			else
				return customerName.substring(0, index);
		}
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
