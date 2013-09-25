package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:SKU Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-7
 */
public class SKU implements Serializable {

	private static final long serialVersionUID = -7769435892023214203L;

	private String skuCode;
	private String skuName;

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

}
