package com.best.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.best.domain.IdoStatistic;
import com.best.domain.OrderData;
import com.best.domain.SalesOrder;
import com.best.domain.SalesShipping;
import com.best.domain.ShippingOrder;
import com.best.domain.ShippingOrderData;
import com.best.utils.DateUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:StatisticDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-10
 */
@Repository("statisticDao")
public class StatisticDao extends BaseDao {

	private static final String space = "statisticSpace.";

	@Resource(name = "ecbossSqlMapClient")
	protected SqlMapClient sqlMapClient;

	// @SuppressWarnings("unchecked")
	// public List<IdoStatistic> getWareHouseSkuIdoCount(String skuCode,
	// List<String> wareHouseCodeList, String customerCode,
	// String dateTime, String endTime) {
	// long betweenTime = 10;
	// try {
	// betweenTime = DateUtil.betweenDayTime(dateTime, endTime);
	// } catch (ParseException e) {
	// }
	// if (betweenTime >= 10)
	// return new ArrayList<IdoStatistic>();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// if (!"-1".equals(skuCode))
	// map.put("skuCode", skuCode);
	// if (null != wareHouseCodeList)
	// map.put("wareHouseCodeList", wareHouseCodeList);
	// map.put("customerCode", customerCode);
	// map.put("startTime", dateTime);
	// try {
	// map.put("endTime", DateUtil.getNextDate(endTime));
	// } catch (ParseException e) {
	// map.put("endTime", endTime);
	// }
	//
	// return (List<IdoStatistic>) this.list(space + "SELECT_SKU_WAREHOUSE_IDO",
	// map, sqlMapClient);
	// }

	@SuppressWarnings("unchecked")
	public List<IdoStatistic> getDistributedSkuIdoCount(String skuCode, String customerCode, String wareHouseCode,
			String dateTime, String endTime) {
		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(dateTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return new ArrayList<IdoStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(skuCode) && !"-1".equals(skuCode))
			map.put("skuCode", skuCode);
		map.put("customerCode", customerCode);
		if (StringUtils.isNotBlank(wareHouseCode) && !"-1".equals(wareHouseCode))
			map.put("whCode", wareHouseCode);
		map.put("startTime", dateTime);
		try {
			map.put("endTime", DateUtil.getNextDate(endTime));
		} catch (ParseException e) {
			map.put("endTime", endTime);
		}

		return (List<IdoStatistic>) this.list(space + "SELECT_SKU_PROVINCE_IDO", map, sqlMapClient);
	}

	// @SuppressWarnings("unchecked")
	// public List<IdoStatistic> getPercentSkuIdoCount(String skuCode, String
	// customerCode, String startTime, String endTime) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("skuCode", skuCode);
	// map.put("customerCode", customerCode);
	// map.put("startTime", startTime);
	// map.put("endTime", endTime);
	//
	// return (List<IdoStatistic>) this.list(space + "SELECT_SKU_PERCENT_IDO",
	// map, sqlMapClient);
	// }

