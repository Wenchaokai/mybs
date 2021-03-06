package com.best.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.best.domain.Customer;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ClassName:ECBOSSCustomerDao Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-6
 */
@Repository("customerDao")
public class CustomerDao extends BaseDao {

	private static final String space = "customerSpace.";

	@Resource(name = "ecbossSqlMapClient")
	protected SqlMapClient ecbossSqlMapClient;

	@SuppressWarnings("unchecked")
	public List<Customer> getAllCustomer() {
		Map<String, Object> map = new HashMap<String, Object>();

		String selectString = "GET_ALL_CUSTOMER";

		List<Customer> res = new ArrayList<Customer>();
		int pageSize = 500;
		int start = 0;
		while (true) {
			map.put("start", start);
			map.put("end", start + pageSize);
			List<Customer> obj = (List<Customer>) this.list(space + selectString, map, ecbossSqlMapClient);
			if (obj == null || obj.size() == 0) {
				break;
			}
			res.addAll(obj);
			if (obj.size() < pageSize)
				break;
			start += pageSize;
		}

		return res;
	}

	public Customer getCustomer(Integer customerId) {
		String selectString = "GET_CUSTOMER_ID";
		Customer obj = (Customer) this.object(space + selectString, customerId, ecbossSqlMapClient);
		return obj;
	}

}
