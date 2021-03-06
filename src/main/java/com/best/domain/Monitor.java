package com.best.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * ClassName:Monitor Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-31
 */
public class Monitor implements Serializable {

	private static final long serialVersionUID = 3277832334829896335L;

	private Long monitorId;
	private String monitorName;
	private String monitorCustomerName;
	private String monitorCustomerCode;
	private String monitorWarehouseNameList;
	private String monitorWarehouseCodeList;
	private Integer monitorStatus;
	private String monitorStartTime;
	private String monitorIndexList;
	private Long monitorResponserId;
	private String monitorSku;
	private String userCount;

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public Long getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(Long monitorId) {
		this.monitorId = monitorId;
	}

	public String getMonitorCustomerName() {
		return monitorCustomerName;
	}

	public void setMonitorCustomerName(String monitorCustomerName) {
		this.monitorCustomerName = monitorCustomerName;
	}

	public String getMonitorCustomerCode() {
		return monitorCustomerCode;
	}

	public void setMonitorCustomerCode(String monitorCustomerCode) {
		this.monitorCustomerCode = monitorCustomerCode;
	}

	public String getMonitorWarehouseNameList() {
		return monitorWarehouseNameList;
	}

	public void setMonitorWarehouseNameList(String monitorWarehouseNameList) {
		this.monitorWarehouseNameList = monitorWarehouseNameList;
	}

	public String getMonitorWarehouseCodeList() {
		return monitorWarehouseCodeList;
	}

	public void setMonitorWarehouseCodeList(String monitorWarehouseCodeList) {
		this.monitorWarehouseCodeList = monitorWarehouseCodeList;
	}

	public Integer getMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(Integer monitorStatus) {
		this.monitorStatus = monitorStatus;
	}

	public String getMonitorStartTime() {
		return monitorStartTime;
	}

	public void setMonitorStartTime(String monitorStartTime) {
		this.monitorStartTime = monitorStartTime;
	}

	public String getMonitorIndexList() {
		return monitorIndexList;
	}

	public void setMonitorIndexList(String monitorIndexList) {
		this.monitorIndexList = monitorIndexList;
	}

	public Long getMonitorResponserId() {
		return monitorResponserId;
	}

	public void setMonitorResponserId(Long monitorResponserId) {
		this.monitorResponserId = monitorResponserId;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public List<WareHouse> getWareHouses() {
		String[] wareHouseCodes = this.monitorWarehouseCodeList.split(",");
		String[] wareHouseNames = this.monitorWarehouseNameList.split(",");
		int size = wareHouseCodes.length < wareHouseNames.length ? wareHouseCodes.length : wareHouseNames.length;
		List<WareHouse> wareHouses = new ArrayList<WareHouse>();
		for (int index = 0; index < size; index++) {
			WareHouse wareHouse = new WareHouse();
			wareHouse.setWareHouseName(wareHouseNames[index]);
			wareHouse.setWareHouseCode(wareHouseCodes[index]);
			wareHouses.add(wareHouse);
		}
		return wareHouses;
	}

	public Set<String> getMonitorWareHouseCode() {
		Set<String> res = new HashSet<String>();
		if (StringUtils.isNotBlank(monitorWarehouseCodeList)) {
			String[] parts = monitorWarehouseCodeList.split(",");
			for (String code : parts) {
				res.add(code);
			}
		}
		return res;
	}

	public Map<String, WareHouse> getMonitorWareHouse() {
		Map<String, WareHouse> res = new HashMap<String, WareHouse>();
		if (StringUtils.isNotBlank(monitorWarehouseCodeList) && StringUtils.isNotBlank(monitorWarehouseNameList)) {
			String[] codeParts = monitorWarehouseCodeList.split(",");
			String[] nameParts = monitorWarehouseNameList.split(",");
			for (int index = 0; index < codeParts.length && index < nameParts.length; index++) {
				WareHouse wareHouse = new WareHouse();
				wareHouse.setWareHouseCode(codeParts[index]);
				wareHouse.setWareHouseName(nameParts[index]);
				res.put(codeParts[index], wareHouse);
			}
		}
		return res;
	}

	public Set<Integer> getMonitorIndexSet() {
		Set<Integer> res = new HashSet<Integer>();
		if (StringUtils.isNotBlank(monitorIndexList)) {
			String[] parts = monitorIndexList.split(",");
			for (String id : parts) {
				res.add(Integer.parseInt(id));
			}
		}
		return res;
	}

	public List<Integer> getMonitorIndex() {
		Set<Integer> res = new HashSet<Integer>();
		if (StringUtils.isNotBlank(monitorIndexList)) {
			String[] parts = monitorIndexList.split(",");
			for (String id : parts) {
				res.add(Integer.parseInt(id));
			}
		}
		return new ArrayList<Integer>(res);
	}

	public String getMonitorSku() {
		return monitorSku;
	}

	public void setMonitorSku(String monitorSku) {
		monitorSku = monitorSku.replace('；', ';');
		this.monitorSku = monitorSku;
	}

	public List<String> getMonitorSkus() {
		List<String> res = new ArrayList<String>();
		if (StringUtils.isNotBlank(monitorSku)) {
			String[] parts = monitorSku.split(";|；");
			for (String sku : parts) {
				res.add(sku.trim());
			}
		}
		return res;
	}

}
