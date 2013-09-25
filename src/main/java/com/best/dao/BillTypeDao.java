package com.best.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.best.domain.BillType;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:BillDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-29
 */
@Repository("billTypeDao")
public class BillTypeDao extends BaseDao {

	@Resource(name = "sqlMapClient")
	protected SqlMapClient sqlMapClient;

	private static final String space = "billTypeSpace.";

	@SuppressWarnings("unchecked")
	public List<BillType> findBillByPageSize() {

		Map<String, Object> map = new HashMap<String, Object>();

		return (List<BillType>) this.list(space + "GET_ALL_BILL_TYPE_INFO", map, sqlMapClient);
	}

}
