package com.best.domain;

import java.io.Serializable;

/**
 * ClassName:WMSWareHouse Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-10
 */
public class WMSWareHouse implements Serializable {

	private static final long serialVersionUID = -485635795556265481L;

	private Integer id;
	private String wmsWareHouseCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWmsWareHouseCode() {
		return wmsWareHouseCode;
	}

	public void setWmsWareHouseCode(String wmsWareHouseCode) {
		this.wmsWareHouseCode = wmsWareHouseCode;
	}

}
