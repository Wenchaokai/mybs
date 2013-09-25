package com.best.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.best.dao.WareHouseDao;
import com.best.domain.WareHouse;

/**
 * ClassName:CustomerService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-6
 */
@Service("wareHouseService")
public class WareHouseService extends BaseService {
	@Autowired
	private WareHouseDao wareHouseDao;

	public static final String ALL_WAREHOUSE_KEY = "ALL_WAREHOUSE_KEY";

	private static final String WAREHOUSE_MEMCACHED_KEY = "WAREHOUSE_MEMCACHED_KEY_";

	private static final String WAREHOUSE_ID_MEMCACHED_KEY = "WAREHOUSE_ID_MEMCACHED_KEY_";

	public List<WareHouse> getAllWareHouse() {
		List<WareHouse> res = null;
		try {
			res = memcachedClient.get(ALL_WAREHOUSE_KEY);
		} catch (Exception e) {
		}
		if (null != res)
			return res;
		res = wareHouseDao.getAllWareHouse();
		try {
			memcachedClient.set(ALL_WAREHOUSE_KEY, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;
	}

	public WareHouse getWareHouse(Integer wareHouseId) {
		Object obj = null;
		try {
			obj = memcachedClient.get(WAREHOUSE_ID_MEMCACHED_KEY + wareHouseId);
			if (obj != null)
				return (WareHouse) obj;
		} catch (Exception e1) {
		}

		WareHouse res = wareHouseDao.getWareHouse(wareHouseId);
		try {
			memcachedClient.set(WAREHOUSE_ID_MEMCACHED_KEY + wareHouseId, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;
	}

	public WareHouse getWareHouse(String CODE) {
		Object obj = null;
		try {
			obj = memcachedClient.get(WAREHOUSE_MEMCACHED_KEY + CODE);
			if (obj != null)
				return (WareHouse) obj;
		} catch (Exception e1) {
		}

		WareHouse res = wareHouseDao.getWareHouse(CODE);
		try {
			memcachedClient.set(WAREHOUSE_MEMCACHED_KEY + CODE, 30 * 60 * 60, res, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return res;
	}
}
