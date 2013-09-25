package com.best.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.best.dao.CustomerDao;
import com.best.domain.Customer;

/**
 * ClassName:CustomerService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-6
 */
@Service("customerService")
public class CustomerService extends BaseService {
	@Autowired
	private CustomerDao customerDao;

	public static final String ALL_CUSTOMER_KEY = "ALL_CUSTOMER_KEY";

	private static final String CUSTOMER_ID_KEY = "CUSTOMER_ID_KEY_";

	public List<Customer> getAllCustomer() {
		List<Customer> res = null;
		try {
			res = memcachedClient.get(ALL_CUSTOMER_KEY);
		} catch (Exception e) {
		}
		if (null != res)
			return res;
		res = customerDao.getAllCustomer();
		try {
			memcachedClient.set(ALL_CUSTOMER_KEY, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;
	}

	public Customer getCustomer(Integer customerId) {
		Object obj = null;
		try {
			obj = memcachedClient.get(CUSTOMER_ID_KEY + customerId);
			if (obj != null)
				return (Customer) obj;
		} catch (Exception e1) {
		}

		Customer res = customerDao.getCustomer(customerId);
		try {
			memcachedClient.set(CUSTOMER_ID_KEY + customerId, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;
	}
}
