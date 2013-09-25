package com.best.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.best.dao.WmsStatisticDao;
import com.best.domain.SKU;
import com.best.domain.WMSWareHouse;
import com.best.domain.WareHouse;
import com.best.domain.WmsStatistic;

/**
 * ClassName:WmsStatisticService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-23
 */
@Service("wmsStatisticService")
public class WmsStatisticService extends BaseService {

	@Autowired
	private WmsStatisticDao wmsStatisticDao;

	@Autowired
	private WareHouseService wareHouseService;

	@Autowired
	private SkuService skuService;

	@Value("${best.base.memcache.timeout}")
	private Integer memcachedTimeOut;

	private static final String SKU_WMS_STATISTIC_REPORT_PREFIX = "SKU_WMS_STATISTIC_ORDER_REPORT_PREFIX_";
	private static final String SKU_WMS_STATISTIC_REPORT_SIZE_PREFIX = "SKU_WMS_STATISTIC_ORDER_REPORT_PREFIX_";

	public List<WmsStatistic> getWmsStatistic(String userCount, String skuCode, String skuName, String customerCode,
			String wareHouseCode, String orgCode, Map<String, String> mapValues, Integer currentPage) {
		StringBuffer keyAppend = new StringBuffer(SKU_WMS_STATISTIC_REPORT_PREFIX);
		keyAppend.append(userCount).append("_").append(skuCode).append("_").append(skuName).append("_").append(customerCode)
				.append("_").append(wareHouseCode).append("_").append(orgCode);
		for (String value : mapValues.values()) {
			keyAppend.append("_").append(value);
		}
		String key = keyAppend.toString();

		List<WmsStatistic> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (res == null) {
			List<String> searchSkuCodes = null;
			if (!StringUtils.isBlank(skuName)) {
				searchSkuCodes = skuService.getSkusByName(skuName);
			}
			res = wmsStatisticDao.getWmsStatistic(skuCode, customerCode, wareHouseCode, orgCode, searchSkuCodes, mapValues,
					currentPage);
			try {
				memcachedClient.set(key, memcachedTimeOut, res);
			} catch (Exception e) {
			}
		}
		int start = currentPage * wmsStatisticDao.pageSize;
		int end = start + wmsStatisticDao.pageSize;
		return res.subList(start, end > res.size() ? res.size() : end);
	}

	public List<WmsStatistic> getWmsSkuIdoCount(String userCount, String currentWareHouseCode, String currentWareHouseName,
			String currentSku, String currentSkuName, String customerCode, String customerName, Map<String, String> mapValues,
			Integer currentPage, Model model) {

		List<WmsStatistic> res = null;
		if (StringUtils.isNotBlank(currentWareHouseCode) && !currentWareHouseCode.equals("-1")) {
			WareHouse wareHouse = wareHouseService.getWareHouse(currentWareHouseCode);
			res = getWmsStatistic(userCount, currentSku, currentSkuName, customerCode, wareHouse.getWmsWareHouseCode(),
					wareHouse.getWmsOrgCode(), mapValues, currentPage);
			Set<String> skuCodes = new HashSet<String>();
			for (WmsStatistic wmsStatistic : res) {
				skuCodes.add(wmsStatistic.getSkuCode());
			}

			Map<String, SKU> skus = getSkus(skuCodes);
			// 设置名字
			for (WmsStatistic wmsStatistic : res) {
				wmsStatistic.setWareHouseCode(wareHouse.getWareHouseCode());
				wmsStatistic.setWareHouseName(wareHouse.getWareHouseName());
				wmsStatistic.setCustomerCode(customerCode);
				wmsStatistic.setCustomerName(customerName);
				SKU sku = skus.get(wmsStatistic.getSkuCode());
				if (sku != null)
					wmsStatistic.setSkuName(sku.getSkuName());
				else
					wmsStatistic.setSkuName("");
			}
		} else {
			res = getWmsStatistic(userCount, currentSku, currentSkuName, customerCode, "-1", "-1", mapValues, currentPage);
			Set<String> wareHouseIds = new HashSet<String>();
			Set<String> skuCodes = new HashSet<String>();
			for (WmsStatistic wmsStatistic : res) {
				wareHouseIds.add(wmsStatistic.getWareHouseId());
				skuCodes.add(wmsStatistic.getSkuCode());
			}
			// 获取WareHouseName
			Map<String, WareHouse> wareHouses = getWareHouses(wareHouseIds);
			Map<String, SKU> skus = getSkus(skuCodes);
			// 设置名字
			for (WmsStatistic wmsStatistic : res) {
				wmsStatistic.setCustomerCode(customerCode);
				wmsStatistic.setCustomerName(customerName);
				WareHouse wareHouse = wareHouses.get(wmsStatistic.getWareHouseId());
				if (wareHouse != null) {
					wmsStatistic.setWareHouseCode(wareHouse.getWareHouseCode());
					wmsStatistic.setWareHouseName(wareHouse.getWareHouseName());
				} else
					wmsStatistic.setWareHouseName("");
				wmsStatistic.setCustomerCode(customerCode);
				wmsStatistic.setCustomerName(customerName);
				SKU sku = skus.get(wmsStatistic.getSkuCode());
				if (sku != null)
					wmsStatistic.setSkuName(sku.getSkuName());
				else
					wmsStatistic.setSkuName("");
			}
		}

		return res;

	}

