package com.best.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.best.dao.StatisticDao;
import com.best.domain.IdoStatistic;
import com.best.domain.OrderData;
import com.best.domain.SalesOrder;
import com.best.domain.SalesShipping;
import com.best.domain.ShippingOrder;
import com.best.domain.ShippingOrderData;
import com.best.domain.WareHouse;
import com.best.utils.CommonUtils;
import com.best.utils.DateUtil;

/**
 * ClassName:StatisticService Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-11
 */
@Service("statisticService")
public class StatisticService extends BaseService {

	@Autowired
	private StatisticDao statisticDao;

	@Value("${best.base.dir}")
	private String baseDir;

	@Value("${best.base.pic.show.size}")
	private Integer showSize;

	public String getDistributedSkuIdoCount(String skuCode, String customerCode, String wareHouseCode, String startTime,
			String endTime, String key, Model model) {

		File file = new File(baseDir, skuCode);
		if (!file.exists())
			file.mkdir();
		File showFile = new File(file, key + ".html");
		List<IdoStatistic> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}

		if (res == null) {
			res = statisticDao.getDistributedSkuIdoCount(skuCode, customerCode, wareHouseCode, startTime, endTime);

			try {
				memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
			} catch (Exception e) {
			}
		}

		Map<String, Integer> provinceMap = new HashMap<String, Integer>();
		Map<String, Map<String, Integer>> dateMap = new HashMap<String, Map<String, Integer>>();
		Integer totalCount = 0;
		for (IdoStatistic idoStatistic : res) {
			// WareHouseCode 作为Key
			Integer numCount = provinceMap.get(idoStatistic.getProvince());
			if (numCount == null) {
				numCount = 0;
			}
			numCount += idoStatistic.getNumCount();
			provinceMap.put(idoStatistic.getProvince(), numCount);
			totalCount += idoStatistic.getNumCount();

			// DateTime作为Key
			Map<String, Integer> dateData = dateMap.get(idoStatistic.getDateTime());
			if (null == dateData) {
				dateData = new HashMap<String, Integer>();
				dateMap.put(idoStatistic.getDateTime(), dateData);
			}
			dateData.put(idoStatistic.getProvince(), idoStatistic.getNumCount());
		}

