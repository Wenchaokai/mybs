package com.best.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * ClassName:WmsStatistic Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-23
 */
public class WmsStatistic implements Serializable {

	private static final long serialVersionUID = -3356956648319611341L;

	private Integer qtyEach = 0;
	private Integer qtyOnholdEach = 0;
	private Integer qtyUseEach = 0;
	private Integer qtyHold4PAEach = 0;
	private String dayTime;
	private String skuCode;
	private String skuName;
	private String orgId;
	private String wareHouseId;
	private String wareHouseCode;
	private String wareHouseName;
	private String customerId;
	private String customerCode;
	private String customerName;
	private String udf1;
	private String udf2;
	private String udf3;
	private String udf4;
	private String udf5;
	private String udf6;
	private String udf7;
	private String udf8;
	private String price;
	private String skuHigh;
	private String skuLength;
	private String skuWidth;

	public static final String[] properties = new String[] { "UDF1", "UDF2", "UDF3", "UDF4", "UDF5", "UDF6", "UDF7", "UDF8",
			"SKULENGTH", "SKUWIDTH", "SKUHIGH", "PRICE" };

	public Integer getQtyEach() {
		return qtyEach == null ? 0 : qtyEach;
	}

	public void setQtyEach(Integer qtyEach) {
		this.qtyEach = qtyEach;
	}

	public Integer getQtyOnholdEach() {
		return qtyOnholdEach == null ? 0 : qtyOnholdEach;
	}

	public void setQtyOnholdEach(Integer qtyOnholdEach) {
		this.qtyOnholdEach = qtyOnholdEach;
	}

	public Integer getQtyUseEach() {
		return qtyUseEach == null ? 0 : qtyUseEach;
	}

	public void setQtyUseEach(Integer qtyUseEach) {
		this.qtyUseEach = qtyUseEach;
	}

	public Integer getQtyHold4PAEach() {
		return qtyHold4PAEach == null ? 0 : qtyHold4PAEach;
	}

	public void setQtyHold4PAEach(Integer qtyHold4PAEach) {
		this.qtyHold4PAEach = qtyHold4PAEach;
	}

	public String getDayTime() {
		return dayTime;
	}

	public void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getWareHouseCode() {
		return wareHouseCode;
	}

	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUdf1() {
		return udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public String getUdf3() {
		return udf3;
	}

	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}

	public String getUdf4() {
		return udf4;
	}

	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}

	public String getUdf5() {
		return udf5;
	}

	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}

	public String getUdf6() {
		return udf6;
	}

	public void setUdf6(String udf6) {
		this.udf6 = udf6;
	}

	public String getUdf7() {
		return udf7;
	}

	public void setUdf7(String udf7) {
		this.udf7 = udf7;
	}

	public String getUdf8() {
		return udf8;
	}

	public void setUdf8(String udf8) {
		this.udf8 = udf8;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSkuHigh() {
		return skuHigh;
	}

	public void setSkuHigh(String skuHigh) {
		this.skuHigh = skuHigh;
	}

	public String getSkuLength() {
		return skuLength;
	}

	public void setSkuLength(String skuLength) {
		this.skuLength = skuLength;
	}

	public String getSkuWidth() {
		return skuWidth;
	}

	public void setSkuWidth(String skuWidth) {
		this.skuWidth = skuWidth;
	}

	public String getUdf2() {
		return udf2;
	}

	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	Map<Integer, String> values = new HashMap<Integer, String>();

	public String getUDF(Integer index) {
		if (values.size() == 0) {
			values.put(1, udf1);
			values.put(2, udf2);
			values.put(3, udf3);
			values.put(4, udf4);
			values.put(5, udf5);
			values.put(6, udf6);
			values.put(7, udf7);
			values.put(8, udf8);
			values.put(9, skuLength);
			values.put(10, skuWidth);
			values.put(11, skuHigh);
			values.put(12, price);
		}
		String value = values.get(index);
		if (StringUtils.isBlank(value))
			return "";
		return value;
	}

}
