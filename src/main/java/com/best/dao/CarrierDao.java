package com.best.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.best.domain.Carrier;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:CarrierDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-8
 */
@Repository("carrierDao")
public class CarrierDao extends BaseDao {

	@Resource(name = "sqlMapClient")
	protected SqlMapClient sqlMapClient;

	private static final String space = "carrierSpace.";

	@SuppressWarnings("unchecked")
	public List<Carrier> findBillByPageSize() {

		Map<String, Object> map = new HashMap<String, Object>();

		return (List<Carrier>) this.list(space + "GET_ALL_CARRIER_INFO", map, sqlMapClient);
	}

}
