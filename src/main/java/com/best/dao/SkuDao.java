package com.best.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.best.domain.SKU;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:SkuDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-7
 */
@Repository("skuDao")
public class SkuDao extends BaseDao {

	private static final String space = "skuSpace.";

	@Resource(name = "ecbossSqlMapClient")
	protected SqlMapClient ecbossSqlMapClient;

	public SKU getSku(String skuCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SKUCODE", skuCode);
		return (SKU) this.object(space + "GET_SKU", map, ecbossSqlMapClient);
	}

	@SuppressWarnings("unchecked")
	public List<String> getSkusByName(String skuName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SKUNAME", "%" + skuName + "%");
		return (List<String>) this.list(space + "GET_SKU_BY_NAME", map, ecbossSqlMapClient);
	}
}