		try {
			// 生成页面展示文件
			String content = CommonUtils.createIdoDisSkuJSON(startTime, endTime, provinceMap);
			if (null != content) {
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(showFile);
				} catch (FileNotFoundException e1) {
				}
				try {
					IOUtils.write(content, outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(outputStream);
				}
			}

			model.addAttribute("mapFile", showFile.getName());
			model.addAttribute("totalCount", totalCount);

			// 生成excel文件
			CommonUtils.createIdoDisSkuExcelFile(startTime, endTime, dateMap, baseDir, key);
			model.addAttribute("currentExcelFile", "/json/monitor/dingdang/" + key + ".xls");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// public String getPercentSkuIdoCount(String skuCode, String customerCode,
	// String startTime, String endTime,
	// String excelFileName, Model model) {
	// StringBuffer keyAppend = new
	// StringBuffer(SKU_PER_STATISTIC_REPORT_PREFIX);
	// keyAppend.append(skuCode).append("_").append(customerCode).append("_").append(startTime).append("_").append(endTime);
	//
	// String key = keyAppend.toString();
	//
	// File file = new File(baseDir, skuCode);
	// if (!file.exists())
	// file.mkdir();
	// File showFile = new File(file, key);
	// List<IdoStatistic> res = null;
	// try {
	// res = memcachedClient.get(key);
	// } catch (Exception e) {
	// }
	//
	// if (res == null) {
	// res = statisticDao.getPercentSkuIdoCount(skuCode, customerCode,
	// startTime, endTime);
	//
	// try {
	// memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
	// } catch (Exception e) {
	// }
	// }
	//
	// Map<String, Integer> skuCountMap = new HashMap<String, Integer>();
	// Map<String, Map<String, Integer>> dateMap = new HashMap<String,
	// Map<String, Integer>>();
	// Integer totalCount = 0;
	// for (IdoStatistic idoStatistic : res) {
	// Integer numCount = skuCountMap.get(idoStatistic.getSkuCode());
	// if (numCount == null) {
	// numCount = 0;
	// }
	// numCount += idoStatistic.getNumCount();
	// skuCountMap.put(idoStatistic.getSkuCode(), numCount);
	// totalCount += idoStatistic.getNumCount();
	//
	// // DateTime作为Key
	// Map<String, Integer> dateData = dateMap.get(idoStatistic.getDateTime());
	// if (null == dateData) {
	// dateData = new HashMap<String, Integer>();
	// dateMap.put(idoStatistic.getDateTime(), dateData);
	// }
	// dateData.put(idoStatistic.getSkuCode(), idoStatistic.getNumCount());
	// }
	//
	// Map<Double, String> skuPercentMap = new TreeMap<Double,
	// String>(Collections.reverseOrder());
	// for (Entry<String, Integer> entry : skuCountMap.entrySet()) {
	// Integer value = entry.getValue();
	// skuPercentMap.put((value + 0.0) / totalCount * 100, entry.getKey());
	// }
	//
	// Map<String, Map<String, Double>> percentMap = new HashMap<String,
	// Map<String, Double>>();
	// for (Map.Entry<String, Map<String, Integer>> entry : dateMap.entrySet())
	// {
	// String dateTime = entry.getKey();
	// Map<String, Integer> value = entry.getValue();
	// Integer tempTotalCount = 0;
	// for (Entry<String, Integer> tempEntry : value.entrySet()) {
	// tempTotalCount += tempEntry.getValue();
	// }
	// Map<String, Double> dateSkuPercentMap = percentMap.get(dateTime);
	// if (dateSkuPercentMap == null) {
	// dateSkuPercentMap = new HashMap<String, Double>();
	// percentMap.put(dateTime, dateSkuPercentMap);
	// }
	// for (Entry<String, Integer> tempEntry : value.entrySet()) {
	// dateSkuPercentMap.put(tempEntry.getKey(), (tempEntry.getValue() + 0.0) /
	// tempTotalCount * 100);
	// }
	// }
	//
	// try {
	// // 生成页面展示文件
	// String content = CommonUtils.createIdoSkuPercentJSON(startTime, endTime,
	// percentMap, skuPercentMap, "占比", showSize,
	// color);
	// if (null != content) {
	// FileOutputStream outputStream = null;
	// try {
	// outputStream = new FileOutputStream(showFile);
	// } catch (FileNotFoundException e1) {
	// }
	// try {
	// IOUtils.write(content, outputStream);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// IOUtils.closeQuietly(outputStream);
	// }
	// }
	// model.addAttribute("currentFilePath", "/json/" + skuCode + "/" +
	// showFile.getName());
	// model.addAttribute("totalCount", totalCount);
	//
	// // 生成excel文件
	// CommonUtils.createIdoSkuPerExcelFile(startTime, endTime, percentMap,
	// skuPercentMap, baseDir, excelFileName);
	//
	// model.addAttribute("currentExcelFile", "/json/monitor/dingdang/" +
	// excelFileName + ".xls");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	public void getCarrierDistributedCount(String customerCode, String wareHouseCode, String startTime, String endTime,
			String key, Model model) {
		File file = new File(baseDir, customerCode);
		if (!file.exists())
			file.mkdir();
		File showFile = new File(file, key);
		List<ShippingOrder> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}

		if (res == null) {
			res = statisticDao.getCarrierDistributedCount(customerCode, wareHouseCode, startTime, endTime);

			try {
				memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
			} catch (Exception e) {
			}
		}

		Map<String, Map<String, Integer>> dataMap = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Integer>> dateMap = new HashMap<String, Map<String, Integer>>();
		for (ShippingOrder shippingOrder : res) {
			Map<String, Integer> date = dateMap.get(shippingOrder.getDateTime());
			if (null == date) {
				date = new HashMap<String, Integer>();
				dateMap.put(shippingOrder.getDateTime(), date);
			}
			date.put(shippingOrder.getShippingOrderName(), shippingOrder.getNumCount());

			Map<String, Integer> data = dataMap.get(shippingOrder.getShippingOrderName());
			if (null == data) {
				data = new HashMap<String, Integer>();
				dataMap.put(shippingOrder.getShippingOrderName(), data);
			}
			data.put(shippingOrder.getDateTime(), shippingOrder.getNumCount());
		}

