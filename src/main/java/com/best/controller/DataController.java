package com.best.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.best.Constants;
import com.best.domain.Carrier;
import com.best.domain.Customer;
import com.best.domain.OrderData;
import com.best.domain.SKU;
import com.best.domain.User;
import com.best.domain.WareHouse;
import com.best.domain.WmsStatistic;
import com.best.service.CarrierService;
import com.best.service.SkuService;
import com.best.service.StatisticService;
import com.best.service.WmsStatisticService;
import com.best.utils.CommonUtils;
import com.best.utils.DateUtil;

/**
 * ClassName:DataController Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-6
 */
@Controller
public class DataController {

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private CarrierService carrierService;

	@Autowired
	private WmsStatisticService wmsStatisticService;

	@Autowired
	private SkuService skuService;

	@RequestMapping(value = "/data/data-index.do")
	public String dataIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;
		Set<String> customerCodes = user.getCustomerCodes();
		List<String> wareHouseCodes = new ArrayList<String>(user.getWareHouseCodes());
		List<String> dates = DateUtil.getListDate();
		List<OrderData> orderDatas = new ArrayList<OrderData>();
		for (int index = 0; index < dates.size(); index++) {
			String startTime = dates.get(index);
			String endTime = null;
			if (index > 0)
				endTime = dates.get(index - 1);
			OrderData orderData = statisticService.getOrderData(customerCodes, wareHouseCodes, startTime, endTime);
			orderDatas.add(orderData);
		}
		model.addAttribute("currentMonthStart", dates.get(0));
		model.addAttribute("currentDate", DateUtil.getCurrentDateString());
		model.addAttribute("currentMonthOrderData", orderDatas.get(0));
		OrderData currentDateOrder = statisticService.getOrderData(customerCodes, wareHouseCodes,
				DateUtil.getCurrentDateString(), null);
		TreeMap<String, String> map = new TreeMap<String, String>();
		for (int index = dates.size() - 2; index > 0; index--) {
			OrderData orderData = orderDatas.get(index);
			OrderData preOrderData = orderDatas.get(index + 1);
			String modelName = "SHOWFILE-" + index;
			statisticService.createOrderDataJson(user.getUserCount(), currentDateOrder, orderData, preOrderData, modelName, map);
		}

		model.addAttribute("currentDateMap", map.descendingMap());

		String preSevenDate = DateUtil.getPreSevenDate();
		String key = "CURRENT_PRE_SEVEN_ORDER_DATA";
		statisticService.getDayOrderData(user.getUserCount(), customerCodes, wareHouseCodes, preSevenDate, null, key, model);
		String preMonthDate = DateUtil.getPreMonthDate();
		key = "CURRENT_PRE_MONTH_ORDER_DATA";
		statisticService.getDayOrderData(user.getUserCount(), customerCodes, wareHouseCodes, preMonthDate, null, key, model);