	@SuppressWarnings("unchecked")
	public List<ShippingOrder> getCarrierDistributedCount(String customerCode, String wareHouseCode, String startTime,
			String endTime) {
		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(startTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return new ArrayList<ShippingOrder>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		if (StringUtils.isNotBlank(wareHouseCode) && !wareHouseCode.equals("-1"))
			map.put("wareHouseCode", wareHouseCode);
		map.put("startTime", startTime);
		try {
			map.put("endTime", DateUtil.getNextDate(endTime));
		} catch (ParseException e) {
			map.put("endTime", endTime);
		}
		return (List<ShippingOrder>) this.list(space + "SELECT_CARRIER_DISTRIBUTED", map, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<ShippingOrder> getCarrierPromiseCount(String customerCode, String wareHouseCode, String carrierCode,
			String startTime, String endTime) {

		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(startTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return new ArrayList<ShippingOrder>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", customerCode);
		if (StringUtils.isNotBlank(wareHouseCode) && !wareHouseCode.equals("-1"))
			map.put("wareHouseCode", wareHouseCode);
		if (StringUtils.isNotBlank(carrierCode))
			map.put("carrierCode", carrierCode);
		map.put("startTime", startTime);
		try {
			map.put("endTime", DateUtil.getNextDate(endTime));
		} catch (ParseException e) {
			map.put("endTime", endTime);
		}
		return (List<ShippingOrder>) this.list(space + "SELECT_CARRIER_PROMISE", map, sqlMapClient);
	}

	public OrderData getOrderData(Set<String> customerCode, List<String> wareHouseCodes, String startTime, String endTime) {
		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(startTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", new ArrayList<String>(customerCode));
		map.put("warehouseCodes", wareHouseCodes);
		map.put("startTime", startTime);
		if (StringUtils.isNotBlank(endTime)) {
			try {
				map.put("endTime", DateUtil.getNextDate(endTime));
			} catch (ParseException e) {
				map.put("endTime", endTime);
			}
		}
		return (OrderData) this.object(space + "SELECT_ORDER_DATA", map, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<OrderData> getDayOrderData(Set<String> customerCodes, List<String> wareHouseCodes, String startTime,
			String endTime) {

		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(startTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return new ArrayList<OrderData>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCode", new ArrayList<String>(customerCodes));
		map.put("warehouseCodes", wareHouseCodes);
		map.put("startTime", startTime);
		if (StringUtils.isNotBlank(endTime)) {
			try {
				map.put("endTime", DateUtil.getNextDate(endTime));
			} catch (ParseException e) {
				map.put("endTime", endTime);
			}
		}
		return (List<OrderData>) this.list(space + "SELECT_ORDER_DATA_BY_DATE", map, sqlMapClient);
	}

	public Integer getShippingOrderDatTotalSize(Set<String> customerCodes, List<String> wareHouseCodes, String relationNo,
			String consigneeName, String consigneePhone, String status, String shippingOrderNo, String logisticsProviderName,
			String startTime, String endTime) {

		long betweenTime = 32;
		try {
			betweenTime = DateUtil.betweenDayTime(startTime, endTime);
		} catch (ParseException e) {
		}
		if (betweenTime >= 32)
			return 0;

		if (CollectionUtils.isEmpty(customerCodes) || CollectionUtils.isEmpty(wareHouseCodes))
			return 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerCodes", new ArrayList<String>(customerCodes));
		map.put("wareHouseCodes", wareHouseCodes);
		if (StringUtils.isNotBlank(startTime))
			map.put("startTime", startTime);
		if (StringUtils.isNotBlank(endTime)) {
			try {
				map.put("endTime", DateUtil.getNextDate(endTime));
			} catch (ParseException e) {
				map.put("endTime", endTime);
			}
		}
		if (StringUtils.isNotBlank(relationNo))
			map.put("relationNo", relationNo);
		if (StringUtils.isNotBlank(consigneeName)) {
			map.put("consigneeName", "%" + consigneeName + "%");
		}
		if (StringUtils.isNotBlank(consigneePhone))
			map.put("consigneePhone", "%" + consigneePhone + "%");
		if (StringUtils.isNotBlank(status) && !status.equals("-1")) {
			List<String> statuses = new ArrayList<String>();
			if (status.equals("PROVIDED")) {
				statuses.add("WMS_PRINTED");
				statuses.add("WMS_PICKUPED");
				statuses.add("WMS_CHECKED");
				statuses.add("WMS_PACKAGED");
				statuses.add("WMS_WEIGHTED");
			} else {
				statuses.add(status);
			}
			map.put("status", statuses);
		}
		if (StringUtils.isNotBlank(shippingOrderNo))
			map.put("shippingOrderNo", "%" + shippingOrderNo + "%");
		if (StringUtils.isNotBlank(logisticsProviderName) && !logisticsProviderName.equals("-1"))
			map.put("logisticsProviderName", logisticsProviderName);
		Integer res = (Integer) this.object(space + "SELECT_SHIPPING_ORDER_DATA_TOTALSIZE", map, sqlMapClient);
		if (res % pageSize == 0)
			return res / pageSize;
		return res / pageSize + 1;
	}

	@SuppressWarnings("unchecked")
	public List<IdoStatistic> getOrderDetailSkuData(Set<String> customerCodes, List<String> wareHouseCodes, String relationNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(customerCodes) || CollectionUtils.isEmpty(wareHouseCodes))
			return new ArrayList<IdoStatistic>();
		map.put("customerCodes", new ArrayList<String>(customerCodes));
		map.put("wareHouseCodes", wareHouseCodes);
		if (StringUtils.isNotBlank(relationNo))
			map.put("relationNo", relationNo);
		return (List<IdoStatistic>) this.list(space + "SELECT_ORDER_DETAIL_IDO", map, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<ShippingOrderData> getShippingOrderData(Set<String> customerCodes, List<String> wareHouseCodes,
			String relationNo, String consigneeName, String consigneePhone, String status, String shippingOrderNo,
			String logisticsProviderName, String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(customerCodes) || CollectionUtils.isEmpty(wareHouseCodes))
			return new ArrayList<ShippingOrderData>();
		map.put("customerCodes", new ArrayList<String>(customerCodes));
		map.put("wareHouseCodes", wareHouseCodes);
		if (StringUtils.isNotBlank(startTime))
			map.put("startTime", startTime);
		if (StringUtils.isNotBlank(endTime)) {
			try {
				map.put("endTime", DateUtil.getNextDate(endTime));
			} catch (ParseException e) {
				map.put("endTime", endTime);
			}
		}
		if (StringUtils.isNotBlank(relationNo))
			map.put("relationNo", relationNo);
		if (StringUtils.isNotBlank(consigneeName)) {
			map.put("consigneeName", "%" + consigneeName + "%");
		}
		if (StringUtils.isNotBlank(consigneePhone))
			map.put("consigneePhone", "%" + consigneePhone + "%");
		if (StringUtils.isNotBlank(status) && !status.equals("-1")) {
			List<String> statuses = new ArrayList<String>();
			if (status.equals("PROVIDED")) {
				statuses.add("WMS_PRINTED");
				statuses.add("WMS_PICKUPED");
				statuses.add("WMS_CHECKED");
				statuses.add("WMS_PACKAGED");
				statuses.add("WMS_WEIGHTED");
			} else {
				statuses.add(status);
			}
			map.put("status", statuses);
		}
		if (StringUtils.isNotBlank(shippingOrderNo))
			map.put("shippingOrderNo", "%" + shippingOrderNo + "%");
		if (StringUtils.isNotBlank(logisticsProviderName) && !logisticsProviderName.equals("-1"))
			map.put("logisticsProviderName", logisticsProviderName);
		return (List<ShippingOrderData>) this.list(space + "SELECT_SHIPPING_ORDER_DATA", map, sqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<SalesShipping> getSalesShippingOrderData(Set<Integer> salesShippingIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(salesShippingIds))
			return new ArrayList<SalesShipping>();
		map.put("shippingOrderIds", new ArrayList<Integer>(salesShippingIds));
		return (List<SalesShipping>) this.list(space + "SELECT_SALES_SHIPPING_ORDER_DATA", map, sqlMapClient);
	}

	public SalesOrder getSalesOrderDetail(Set<String> customerCodes, List<String> wareHouseCodes, String relationNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(customerCodes) || CollectionUtils.isEmpty(wareHouseCodes))
			return null;
		map.put("customerCode", new ArrayList<String>(customerCodes));
		map.put("warehouseCodes", wareHouseCodes);
		if (StringUtils.isNotBlank(relationNo))
			map.put("relationNo", relationNo);
		return (SalesOrder) this.object(space + "SELECT_SALES_ORDER_DATA", map, sqlMapClient);
	}

}