		try {
			// 生成页面展示文件
			String content = CommonUtils.createCarrierDistributedJSON(startTime, endTime, dataMap, showSize);
			if (null != content) {
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(showFile);
				} catch (FileNotFoundException e1) {
				}
				try {
					IOUtils.write(content, outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(outputStream);
				}
			}
			model.addAttribute("currentFilePath", "/json/" + customerCode + "/" + showFile.getName());

			// 生成excel文件
			CommonUtils.createCarrierDistributedExcelFile(startTime, endTime, dataMap, dateMap, baseDir, key);

			model.addAttribute("currentExcelFile", "/json/monitor/dingdang/" + key + ".xls");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public OrderData getOrderData(Set<String> customerCode, List<String> wareHouseCodes, String startTime, String endTime) {
		// String fileName = "ORDER_DATA_" + startTime + "_" + endTime;
		// File file = new File(baseDir, userCount);
		// if (!file.exists())
		// file.mkdir();
		// File showFile = new File(file, fileName);
		// if (showFile.exists())
		// showFile.delete();
		String key = "ORDER_DATA";
		if (null != customerCode) {
			for (String string : customerCode) {
				key += "_c_" + string;
			}
		}
		if (null != wareHouseCodes) {
			for (String string : wareHouseCodes) {
				key += "_w_" + string;
			}
		}
		if (null != startTime)
			key += "_" + startTime;
		if (null != endTime)
			key += "_" + endTime;

		Object res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}

		if (null == res) {

			res = statisticDao.getOrderData(customerCode, wareHouseCodes, startTime, endTime);
			if (StringUtils.isBlank(((OrderData) res).getMonth()))
				((OrderData) res).setMonth(startTime.substring(0, 4) + "-" + startTime.substring(4, 6));

			try {
				memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
			} catch (Exception e) {
			}
		}
		// try {
		// // 生成页面展示文件
		// String content = CommonUtils.createOrderDataJSON(startTime, endTime,
		// orderData, showSize);
		// if (null != content) {
		// FileOutputStream outputStream = null;
		// try {
		// outputStream = new FileOutputStream(showFile);
		// } catch (FileNotFoundException e1) {
		// }
		// try {
		// IOUtils.write(content, outputStream);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// IOUtils.closeQuietly(outputStream);
		// }
		// }
		// model.addAttribute(keyName, "/json/" + customerCode + "/" +
		// showFile.getName());
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return (OrderData) res;
	}

	public void getCarrierPromiseCount(String customerCode, String wareHouseCode, String carrierCode, String startTime,
			String endTime, String key, Model model) {
		File file = new File(baseDir, customerCode);
		if (!file.exists())
			file.mkdir();
		File showFile = new File(file, key);
		List<ShippingOrder> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}

		if (res == null) {
			res = statisticDao.getCarrierPromiseCount(customerCode, wareHouseCode, carrierCode, startTime, endTime);
			try {
				memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
			} catch (Exception e) {
			}
		}

		Map<String, Map<String, Integer>> dataMap = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Integer>> dateMap = new HashMap<String, Map<String, Integer>>();
		for (ShippingOrder shippingOrder : res) {
			Map<String, Integer> date = dateMap.get(shippingOrder.getDateTime());
			if (null == date) {
				date = new HashMap<String, Integer>();
				dateMap.put(shippingOrder.getDateTime(), date);
			}
			if (shippingOrder.getDiffTime().equals("1") || shippingOrder.getDiffTime().equals("2")
					|| shippingOrder.getDiffTime().equals("3"))
				date.put(shippingOrder.getDiffTime(), shippingOrder.getNumCount());

			if (shippingOrder.getDiffTime().equals("1") || shippingOrder.getDiffTime().equals("2")
					|| shippingOrder.getDiffTime().equals("3")) {
				Map<String, Integer> data = dataMap.get(shippingOrder.getDiffTime());
				if (null == data) {
					data = new HashMap<String, Integer>();
					dataMap.put(shippingOrder.getDiffTime(), data);
				}
				data.put(shippingOrder.getDateTime(), shippingOrder.getNumCount());
			}
		}