		return "/data/data-index";
	}

	@RequestMapping(value = "/data/data-sku-index.do")
	public String dataSkuIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;

		String startTime = req.getParameter("startTime");
		if (StringUtils.isBlank(startTime))
			startTime = DateUtil.getPreSevenDate();
		String endTime = req.getParameter("endTime");
		if (StringUtils.isBlank(endTime))
			endTime = DateUtil.getCurrentDateString();

		if (startTime.compareTo(endTime) > 0) {
			String tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
		}

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		String customerCode = req.getParameter("customerCode");
		if (StringUtils.isBlank(customerCode)) {
			// 如果不存在，随便获取一个
			customerCode = user.getCustomers().get(0).getCustomerCode();
		}

		List<Customer> customers = user.getCustomers();
		if (StringUtils.isNotBlank(customerCode)) {
			for (Customer customer : customers) {
				if (customerCode.equals(customer.getCustomerCode())) {
					customer.setChecked(Boolean.TRUE);
				} else {
					customer.setChecked(Boolean.FALSE);
				}
			}
		}

		String wareHouseSelected = req.getParameter("wareHouseSelected");
		if (StringUtils.isBlank(wareHouseSelected))
			wareHouseSelected = "-1#所有仓库";
		String[] parts = wareHouseSelected.split("#");
		String wareHouseCode = parts[0];
		String wareHouseName = parts[1];

		List<WareHouse> wareHouses = user.getWareHouses();
		if (StringUtils.isNotBlank(wareHouseCode)) {
			for (WareHouse wareHouse : wareHouses) {
				if (wareHouseCode.equals(wareHouse.getWareHouseCode())) {
					wareHouse.setChecked(Boolean.TRUE);
				} else {
					wareHouse.setChecked(Boolean.FALSE);
				}
			}
		}

		model.addAttribute("wareHouses", wareHouses);
		model.addAttribute("customers", customers);
		model.addAttribute("wareHouseName", wareHouseName);

		String skuCode = req.getParameter("skuCode");
		if (StringUtils.isBlank(skuCode))
			skuCode = "-1";
		model.addAttribute("skuCode", skuCode);
		model.addAttribute("currentSku", skuCode.equals("-1") ? "" : skuCode);

		String key = user.getUserCount() + "_" + skuCode + "_" + customerCode + "_" + wareHouseCode + "_ORDER_" + startTime + "_"
				+ endTime;
		statisticService.getDistributedSkuIdoCount(skuCode, customerCode, wareHouseCode, startTime, endTime, key, model);

		return "/data/data-sku-search";
	}

	@RequestMapping(value = "/data/data-order-index.do")
	public String dataOrderIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException,
			ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;

		String startTime = req.getParameter("startTime");
		if (StringUtils.isBlank(startTime))
			startTime = DateUtil.getPreSevenDate();
		String endTime = req.getParameter("endTime");
		if (StringUtils.isBlank(endTime))
			endTime = DateUtil.getCurrentDateString();

		if (startTime.compareTo(endTime) > 0) {
			String tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
		}

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		String customerCode = req.getParameter("customerCode");
		if (StringUtils.isBlank(customerCode)) {
			// 如果不存在，随便获取一个
			customerCode = user.getCustomers().get(0).getCustomerCode();
		}

		List<Customer> customers = user.getCustomers();
		if (StringUtils.isNotBlank(customerCode)) {
			for (Customer customer : customers) {
				if (customerCode.equals(customer.getCustomerCode())) {
					customer.setChecked(Boolean.TRUE);
				} else {
					customer.setChecked(Boolean.FALSE);
				}
			}
		}

		String wareHouseSelected = req.getParameter("wareHouseSelected");
		if (StringUtils.isBlank(wareHouseSelected))
			wareHouseSelected = "-1#所有仓库";
		String[] parts = wareHouseSelected.split("#");
		String wareHouseCode = parts[0];
		String wareHouseName = parts[1];

		List<WareHouse> wareHouses = user.getWareHouses();
		if (StringUtils.isNotBlank(wareHouseCode)) {
			for (WareHouse wareHouse : wareHouses) {
				if (wareHouseCode.equals(wareHouse.getWareHouseCode())) {
					wareHouse.setChecked(Boolean.TRUE);
				} else {
					wareHouse.setChecked(Boolean.FALSE);
				}
			}
		}

		model.addAttribute("wareHouses", wareHouses);
		model.addAttribute("customers", customers);
		model.addAttribute("wareHouseName", wareHouseName);

		String key = user.getUserCount() + "_" + "-1" + "_" + customerCode + "_" + wareHouseCode + "_ORDER_" + startTime + "_"
				+ endTime;
		statisticService.getDistributedSkuIdoCount("-1", customerCode, wareHouseCode, startTime, endTime, key, model);

		return "/data/data-order-source-search";
	}

	@RequestMapping(value = "/data/map.do")
	public String map(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";
		String mapFile = req.getParameter("mapFile");
		String totalCount = req.getParameter("totalCount");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String skuCode = req.getParameter("skuCode");
		String isSku = req.getParameter("isSku");
		if (StringUtils.isNotBlank(isSku))
			model.addAttribute("isSku", isSku);
		String skuName = "所有SKU";
		if (StringUtils.isNotBlank(skuCode)) {
			if (!"-1".equals(skuCode)) {
				SKU sku = skuService.getSku(skuCode);
				if (null != sku) {
					skuName = sku.getSkuName();

				} else {
					skuName = "";
				}
			}
		}
		String wareHouseName = req.getParameter("wareHouseName");

		model.addAttribute("wareHouseName", wareHouseName);
		model.addAttribute("mapFile", mapFile);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("currentSku", skuCode.equals("-1") ? "" : skuCode);
		model.addAttribute("skuCode", skuCode);
		model.addAttribute("skuName", skuName);
		return "/data/order-source-map";
	}

	@RequestMapping(value = "/data/data-store-index.do")
	public String dataStoreIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;

		StringBuffer buf = new StringBuffer();

		String currentWareHouseCode = req.getParameter("wareHouseSelected");
		String currentWareHouseName = "";
		List<WareHouse> wareHouses = user.getWareHouses();
		if (StringUtils.isNotBlank(currentWareHouseCode)) {
			for (WareHouse wareHouse : wareHouses) {
				if (currentWareHouseCode.equals(wareHouse.getWareHouseCode())) {
					wareHouse.setChecked(Boolean.TRUE);
					currentWareHouseName = wareHouse.getWareHouseName();
				} else {
					wareHouse.setChecked(Boolean.FALSE);
				}
			}
		} else {
			currentWareHouseCode = "";
		}
		buf.append("wareHouseSelected").append("=").append(currentWareHouseCode);

		String currentCustomerCode = req.getParameter("customerSelected");
		String currentCustomerName = "";
		List<Customer> customers = user.getCustomers();
		if (StringUtils.isNotBlank(currentCustomerCode)) {
			for (Customer customer : customers) {
				if (currentCustomerCode.equals(customer.getCustomerCode())) {
					customer.setChecked(Boolean.TRUE);
					currentCustomerName = customer.getCustomerName();
				} else {
					customer.setChecked(Boolean.FALSE);
				}
			}
		} else {
			currentCustomerCode = customers.get(0).getCustomerCode();
			currentCustomerName = customers.get(0).getCustomerName();
			customers.get(0).setChecked(Boolean.TRUE);
		}
		buf.append("&").append("customerSelected").append("=").append(currentCustomerCode);
		model.addAttribute("wareHouses", wareHouses);
		model.addAttribute("customers", customers);

		String currentSkuCode = req.getParameter("currentSkuCode");
		if (StringUtils.isBlank(currentSkuCode))
			currentSkuCode = "";
		buf.append("&").append("currentSkuCode").append("=").append(currentSkuCode);
		model.addAttribute("currentSkuCode", currentSkuCode);
		String currentSkuName = req.getParameter("currentSkuName");
		if (StringUtils.isBlank(currentSkuName))
			currentSkuName = "";
		model.addAttribute("currentSkuName", currentSkuName);
		buf.append("&").append("currentSkuName").append("=").append(currentSkuName);

		Map<Integer, String> propertiesMap = user.getPropertyMap();
		Map<Integer, String> propertiesValuesMap = new HashMap<Integer, String>();
		Map<String, String> mapValues = new HashMap<String, String>();
		int index = 0;
		Map<Integer, String> mapValuesRes = new HashMap<Integer, String>();
		for (Entry<Integer, String> entry : propertiesMap.entrySet()) {
			Integer key = entry.getKey();
			if (index < 3) {
				mapValuesRes.put(key, entry.getValue());
				index++;
			}
			Integer keyIndex = key - 1;
			String formValue = req.getParameter(key + "");
			if (StringUtils.isBlank(formValue))
				formValue = "";
			propertiesValuesMap.put(key, formValue);
			if (StringUtils.isNotBlank(formValue))
				mapValues.put(WmsStatistic.properties[keyIndex], formValue);
			buf.append("&").append(key).append("=").append(formValue);
		}
		model.addAttribute("propertieValues", propertiesValuesMap);
		model.addAttribute("mapValues", mapValuesRes);

		int totalSize = wmsStatisticService.getWmsSkuIdoTotalSize(user.getUserCount(), currentWareHouseCode, currentSkuCode,
				currentSkuName, currentCustomerCode, mapValues);

		List<WmsStatistic> res = null;
		String currentPage = req.getParameter("currentPage");
		if (StringUtils.isBlank(currentPage))
			currentPage = "1";
		Integer page = Integer.parseInt(currentPage);
		if (!(totalSize == 0 || page > totalSize)) {
			try {
				res = wmsStatisticService.getWmsSkuIdoCount(user.getUserCount(), currentWareHouseCode, currentWareHouseName,
						currentSkuCode, currentSkuName, currentCustomerCode, currentCustomerName, mapValues,
						Integer.parseInt(currentPage) - 1, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (null == res)
			res = new ArrayList<WmsStatistic>();

		model.addAttribute("currentPage", page);
		int start = page - 2;
		if (start <= 1)
			start = 1;
		int end = start + 4;
		if (end > totalSize) {
			int dis = end - totalSize;
			end = totalSize;
			start -= dis;
			if (start <= 1)
				start = 1;
		}

		model.addAttribute("params", buf.toString());
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("prePage", ((page - 1) < 1 ? 1 : (page - 1)) + "");
		model.addAttribute("nextPage", ((page + 1) > totalSize ? totalSize : (page + 1)) + "");

		model.addAttribute("res", res);

		return "/data/data-store-search";
	}

	@RequestMapping(value = "/data/data-carrier-index.do")
	public String dataCarrierIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException,
			ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;

		String type = req.getParameter("type");
		if (StringUtils.isBlank(type))
			type = "1";
		model.addAttribute("type", type);

		String startTime = req.getParameter("startTime");
		if (StringUtils.isBlank(startTime))
			startTime = DateUtil.getPreSevenDate();
		String endTime = req.getParameter("endTime");
		if (StringUtils.isBlank(endTime))
			endTime = DateUtil.getCurrentDateString();

		if (startTime.compareTo(endTime) > 0) {
			String tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
		}

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		String customerCode = req.getParameter("customerCode");
		if (StringUtils.isBlank(customerCode)) {
			// 如果不存在，随便获取一个
			customerCode = user.getCustomers().get(0).getCustomerCode();
		}

		List<Customer> customers = user.getCustomers();
		if (StringUtils.isNotBlank(customerCode)) {
			for (Customer customer : customers) {
				if (customerCode.equals(customer.getCustomerCode())) {
					customer.setChecked(Boolean.TRUE);
				} else {
					customer.setChecked(Boolean.FALSE);
				}
			}
		}

		String wareHouseSelected = req.getParameter("wareHouseSelected");
		if (StringUtils.isBlank(wareHouseSelected))
			wareHouseSelected = "-1#所有仓库";
		String[] parts = wareHouseSelected.split("#");
		String wareHouseCode = parts[0];
		String wareHouseName = parts[1];

		List<WareHouse> wareHouses = user.getWareHouses();
		if (StringUtils.isNotBlank(wareHouseCode)) {
			for (WareHouse wareHouse : wareHouses) {
				if (wareHouseCode.equals(wareHouse.getWareHouseCode())) {
					wareHouse.setChecked(Boolean.TRUE);
				} else {
					wareHouse.setChecked(Boolean.FALSE);
				}
			}
		}

		model.addAttribute("wareHouses", wareHouses);
		model.addAttribute("customers", customers);
		model.addAttribute("wareHouseName", wareHouseName);

		if (type.equals("1")) {
			// 订单分布
			String key = user.getUserCount() + "_" + customerCode + "_" + wareHouseCode + "_CARRIER_" + startTime + "_" + endTime;
			statisticService.getCarrierDistributedCount(customerCode, wareHouseCode, startTime, endTime, key, model);
			return "/data/data-carrier-distributed";
		} else {
			String carrierCode = req.getParameter("carrierCode");
			List<Carrier> carriers = carrierService.fillCarriers();
			if (StringUtils.isNotBlank(carrierCode)) {
				for (Carrier carrier : carriers) {
					if (carrier.equals(carrierCode))
						carrier.setChecked(Boolean.TRUE);
				}
			} else {
				carriers.get(0).setChecked(Boolean.TRUE);
				carrierCode = carriers.get(0).getCarrierCode();
			}
			model.addAttribute("carriers", carriers);
			String key = user.getUserCount() + "_" + customerCode + "_" + wareHouseCode + "_" + carrierCode + "_CARRIER_PROMISE_"
					+ startTime + "_" + endTime;
			statisticService.getCarrierPromiseCount(customerCode, wareHouseCode, carrierCode, startTime, endTime, key, model);
			return "/data/data-carrier-promise";
		}

	}
}