	/**
	 * 
	 * @param skuCodes
	 * @return
	 */
	private Map<String, SKU> getSkus(Set<String> skuCodes) {
		Map<String, SKU> skuCodeMap = new HashMap<String, SKU>();
		for (String skuCode : skuCodes) {
			SKU sku = skuService.getSku(skuCode);
			if (null != sku)
				skuCodeMap.put(skuCode, sku);
		}
		return skuCodeMap;
	}

	private Map<String, WareHouse> getWareHouses(Set<String> wareHouseIds) {
		Map<String, WareHouse> map = new HashMap<String, WareHouse>();
		for (String wareHouseId : wareHouseIds) {
			WMSWareHouse wmsWareHouse = wmsStatisticDao.getWmsWareHouse(Integer.parseInt(wareHouseId));
			WareHouse wareHouse = wareHouseService.getWareHouse(wmsWareHouse.getWmsWareHouseCode());
			map.put(wareHouseId, wareHouse);
		}
		return map;
	}

	public Integer getWmsSkuIdoTotalSize(String userCount, String currentWareHouseCode, String currentSkuCode,
			String currentSkuName, String currentCustomerCode, Map<String, String> propertiesValuesMap) {
		String wareHouseCode = "-1";
		String orgCode = "-1";
		if (StringUtils.isNotBlank(currentWareHouseCode) && !currentWareHouseCode.equals("-1")) {
			WareHouse wareHouse = wareHouseService.getWareHouse(currentWareHouseCode);
			wareHouseCode = wareHouse.getWmsWareHouseCode();
			orgCode = wareHouse.getWmsOrgCode();
		}
		StringBuffer keyAppend = new StringBuffer(SKU_WMS_STATISTIC_REPORT_SIZE_PREFIX);
		keyAppend.append(userCount).append("_").append(currentSkuCode).append("_").append(currentSkuName).append("_")
				.append(currentCustomerCode).append("_").append(wareHouseCode).append("_").append(orgCode);
		for (Object value : propertiesValuesMap.values()) {
			keyAppend.append("_").append(value);
		}
		String key = keyAppend.toString();

		Integer res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (res == null) {
			List<String> searchSkuCodes = null;
			if (!StringUtils.isBlank(currentSkuName)) {
				searchSkuCodes = skuService.getSkusByName(currentSkuName);
			}
			res = wmsStatisticDao.getWmsStatisticSize(currentSkuCode, currentCustomerCode, wareHouseCode, orgCode,
					searchSkuCodes, propertiesValuesMap);
			try {
				memcachedClient.set(key, memcachedTimeOut, res);
			} catch (Exception e) {
			}
		}
		return res;
	}
}
