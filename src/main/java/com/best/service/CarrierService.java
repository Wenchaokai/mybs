package com.best.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.best.dao.CarrierDao;
import com.best.domain.Carrier;

/**
 * ClassName:CarrierService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-8
 */
@Service("carrierService")
public class CarrierService extends BaseService {

	@Autowired
	private CarrierDao carrierDao;

	@Value("${best.base.memcache.timeout}")
	private Integer memcachedTimeOut;

	private static final String CARRIER_MEMCACHE_KEY = "CARRIER_MEMCACHE_KEY";

	public List<Carrier> fillCarriers() {
		List<Carrier> res = null;
		try {
			res = memcachedClient.get(CARRIER_MEMCACHE_KEY);
		} catch (Exception e) {
		}
		if (null != res)
			return res;
		res = carrierDao.findBillByPageSize();

		if (res == null) {
			return new ArrayList<Carrier>();
		}
		try {
			memcachedClient.set(CARRIER_MEMCACHE_KEY, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;

	}
}
