package com.best.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.best.Constants;
import com.best.domain.BillInfo;
import com.best.domain.BillType;
import com.best.domain.Customer;
import com.best.domain.User;
import com.best.domain.WareHouse;
import com.best.service.BillService;
import com.best.service.BillTypeService;
import com.best.service.UserService;
import com.best.utils.CommonUtils;
import com.best.utils.DateUtil;

/**
 * ClassName:UserController Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-27
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BillService billService;

	@Autowired
	private BillTypeService billTypeService;

	@RequestMapping(value = "/user/checkUser.do")
	public String checkUser(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		String userCount = req.getParameter("userName");
		String userPassword = req.getParameter("password");

		User user = new User();
		user.setUserCount(userCount);
		user.setUserPassword(userPassword);
		user.setUserRole(0);

		user = userService.checkUser(user);

		if (user == null) {
			// 输入有错误

			model.addAttribute("userCount", userCount);
			model.addAttribute("errorLogin", Boolean.TRUE);

			return "redirect:/login.do";
		} else {
			HttpSession session = req.getSession();
			session.setAttribute(Constants.USER_TOKEN_IDENTIFY, user);
			return "redirect:/index.do";
		}
	}

	@RequestMapping(value = "/user/loginout.do")
	public String loginout(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.USER_TOKEN_IDENTIFY);
		if (user != null) {
			session.removeAttribute(Constants.USER_TOKEN_IDENTIFY);
		}

		return "redirect:/login.do";
	}

	@RequestMapping(value = "/user/user-manager.do")
	public String infoManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		return "/user/account-index";
	}

	@RequestMapping(value = "/user/password-update-view.do")
	public String memberUpdateView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		return "/user/account-password-update-view";
	}

	@RequestMapping(value = "/user/password-update.do")
	public String modifyPassword(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Long userId = Long.parseLong(req.getParameter("userId"));
		String oldPassword = req.getParameter("oldPassowrd");
		String newPassword = req.getParameter("newPassowrd");

		int modifyCount = userService.updateUserPassword(userId, oldPassword, newPassword);

		if (modifyCount == 0) {
			model.addAttribute("error", Boolean.TRUE);
			return "/user/account-password-update-view";
		} else {
			model.addAttribute("error", Boolean.FALSE);
			model.addAttribute("modifyStatus", Boolean.TRUE);
			return "/user/account-password-update-view";
		}
	}

	@RequestMapping(value = "/user/user-bill-status-update.do")
	public String billStatusUpdateManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		String billId = req.getParameter("billId");
		BillInfo billInfo = billService.getBill(Long.parseLong(billId));
		billInfo.setBillStatus(1);
		billService.updateBillStatus(billInfo);
		model.addAllAttributes(req.getParameterMap());
		model.addAttribute("handler", Boolean.TRUE);
		return "redirect:/user/user-bill-manager.do";
	}

	@RequestMapping(value = "/user/user-bill-manager.do")
	public String billManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/login.do";

		Object obj = req.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		User user = (User) obj;

		String handler = req.getParameter("handler");
		if (null != handler) {
			model.addAttribute("handler", Boolean.parseBoolean(handler));
		}

		String billType = req.getParameter("billType");
		List<BillType> billTypes = billTypeService.fillBills();
		if (StringUtils.isEmpty(billType)) {
			for (BillType billType2 : billTypes) {
				billType2.setChecked(Boolean.TRUE);
				billType = billType2.getBillType() + "";
				break;
			}
		} else {
			int type = Integer.parseInt(billType.trim());
			for (BillType billType2 : billTypes) {
				if (billType2.getBillType() == type) {
					billType2.setChecked(Boolean.TRUE);
					break;
				}
			}
		}
		model.addAttribute("billTypes", billTypes);
		model.addAttribute("billType", billType);

		String dateType = req.getParameter("dateType");
		String startTime = "";

		String endTime = DateUtil.getCurrentDateString();
		try {
			if ("4".equals(dateType)) {
				startTime = DateUtil.getPreDate(-360);
				model.addAttribute("dateType", "4");
			} else if ("2".equals(dateType)) {
				startTime = DateUtil.getPreDate(-90);
				model.addAttribute("dateType", "2");
			} else if ("3".equals(dateType)) {
				startTime = DateUtil.getPreDate(-180);
				model.addAttribute("dateType", "3");
			} else {
				startTime = DateUtil.getPreDate(-30);
				model.addAttribute("dateType", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> customerCodes = new ArrayList<String>();
		List<Customer> customers = user.getCustomers();
		String customerSelected = req.getParameter("customerSelected");
		if (StringUtils.isNotBlank(customerSelected)) {
			model.addAttribute("customerSelected", customerSelected);
			String customerCode = customerSelected;
			customerCodes.add(customerCode);
			for (Customer customer : customers) {
				if (customer.getCustomerCode().equals(customerCode)) {
					customer.setChecked(Boolean.TRUE);
					break;
				}
			}
		} else {
			// 所有客户
			customerCodes = new ArrayList<String>(user.getCustomerCodes());
			model.addAttribute("customerSelected", "");
		}

		List<String> wareHouseCodes = new ArrayList<String>();
		List<WareHouse> wareHouses = user.getWareHouses();
		String warehouseSelected = req.getParameter("warehouseSelected");
		if (StringUtils.isNotBlank(warehouseSelected)) {
			model.addAttribute("warehouseSelected", warehouseSelected);
			String wareHouseCode = warehouseSelected;
			wareHouseCodes.add(wareHouseCode);
			for (WareHouse wareHouse : wareHouses) {
				if (wareHouse.getWareHouseCode().equals(wareHouseCode)) {
					wareHouse.setChecked(Boolean.TRUE);
					break;
				}
			}
		} else {
			wareHouseCodes = new ArrayList<String>(user.getWareHouseCodes());
			// 所有仓库
			model.addAttribute("warehouseSelected", "");
		}

		model.addAttribute("customers", customers);
		model.addAttribute("wareHouses", wareHouses);

		String currentPage = req.getParameter("currentPage");
		int totalPage = 0;
		List<BillInfo> res = null;
		totalPage = billService.getTotalSize(customerCodes, wareHouseCodes, Integer.parseInt(billType), startTime, endTime);

		if (StringUtils.isBlank(currentPage))
			currentPage = "1";
		Integer page = Integer.parseInt(currentPage);
		if (!(totalPage == 0 || page > totalPage)) {
			res = billService.fillBills(customerCodes, wareHouseCodes, Integer.parseInt(billType), startTime, endTime, page - 1);
		} else {
			res = new ArrayList<BillInfo>();
		}

		model.addAttribute("res", res);
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
		return "/user/user-bill-manager";
	}

}
