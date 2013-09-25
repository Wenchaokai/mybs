package com.best.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.best.domain.SalesOrder;
import com.best.domain.ShippingOrderData;
import com.best.domain.User;
import com.best.domain.WareHouse;
import com.best.service.CarrierService;
import com.best.service.CustomerService;
import com.best.service.StatisticService;
import com.best.service.WareHouseService;
import com.best.utils.CommonUtils;

/**
 * ClassName:OrderController Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-10-17
 */
@Controller
public class OrderController {

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private CarrierService carrierService;

	@Autowired
	private WareHouseService wareHouseService;

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/order/order-index.do")
	public String dataIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;
		Set<String> customerCodes = user.getCustomerCodes();
		StringBuffer param = new StringBuffer();
		String relationNo = req.getParameter("relationNo");
		if (StringUtils.isEmpty(relationNo))
			relationNo = "";
		model.addAttribute("relationNo", relationNo);
		param.append("relationNo").append("=").append(relationNo);

		String userName = req.getParameter("userName");
		if (StringUtils.isEmpty(userName))
			userName = "";
		model.addAttribute("userName", userName);
		param.append("&").append("userName").append("=").append(userName);

		String phone = req.getParameter("phone");
		if (StringUtils.isEmpty(phone))
			phone = "";
		model.addAttribute("phone", phone);
		param.append("&").append("phone").append("=").append(phone);

		String status = req.getParameter("status");
		if (StringUtils.isEmpty(status) || status.equals("-1"))
			status = "";
		model.addAttribute("status", status);
		param.append("&").append("status").append("=").append(status);

		String startTime = req.getParameter("startTime");
		if (StringUtils.isEmpty(startTime))
			startTime = "";
		model.addAttribute("startTime", startTime);
		param.append("&").append("startTime").append("=").append(startTime);

		String endTime = req.getParameter("endTime");
		if (StringUtils.isEmpty(endTime))
			endTime = "";
		model.addAttribute("endTime", endTime);
		param.append("&").append("endTime").append("=").append(endTime);

		String shipperNo = req.getParameter("shipperNo");
		if (StringUtils.isEmpty(shipperNo))
			shipperNo = "";
		model.addAttribute("shipperNo", shipperNo);
		param.append("&").append("shipperNo").append("=").append(shipperNo);

		String shipperName = req.getParameter("shipperName");
		List<Carrier> carriers = carrierService.fillCarriers();
		if (StringUtils.isEmpty(shipperName)) {
			shipperName = "";
		}
		param.append("&").append("shipperName").append("=").append(shipperName);
		for (Carrier carrier : carriers) {
			if (carrier.getCarrierCode().equals(shipperName))
				carrier.setChecked(Boolean.TRUE);
		}
		model.addAttribute("carriers", carriers);

		String currentPage = req.getParameter("currentPage");

		int totalPage = 0;
		List<ShippingOrderData> res = null;
		totalPage = statisticService.getShippingOrderDataTotalPages(customerCodes, user.getWareHouses(), relationNo, userName,
				phone, status, shipperNo, shipperName, startTime, endTime);

		if (StringUtils.isBlank(currentPage))
			currentPage = "1";
		Integer page = Integer.parseInt(currentPage);
		if (!(totalPage == 0 || page > totalPage)) {
			res = statisticService.getShippingOrderData(user.getUserCount(), customerCodes, user.getWareHouses(), relationNo,
					userName, phone, status, shipperNo, shipperName, startTime, endTime, page - 1, model);
		}

		if (null == res)
			res = new ArrayList<ShippingOrderData>();
		model.addAttribute("res", res);
		model.addAttribute("params", param);

		model.addAttribute("currentPage", page);
		int start = page - 2;
		if (start <= 1)
			start = 1;
		int end = start + 4;
		if (end > totalPage) {
			int dis = end - totalPage;
			end = totalPage;
			start -= dis;
			if (start <= 1)
				start = 1;
		}
		if (end < start)
			end = start;

		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("prePage", ((page - 1) < 1 ? 1 : (page - 1)) + "");
		model.addAttribute("nextPage", ((page + 1) > totalPage ? totalPage : (page + 1)) + "");

		return "/order/order-index";
	}

	@RequestMapping(value = "/order/order-detail.do")
	public String dataDetail(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;
		Set<String> customerCodes = user.getCustomerCodes();
		Set<String> wareHouseCodes = user.getWareHouseCodes();
		String relationNo = req.getParameter("relationNo");
		List<ShippingOrderData> shippingOrderData = statisticService.getShippingOrderDetail(customerCodes, wareHouseCodes,
				relationNo, model);
		// 订单详情
		SalesOrder salesOrder = statisticService.getSalesOrderDetail(customerCodes, new ArrayList<String>(wareHouseCodes),
				relationNo);
		if (null != salesOrder) {
			CommonUtils.convertStatus(salesOrder);
			WareHouse wareHouse = wareHouseService.getWareHouse(salesOrder.getWareHouseId());
			salesOrder.setWareHouseName(wareHouse.getWareHouseName());
			Customer customer = customerService.getCustomer(salesOrder.getCustomerId());
			salesOrder.setCustomerName(customer.getCustomerName());
		} else
			salesOrder = new SalesOrder();
		model.addAttribute("shippingOrderData", shippingOrderData);
		model.addAttribute("salesOrder", salesOrder);
		return "/order/order-detail";
	}
}