		try {
			// 生成页面展示文件
			String content = CommonUtils.createCarrierPromiseJSON(startTime, endTime, dataMap, showSize);
			if (null != content) {
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(showFile);
				} catch (FileNotFoundException e1) {
				}
				try {
					IOUtils.write(content, outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(outputStream);
				}
			}
			model.addAttribute("currentFilePath", "/json/" + customerCode + "/" + showFile.getName());

			// 生成excel文件
			CommonUtils.createCarrierPromiseExcelFile(startTime, endTime, dataMap, dateMap, baseDir, key);

			model.addAttribute("currentExcelFile", "/json/monitor/dingdang/" + key + ".xls");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createOrderDataJson(String userCount, OrderData currentDateOrder, OrderData orderData, OrderData preOrderData,
			String modelName, Map<String, String> model) {
		File file = new File(baseDir, userCount);
		if (!file.exists())
			file.mkdir();
		File showFile = new File(file, modelName);
		if (showFile.exists())
			showFile.delete();

		String content = CommonUtils.createOrderDataJson(currentDateOrder, orderData, preOrderData);
		if (null != content) {
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(showFile);
			} catch (FileNotFoundException e1) {
			}
			try {
				IOUtils.write(content, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(outputStream);
			}
		}
		model.put(orderData.getMonth(), "/json/" + userCount + "/" + showFile.getName());

	}

	public List<OrderData> getDayOrderData(String userCount, Set<String> customerCodes, List<String> wareHouseCodes,
			String startTime, String endTime, String key, Model model) throws ParseException {
		File file = new File(baseDir, userCount);
		if (!file.exists())
			file.mkdir();
		File showFile = new File(file, key);
		if (showFile.exists())
			showFile.delete();
		List<OrderData> orderDatas = statisticDao.getDayOrderData(customerCodes, wareHouseCodes, startTime, endTime);

		String content = CommonUtils.createDayOrderDataJson(orderDatas, startTime, DateUtil.getCurrentDateString());
		if (null != content) {
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(showFile);
			} catch (FileNotFoundException e1) {
			}
			try {
				IOUtils.write(content, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(outputStream);
			}
		}
		model.addAttribute(key, "/json/" + userCount + "/" + showFile.getName());
		return orderDatas;
	}

	private static final String SHIPPING_ORDER_DATA_KEY_PREFIX = "SHIPPING_ORDER_DATA_";
	private static final String SHIPPING_ORDER_DATA_DETAIL_KEY_PREFIX = "SHIPPING_ORDER_DETAIL_DATA_";
	private static final String SALES_ORDER_DATA_DETAIL_KEY_PREFIX = "SALES_ORDER_DETAIL_DATA_";
	private static final String SHIPPING_ORDER_DATA_KEY_TOTALSIZE_PREFIX = "SHIPPING_ORDER_DATA_SIZE_";

	public List<ShippingOrderData> getShippingOrderData(String userCount, Set<String> customerCodes, List<WareHouse> wareHouses,
			String relationNo, String consigneeName, String consigneePhone, String status, String shippingOrderNo,
			String logisticsProviderName, String startTime, String endTime, Integer currentPage, Model model) {
		Map<String, WareHouse> wareHouseMap = new HashMap<String, WareHouse>();
		for (WareHouse wareHouse : wareHouses) {
			wareHouseMap.put(wareHouse.getWareHouseCode(), wareHouse);
		}
		String key = SHIPPING_ORDER_DATA_KEY_PREFIX + CommonUtils.caculateHashCode(new ArrayList<Object>(customerCodes)) + "_"
				+ CommonUtils.caculateHashCode(new ArrayList<Object>(wareHouseMap.keySet())) + "_"
				+ (relationNo == null ? "" : relationNo) + "_" + (consigneeName == null ? "" : consigneeName) + "_"
				+ (consigneePhone == null ? "" : consigneePhone) + "_" + (status == null ? "" : status) + "_"
				+ (shippingOrderNo == null ? "" : shippingOrderNo) + "_"
				+ (logisticsProviderName == null ? "" : logisticsProviderName) + "_" + (startTime == null ? "" : startTime) + "_"
				+ (endTime == null ? "" : endTime);
		List<ShippingOrderData> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (null == res) {
			res = statisticDao.getShippingOrderData(customerCodes, new ArrayList<String>(wareHouseMap.keySet()), relationNo,
					consigneeName, consigneePhone, status, shippingOrderNo, logisticsProviderName, startTime, endTime);
			if (null != res) {
				try {
					memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
				} catch (Exception e) {
				}
			}
		}
		for (ShippingOrderData shippingOrderData : res) {
			shippingOrderData.setStatusName(CommonUtils.getWMSStatus(shippingOrderData.getStatus()));
		}

		try {
			CommonUtils.createShippingOrderDataExcelFile(userCount, key, res, baseDir, model);
		} catch (Exception e) {
		}
		int start = currentPage * statisticDao.pageSize;
		int end = start + statisticDao.pageSize;
		end = end > res.size() ? res.size() : end;
		if (end > start)
			return res.subList(start, end);
		else
			return new ArrayList<ShippingOrderData>();
	}

	public Integer getShippingOrderDataTotalPages(Set<String> customerCodes, List<WareHouse> wareHouses, String relationNo,
			String consigneeName, String consigneePhone, String status, String shippingOrderNo, String logisticsProviderName,
			String startTime, String endTime) {
		Map<String, WareHouse> wareHouseMap = new HashMap<String, WareHouse>();
		for (WareHouse wareHouse : wareHouses) {
			wareHouseMap.put(wareHouse.getWareHouseCode(), wareHouse);
		}
		String key = SHIPPING_ORDER_DATA_KEY_TOTALSIZE_PREFIX
				+ CommonUtils.caculateHashCode(new ArrayList<Object>(customerCodes)) + "_"
				+ CommonUtils.caculateHashCode(new ArrayList<Object>(wareHouseMap.keySet())) + "_"
				+ (relationNo == null ? "" : relationNo) + "_" + (consigneeName == null ? "" : consigneeName) + "_"
				+ (consigneePhone == null ? "" : consigneePhone) + "_" + (status == null ? "" : status) + "_"
				+ (shippingOrderNo == null ? "" : shippingOrderNo) + "_"
				+ (logisticsProviderName == null ? "" : logisticsProviderName) + "_" + (startTime == null ? "" : startTime) + "_"
				+ (endTime == null ? "" : endTime);
		Integer res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (null == res) {
			res = statisticDao
					.getShippingOrderDatTotalSize(customerCodes, new ArrayList<String>(wareHouseMap.keySet()), relationNo,
							consigneeName, consigneePhone, status, shippingOrderNo, logisticsProviderName, startTime, endTime);
			if (null != res) {
				try {
					memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
				} catch (Exception e) {
				}
			}
		}
		return res;
	}

	public SalesOrder getSalesOrderDetail(Set<String> customerCodes, List<String> wareHouseCodes, String relationNo) {
		String key = SALES_ORDER_DATA_DETAIL_KEY_PREFIX + CommonUtils.caculateHashCode(new ArrayList<Object>(customerCodes))
				+ "_" + CommonUtils.caculateHashCode(new ArrayList<Object>(wareHouseCodes)) + "_"
				+ (relationNo == null ? "" : relationNo);
		SalesOrder res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (null == res) {
			res = statisticDao.getSalesOrderDetail(customerCodes, wareHouseCodes, relationNo);
			if (null != res) {
				try {
					memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
				} catch (Exception e) {
				}
			}
		}
		return res;
	}

	public List<ShippingOrderData> getShippingOrderDetail(Set<String> customerCodes, Set<String> wareHouseCodes,
			String relationNo, Model model) {
		String key = SHIPPING_ORDER_DATA_DETAIL_KEY_PREFIX + CommonUtils.caculateHashCode(new ArrayList<Object>(customerCodes))
				+ "_" + CommonUtils.caculateHashCode(new ArrayList<Object>(wareHouseCodes)) + "_"
				+ (relationNo == null ? "" : relationNo);
		List<ShippingOrderData> res = null;
		try {
			res = memcachedClient.get(key);
		} catch (Exception e) {
		}
		if (null == res) {
			Set<Integer> ids = new HashSet<Integer>();
			res = statisticDao.getShippingOrderData(customerCodes, new ArrayList<String>(wareHouseCodes), relationNo, null, null,
					null, null, null, null, null);
			for (ShippingOrderData shippingOrderData : res) {
				ids.add(shippingOrderData.getId());
			}
			List<SalesShipping> salesShippings = statisticDao.getSalesShippingOrderData(ids);
			List<IdoStatistic> idoStatistics = statisticDao.getOrderDetailSkuData(customerCodes, new ArrayList<String>(
					wareHouseCodes), relationNo);

			for (ShippingOrderData shippingOrderData : res) {
				int shippingOrderId = shippingOrderData.getId();
				for (SalesShipping salesShipping : salesShippings) {
					if (salesShipping.getShippingOrderId() == shippingOrderId) {
						shippingOrderData.addSalesShipping(salesShipping);
					}
				}
				int ldoId = shippingOrderData.getLdoId();
				for (IdoStatistic idoStatistic : idoStatistics) {
					if (idoStatistic.getId() == ldoId) {
						shippingOrderData.addIdoStatistics(idoStatistic);
					}
				}
			}
			if (null != res) {
				try {
					memcachedClient.set(key, 30 * 60 * 60, res, 30 * 60 * 60);
				} catch (Exception e) {
				}
			}
		}
		Map<String, IdoStatistic> map = new HashMap<String, IdoStatistic>();
		for (ShippingOrderData shippingOrderData : res) {
			for (IdoStatistic idoStatistic : shippingOrderData.getIdoStatistics()) {
				IdoStatistic temp = map.get(idoStatistic.getSkuCode());
				if (temp == null)
					map.put(idoStatistic.getSkuCode(), idoStatistic);
				else {
					temp.setQtyOrdered(temp.getQtyOrdered() + idoStatistic.getQtyOrdered());
					map.put(idoStatistic.getSkuCode(), temp);
				}
			}
		}

		model.addAttribute("merchInfo", map.values());
		return res;
	}
}
