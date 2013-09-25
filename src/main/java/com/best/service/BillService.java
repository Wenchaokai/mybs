package com.best.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.best.dao.BillDao;
import com.best.domain.BillInfo;
import com.best.utils.CommonUtils;

/**
 * ClassName:BillService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-29
 */
@Service("billService")
public class BillService extends BaseService {
	@Autowired
	private BillDao billDao;

	@Value("${best.base.dir}")
	public String baseDir;

	private static final String BILL_MEMCACHE_KEY = "BILL_MEMCACHE_KEY_";
	private static final String BILL_MEMCACHE_SIZE_KEY = "BILL_MEMCACHE_KEY_";
	private static final String BILL_MEMCACHE_ID_KEY = "BILL_MEMCACHE_ID_KEY_";

	public void insertBill(BillInfo bill) {
		billDao.insertBill(bill);
	}

	public BillInfo getBill(Long billId) {
		BillInfo res = null;
		try {
			res = memcachedClient.get(BILL_MEMCACHE_ID_KEY + billId);
		} catch (Exception e) {
		}
		if (null == res) {
			res = (BillInfo) billDao.getBill(billId);
			if (null != res) {
				try {
					memcachedClient.set(BILL_MEMCACHE_ID_KEY + billId, 30 * 60 * 60, res, 30 * 60 * 60);
				} catch (Exception e) {
				}
			}
		}
		return res;
	}

	public void updateBill(BillInfo billInfo) {
		billDao.updateBill(billInfo);
		try {
			memcachedClient.delete(BILL_MEMCACHE_ID_KEY + billInfo.getBillId());
		} catch (Exception e) {
		}
	}

	public void updateBillStatus(BillInfo billInfo) {
		billDao.updateBillStatus(billInfo.getBillId(), billInfo.getBillStatus());
		try {
			memcachedClient.delete(BILL_MEMCACHE_ID_KEY + billInfo.getBillId());
		} catch (Exception e) {
		}
	}

	public int getTotalSize(String billCustomerCode, String billWareHouseCode, Integer billType, String startTime, String endTime) {
		String key = BILL_MEMCACHE_SIZE_KEY + billCustomerCode + "_" + billWareHouseCode + "_" + billType + "_" + startTime + "_"
				+ endTime;
		Integer res = null;
		res = billDao.findBillSize(billCustomerCode, billWareHouseCode, billType, startTime, endTime);
		return res;
	}

	public int getTotalSize(List<String> billCustomerCodes, List<String> billWareHouseCodes, Integer billType, String startTime,
			String endTime) {
		String key = BILL_MEMCACHE_SIZE_KEY + CommonUtils.caculateHashCode(new ArrayList<Object>(billCustomerCodes)) + "_"
				+ CommonUtils.caculateHashCode(new ArrayList<Object>(billWareHouseCodes)) + "_" + billType + "_" + startTime
				+ "_" + endTime;
		Integer res = billDao.findBillSize(billCustomerCodes, billWareHouseCodes, billType, startTime, endTime);
		return res;
	}

	public List<BillInfo> fillBills(String billCustomerCode, String billWareHouseCode, Integer billType, String startTime,
			String endTime, Integer startIndex) {
		List<BillInfo> res = null;
		res = billDao.findBillByPageSize(billCustomerCode, billWareHouseCode, billType, startTime, endTime, startIndex);
		if (res == null) {
			return new ArrayList<BillInfo>();
		}
		return res;

	}

	public List<BillInfo> fillBills(List<String> billCustomerCodes, List<String> billWareHouseCodes, Integer billType,
			String startTime, String endTime, Integer startIndex) {
		List<BillInfo> res = null;
		res = billDao.findBillByPageSize(billCustomerCodes, billWareHouseCodes, billType, startTime, endTime, startIndex);
		if (res == null) {
			return new ArrayList<BillInfo>();
		}
		return res;

	}
}
