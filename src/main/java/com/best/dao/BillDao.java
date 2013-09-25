package com.best.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.best.domain.BillInfo;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:BillDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-29
 */
@Repository("billDao")
public class BillDao extends BaseDao {

	@Resource(name = "sqlMapClient")
	protected SqlMapClient sqlMapClient;

	private static final String space = "billSpace.";

	public Object insertBill(BillInfo bill) {
		return insert(space + "INSERT_BILL", bill, sqlMapClient);
	}

	public int updateBillStatus(Long billId, Integer billStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billId", billId);
		map.put("billStatus", billStatus);
		return update(space + "UPDATE_BILL_STATUS", map, sqlMapClient);
	}

	public Object deleteBill(Long billId) {
		return delete(space + "DELETE_BILL", billId, sqlMapClient);
	}

	public Object getBill(Long billId) {
		return object(space + "GET_BILL_BY_ID", billId, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<BillInfo> findBillByPageSize(String billCustomerCode, String billWareHouseCode, Integer billType,
			String startTime, String endTime, Integer startIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectString = "GET_BILL_BY_PAGESIZE";
		if (StringUtils.isNotBlank(billCustomerCode) && !billCustomerCode.equals("-1")) {
			map.put("billCustomerCode", billCustomerCode);
		}
		if (StringUtils.isNotBlank(billWareHouseCode) && !billWareHouseCode.equals("-1")) {
			map.put("billWareHouseCode", billWareHouseCode);
		}
		if (null != billType)
			map.put("billType", billType);
		if (StringUtils.isNotBlank(startTime)) {
			map.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime);
		}

		map.put("pageSize", pageSize);
		map.put("startIndex", startIndex * pageSize);

		return (List<BillInfo>) this.list(space + selectString, map, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<BillInfo> findBillByPageSize(List<String> billCustomerCodes, List<String> billWareHouseCodes, Integer billType,
			String startTime, String endTime, Integer startIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectString = "GET_BILLS_BY_PAGESIZE";
		if (null != billCustomerCodes) {
			if (billCustomerCodes.size() == 0)
				billCustomerCodes.add("-1");
			map.put("billCustomerCodes", billCustomerCodes);
		} else {
			billCustomerCodes = new ArrayList<String>();
			billCustomerCodes.add("-1");
			map.put("billCustomerCodes", billCustomerCodes);
		}
		if (null != billWareHouseCodes) {
			if (billWareHouseCodes.size() == 0)
				billWareHouseCodes.add("-1");
			map.put("billWareHouseCodes", billWareHouseCodes);
		} else {
			billWareHouseCodes = new ArrayList<String>();
			billWareHouseCodes.add("-1");
			map.put("billWareHouseCodes", billWareHouseCodes);
		}
		if (null != billType)
			map.put("billType", billType);
		if (StringUtils.isNotBlank(startTime)) {
			map.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime);
		}

		map.put("pageSize", pageSize);
		map.put("startIndex", startIndex * pageSize);

		return (List<BillInfo>) this.list(space + selectString, map, sqlMapClient);
	}

	public Integer findBillSize(String billCustomerCode, String billWareHouseCode, Integer billType, String startTime,
			String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectString = "GET_BILL_BY_TOTALSIZE";
		if (StringUtils.isNotBlank(billCustomerCode) && !billCustomerCode.equals("-1")) {
			map.put("billCustomerCode", billCustomerCode);
		}
		if (StringUtils.isNotBlank(billWareHouseCode) && !billWareHouseCode.equals("-1")) {
			map.put("billWareHouseCode", billWareHouseCode);
		}
		if (null != billType)
			map.put("billType", billType);
		if (StringUtils.isNotBlank(startTime)) {
			map.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime);
		}

		Integer res = (Integer) this.object(space + selectString, map, sqlMapClient);
		if (res % pageSize == 0)
			return res / pageSize;
		return res / pageSize + 1;
	}

	public Integer findBillSize(List<String> billCustomerCodes, List<String> billWareHouseCodes, Integer billType,
			String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectString = "GET_BILLS_BY_TOTALSIZE";
		if (null != billCustomerCodes) {
			if (billCustomerCodes.size() == 0)
				billCustomerCodes.add("-1");
			map.put("billCustomerCodes", billCustomerCodes);
		} else {
			billCustomerCodes = new ArrayList<String>();
			billCustomerCodes.add("-1");
			map.put("billCustomerCodes", billCustomerCodes);
		}
		if (null != billWareHouseCodes) {
			if (billWareHouseCodes.size() == 0)
				billWareHouseCodes.add("-1");
			map.put("billWareHouseCodes", billWareHouseCodes);
		} else {
			billWareHouseCodes = new ArrayList<String>();
			billWareHouseCodes.add("-1");
			map.put("billWareHouseCodes", billWareHouseCodes);
		}
		if (null != billType)
			map.put("billType", billType);
		if (StringUtils.isNotBlank(startTime)) {
			map.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime);
		}

		Integer res = (Integer) this.object(space + selectString, map, sqlMapClient);
		if (res % pageSize == 0)
			return res / pageSize;
		return res / pageSize + 1;
	}

	public void updateBill(BillInfo billInfo) {
		this.update(space + "UPDATE_BILL_INFO", billInfo, sqlMapClient);
	}
}
