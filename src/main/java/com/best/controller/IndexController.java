package com.best.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.best.dao.StatisticDao;
import com.best.dao.WmsStatisticDao;
import com.best.utils.CommonUtils;

/**
 * ClassName:IndexController Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-27
 */
@Controller
public class IndexController {

	@Autowired
	private WmsStatisticDao wmsStatisticDao;

	@Autowired
	private StatisticDao statisticDao;

	@RequestMapping(value = "/login.do")
	public String checkUser(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		// // List<IdoStatistic> statistics =
		// // statisticDao.getDistributedSkuIdoCount("6941499109123",
		// // "EOMS-WMS-GUANYI-COFCO",
		// // "WH_EC_BJ", "20120120", "20120210");
		// //
		// // statistics = statisticDao.getDistributedSkuIdoCount(null,
		// // "EOMS-WMS-GUANYI-COFCO", "WH_EC_BJ", "20120120", "20120210");
		// //
		// // statistics = statisticDao.getDistributedSkuIdoCount(null,
		// // "EOMS-WMS-GUANYI-COFCO", null, "20120120", "20120210");
		// //
		// // statistics =
		// statisticDao.getDistributedSkuIdoCount("6941499109123",
		// // "EOMS-WMS-GUANYI-COFCO", null, "20120120",
		// // "20120210");
		// Set<String> tt = new HashSet<String>();
		// tt.add("EOMS-WMS-GUANYI-COFCO");
		//
		// List<String> ttt = new ArrayList<String>();
		// ttt.add("WH_EC_BJ");
		// // int statistics = statisticDao.getShippingOrderDatTotalSize(tt,
		// ttt,
		// // "227718876168216", "之江", "80906074", "WMS_DELIVERED",
		// // "21010257076", "HTKY", "20130210", "20130223");
		//
		// // List<ShippingOrderData> TSF =
		// statisticDao.getShippingOrderData(tt,
		// // ttt, "227718876168216", "之江", "80906074",
		// // "WMS_DELIVERED", "21010257076", "HTKY", "20130210", "20130223");
		//
		// // Set<Integer> tttt = new HashSet<Integer>();
		// // tttt.add(7907097);
		// // tttt.add(7837214);
		// //
		// // SalesOrder tdt = statisticDao.getSalesOrderDetail(tt, ttt,
		// // "227718876168216");
		// //
		// // List<SalesShipping> ttsfa =
		// // statisticDao.getSalesShippingOrderData(tttt);
		//
		// List<String> codes = new ArrayList<String>();
		// codes.add("6941499109123");
		// codes.add("6939656100670");
		//
		// Map<String, String> mapValuesMap = new HashMap<String, String>();
		// mapValuesMap.put("UDF1", "UDF1");
		// mapValuesMap.put("UDF2", "UDF2");
		// mapValuesMap.put("UDF3", "UDF3");
		// mapValuesMap.put("UDF4", "UDF4");
		// mapValuesMap.put("UDF5", "UDF5");
		// mapValuesMap.put("UDF6", "UDF6");
		// mapValuesMap.put("UDF7", "UDF7");
		// mapValuesMap.put("UDF8", "UDF8");
		// mapValuesMap.put("SKUHIGH", "0");
		// mapValuesMap.put("SKULENGTH", "0");
		// mapValuesMap.put("SKUWIDTH", "0");
		// mapValuesMap.put("PRICE", "0");
		//
		// int size = 0;
		// try {
		// List<WmsStatistic> ttlkj =
		// wmsStatisticDao.getWmsStatistic("6939656100670",
		// "EOMS-WMS-GUANYI-COFCO", "WH_EC_BJ",
		// "EC_BJ", null, null, 1);
		// size = wmsStatisticDao.getWmsStatisticSize("6939656100670",
		// "EOMS-WMS-GUANYI-COFCO", "WH_EC_BJ", "EC_BJ", null, null);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// size = wmsStatisticDao.getWmsStatisticSize(null,
		// "EOMS-WMS-GUANYI-COFCO", "WH_EC_BJ", "EC_BJ", codes, mapValuesMap);

		if (CommonUtils.checkSessionTimeOut(req)) {
			return "redirect:/index.do";
		}
		String userCount = req.getParameter("userCount");
		String errorLogin = req.getParameter("errorLogin");
		if (null != userCount)
			model.addAttribute("userCount", userCount);
		if (null != errorLogin)
			model.addAttribute("errorLogin", Boolean.parseBoolean(errorLogin));
		return "login";
	}

	@RequestMapping(value = "/index.do")
	public String showIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		return "index";
	}
}
