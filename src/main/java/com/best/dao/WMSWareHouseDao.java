package com.best.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.best.domain.WMSWareHouse;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:WMSWareHouse Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-10
 */
@Repository("wmsWareHouseDao")
public class WMSWareHouseDao extends BaseDao {

	private static final String space = "wmsStatisticSpace.";

	@Resource(name = "wmsSqlMapClient")
	protected SqlMapClient sqlMapClient;

	public WMSWareHouse getWmsWareHouse(Integer wareHouseId) {
		return (WMSWareHouse) this.object(space + "GET_WMS_WAREHOUSE", wareHouseId, sqlMapClient);
	}

}
