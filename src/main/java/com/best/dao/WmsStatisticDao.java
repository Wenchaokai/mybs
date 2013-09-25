package com.best.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.best.domain.WMSWareHouse;
import com.best.domain.WmsStatistic;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:WmsStatisticDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-23
 */
@Repository("wmsStatisticDao")
public class WmsStatisticDao extends BaseDao {

	private static final String space = "wmsStatisticSpace.";

	@Resource(name = "wmsSqlMapClient")
	protected SqlMapClient sqlMapClient;

	@SuppressWarnings("unchecked")
	public List<WmsStatistic> getWmsStatistic(String skuCode, String customerCode, String wareHouseCode, String orgCode,
			List<String> searchSkuCodes, Map<String, String> mapValues, Integer currentPage) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(skuCode) && !"-1".equals(skuCode))
			map.put("SKUCODE", skuCode);
		map.put("CUSTOMERCODE", customerCode);
		if (StringUtils.isNotBlank(wareHouseCode) && !"-1".equals(wareHouseCode))
			map.put("WHCODE", wareHouseCode);
		if (StringUtils.isNotBlank(orgCode) && !"-1".equals(orgCode))
			map.put("ORGCODE", orgCode);

		if (CollectionUtils.isNotEmpty(searchSkuCodes))
			map.put("SKUCODES", searchSkuCodes);

		if (null != mapValues) {
			for (Entry<String, String> entry : mapValues.entrySet()) {
				if (entry.getKey().startsWith("UDF")) {

					map.put(entry.getKey(), entry.getValue());
				} else if (null != entry.getValue() && entry.getValue().length() > 0 && StringUtils.isNumeric(entry.getValue())) {
					map.put(entry.getKey(), Float.parseFloat(entry.getValue()));
				}
			}
		}

		int totalSize = (currentPage + 1) * pageSize;
		map.put("limit", totalSize);

		return (List<WmsStatistic>) this.list(space + "SELECT_WMS_ORDER_INFO", map, sqlMapClient);
	}

	public WMSWareHouse getWmsWareHouse(Integer wareHouseId) {
		return (WMSWareHouse) this.object(space + "GET_WMS_WAREHOUSE", wareHouseId, sqlMapClient);
	}

	public Integer getWmsStatisticSize(String currentSkuCode, String currentCustomerCode, String wareHouseCode, String orgCode,
			List<String> searchSkuCodes, Map<String, String> mapValues) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(currentSkuCode) && !"-1".equals(currentSkuCode))
			map.put("SKUCODE", currentSkuCode);
		map.put("CUSTOMERCODE", currentCustomerCode);
		if (StringUtils.isNotBlank(wareHouseCode) && !"-1".equals(wareHouseCode))
			map.put("WHCODE", wareHouseCode);
		if (StringUtils.isNotBlank(orgCode) && !"-1".equals(orgCode))
			map.put("ORGCODE", orgCode);
		if (CollectionUtils.isNotEmpty(searchSkuCodes))
			map.put("SKUCODES", searchSkuCodes);

		if (null != mapValues) {
			for (Entry<String, String> entry : mapValues.entrySet()) {
				if (entry.getKey().startsWith("UDF")) {

					map.put(entry.getKey(), entry.getValue());
				} else if (null != entry.getValue() && entry.getValue().length() > 0 && StringUtils.isNumeric(entry.getValue())) {
					map.put(entry.getKey(), Float.parseFloat(entry.getValue()));
				}
			}
		}

		Integer res = (Integer) this.object(space + "SELECT_WMS_ORDER_SIZE_INFO", map, sqlMapClient);

		if (res % pageSize == 0)
			return res / pageSize;
		return res / pageSize + 1;
	}

}
