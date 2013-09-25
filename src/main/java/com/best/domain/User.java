package com.best.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
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
	private Integer userRole = 0;
	private Integer userSecurity = 0;
	private Map<Integer, String> propertyMap;

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

	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		if (StringUtils.isNotBlank(userCustomers)) {
			String[] parts = userCustomers.split(",");
			for (String part : parts) {
				Customer customer = new Customer();
				customer.setCustomerCode(part.split("#")[0]);
				customer.setCustomerName(part.split("#")[1]);
				customers.add(customer);
			}
		}
		return customers;
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

	public List<WareHouse> getWareHouses() {
		List<WareHouse> wareHouses = new ArrayList<WareHouse>();
		if (StringUtils.isNotBlank(userWareHouses)) {
			String[] parts = userWareHouses.split(",");
			for (String part : parts) {
				WareHouse house = new WareHouse();
				house.setWareHouseCode(part.split("#")[0]);
				house.setWareHouseName(part.split("#")[1]);
				wareHouses.add(house);
			}
		}
		return wareHouses;
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

	private boolean isChinese(char ch) {
		// 获取此字符的UniCodeBlock
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		// GENERAL_PUNCTUATION 判断中文的“号
		// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
		// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			System.out.println(ch + " 是中文");
			return true;
		}
		return false;
	}

	public Map<Integer, String> formatTwoProperties() {
		Map<Integer, String> propertyMap = new TreeMap<Integer, String>();
		if (StringUtils.isNotBlank(this.userWareHousesProperties)) {
			String[] parts = this.userWareHousesProperties.split(",");
			for (String part : parts) {
				String[] temp = part.split("#");
				String value = temp.length > 1 ? temp[1] : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				String newValue = "";
				int len = 0;
				for (int i = 0; i < 4 && i < value.length(); i++) {
					char tmp = value.charAt(i);
					if (isChinese(tmp)) {
						newValue += tmp;
						len += 2;
					} else {
						// 非中文字符
						newValue += tmp;
						len++;
					}
					if (len >= 4)
						break;
				}
				while (len < 4) {
					newValue += "&nbsp;&nbsp;";
					len++;
				}
				propertyMap.put(Integer.parseInt(temp[0]), newValue);
			}
		}
		return propertyMap;
	}

	public void formatProperties() {
		propertyMap = new TreeMap<Integer, String>();
		if (StringUtils.isNotBlank(this.userWareHousesProperties)) {
			String[] parts = this.userWareHousesProperties.split(",");
			for (String part : parts) {
				propertyMap.put(Integer.parseInt(part.split("#")[0]), part.split("#").length > 1 ? part.split("#")[1] : "");
			}
		}
	}

	public Map<Integer, String> getTwoPropertyMap() {
		return formatTwoProperties();
	}

	public Map<Integer, String> getPropertyMap() {
		if (propertyMap == null)
			formatProperties();
		return propertyMap;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Integer getUserSecurity() {
		return userSecurity;
	}

	public void setUserSecurity(Integer userSecurity) {
		this.userSecurity = userSecurity;
	}

	public Boolean checkedSecurity(Integer security) {
		boolean flag = true;
		if (CollectionUtils.isEmpty(this.getCustomerCodes()) || CollectionUtils.isEmpty(this.getWareHouseCodes()))
			flag = false;
		if (!flag)
			return Boolean.FALSE;
		return (this.userSecurity & security) > 0 ? true : false;
	}

}
