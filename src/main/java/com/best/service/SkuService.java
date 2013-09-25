package com.best.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.best.dao.SkuDao;
import com.best.domain.SKU;

/**
 * ClassName:SkuService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-7
 */
@Service("skuService")
public class SkuService extends BaseService {

	@Autowired
	private SkuDao skuDao;

	private static final String SKU_MEMCACHED_KEY = "SKU_MEMCACHED_KEY_";

	private static final String SKU_MEMCACHED_NAME_KEY = "SKU_MEMCACHED_NAME_KEY_";

	public SKU getSku(String skuCode) {
		Object obj = null;
		try {
			obj = memcachedClient.get(SKU_MEMCACHED_KEY + skuCode);
			if (obj != null)
				return (SKU) obj;
		} catch (Exception e1) {
		}

		SKU sku = skuDao.getSku(skuCode);
		try {
			memcachedClient.set(SKU_MEMCACHED_KEY + skuCode, 30 * 60 * 60, sku, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return sku;
	}

	@SuppressWarnings("unchecked")
	public List<String> getSkusByName(String skuName) {
		Object obj = null;
		try {
			obj = memcachedClient.get(SKU_MEMCACHED_NAME_KEY + skuName);
			if (obj != null)
				return (List<String>) obj;
		} catch (Exception e1) {
		}

		List<String> skus = skuDao.getSkusByName(skuName);
		try {
			memcachedClient.set(SKU_MEMCACHED_NAME_KEY + skuName, 30 * 60 * 60, skus, 30 * 60 * 60);
		} catch (Exception e) {
		}
		return skus;
	}

}
