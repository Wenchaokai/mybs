package com.best.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * ClassName:User Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-27
 */
public class User implements Serializable {

	private static final long serialVersionUID = -398744926531951923L;

	private Long userId;
	private String userCount;
	private String userName;
	private String userPassword;
	private String userCustomers;
	private String userWareHouses;
	private String userWareHousesProperties;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserCustomers() {
		return userCustomers;
	}

	public void setUserCustomers(String userCustomers) {
		this.userCustomers = userCustomers;
	}

	public Set<String> getCustomerCodes() {
		Set<String> customerCodes = new HashSet<String>();
		if (StringUtils.isNotBlank(userCustomers)) {
			String[] parts = userCustomers.split(",");
			for (String part : parts) {
				customerCodes.add(part.split("#")[0]);
			}
		}
		return customerCodes;
	}

	public Set<String> getWareHouseCodes() {
		Set<String> wareHouseCode = new HashSet<String>();
		if (StringUtils.isNotBlank(userWareHouses)) {
			String[] parts = userWareHouses.split(",");
			for (String part : parts) {
				wareHouseCode.add(part.split("#")[0]);
			}
		}
		return wareHouseCode;
	}

	public String getUserWareHouses() {
		return userWareHouses;
	}

	public void setUserWareHouses(String userWareHouses) {
		this.userWareHouses = userWareHouses;
	}

	public String getUserWareHousesProperties() {
		return userWareHousesProperties;
	}

	public void setUserWareHousesProperties(String userWareHousesProperties) {
		this.userWareHousesProperties = userWareHousesProperties;
	}

	public String formatCustomerCode() {
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotBlank(userCustomers)) {
			String[] parts = this.userCustomers.split(",");
			boolean isOr = false;
			for (String part : parts) {
				if (isOr)
					buffer.append("<br>");
				buffer.append(part.split("#")[1]);
				isOr = true;
			}
		}
		return buffer.toString();
	}

	public String formatWareHouse() {
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotBlank(this.userWareHouses)) {
			String[] parts = this.userWareHouses.split(",");
			boolean isOr = false;
			for (String part : parts) {
				if (isOr)
					buffer.append(" <br> ");
				buffer.append(part.split("#")[1]);
				isOr = true;
			}
		}
		return buffer.toString();
	}

}
