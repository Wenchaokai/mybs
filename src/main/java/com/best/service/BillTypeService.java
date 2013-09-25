package com.best.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.best.dao.BillTypeDao;
import com.best.domain.BillType;

/**
 * ClassName:BillService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-29
 */
@Service("billTypeService")
public class BillTypeService extends BaseService {
	@Autowired
	private BillTypeDao billTypeDao;

	@Value("${best.base.memcache.timeout}")
	private Integer memcachedTimeOut;

	private static final String BILL_TYPE_MEMCACHE_KEY = "BILL_TYPE_MEMCACHE_KEY";

	public List<BillType> fillBills() {
		List<BillType> res = null;
		try {
			res = memcachedClient.get(BILL_TYPE_MEMCACHE_KEY);
		} catch (Exception e) {
		}
		if (null != res)
			return res;
		res = billTypeDao.findBillByPageSize();

		if (res == null) {
			return new ArrayList<BillType>();
		}
		try {
			memcachedClient.set(BILL_TYPE_MEMCACHE_KEY, memcachedTimeOut, res);
		} catch (Exception e) {
		}
		return res;

	}
}
