package com.best.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.best.Constants;
import com.best.domain.Customer;
import com.best.domain.User;
import com.best.domain.WareHouse;
import com.best.service.CustomerService;
import com.best.service.UserService;
import com.best.service.WareHouseService;
import com.best.utils.CommonUtils;

/**
 * ClassName:AdminController Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-20
 */
@Controller
public class AdminController {
	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private WareHouseService wareHouseService;

	private static final Integer SPLIT_SIZE = 4;

	@RequestMapping(value = "/admin/admin-login.do")
	public String adminLoginUser(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		if (CommonUtils.checkSessionTimeOut(req)) {
			return "redirect:/admin/admin-index.do";
		}
		String userCount = req.getParameter("userCount");
		String errorLogin = req.getParameter("errorLogin");
		if (null != userCount)
			model.addAttribute("userCount", userCount);
		if (null != errorLogin)
			model.addAttribute("errorLogin", Boolean.parseBoolean(errorLogin));
		return "/admin/admin-login";
	}

	@RequestMapping(value = "/admin/add-member.do")
	public String addMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String userCount = req.getParameter("userCount");
		Integer count = userService.checkUserCountExisted(userCount);
		if (count > 0) {
			model.addAttribute("memberExisted", Boolean.TRUE);
			return "redirect:/admin/user-add-view.do";
		}
		String userName = req.getParameter("userName");
		String[] userCustomers = req.getParameterValues("userCustomer");
		String userCustomer = "";

		if (userCustomers != null && userCustomers.length > 0) {
			boolean isOr = false;
			for (String customer : userCustomers) {
				if (customer.trim().length() > 0) {
					if (isOr)
						userCustomer += ",";
					userCustomer += customer;
					isOr = true;
				}
			}
		}

		String[] wareHouses = req.getParameterValues("userWareHouse");
		String userWareHouses = "";
		if (null != wareHouses && wareHouses.length > 0) {
			boolean isOr = false;
			for (String warehouse : wareHouses) {
				if (warehouse.trim().length() > 0) {
					if (isOr)
						userWareHouses += ",";
					userWareHouses += warehouse;
					isOr = true;
				}
			}
		}

		String[] userProperties = req.getParameterValues("userProperties");
		String properties = "";
		if (null != userProperties && userProperties.length > 0) {
			boolean isOr = false;
			for (String property : userProperties) {
				if (property.trim().length() > 0) {
					if (isOr)
						properties += ",";
					properties += property;
					isOr = true;
				}
			}
		}

		User user = new User();
		user.setUserCount(userCount);
		user.setUserName(userName);
		user.setUserPassword("Abc123");
		user.setUserCustomers(userCustomer);
		user.setUserWareHouses(userWareHouses);
		user.setUserWareHousesProperties(properties);

		userService.addMember(user);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/user-add-view.do")
	public String addUserView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";
		List<Customer> customers = customerService.getAllCustomer();
		for (Customer customer : customers) {
			if (customer.getCustomerName().length() > 10)
				customer.setCustomerName(customer.getCustomerName().substring(0, 10));
		}
		List<List<Customer>> customerList = new ArrayList<List<Customer>>();
		int size = customers.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			customerList.add(customers.subList(start, end));
		}

		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		List<List<WareHouse>> wareHouseList = new ArrayList<List<WareHouse>>();
		size = wareHouses.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			wareHouseList.add(wareHouses.subList(start, end));
		}
		String memberExisted = req.getParameter("memberExisted");
		if (null != memberExisted)
			model.addAttribute("memberExisted", Boolean.parseBoolean(memberExisted));
		model.addAttribute("customerList", customerList);
		model.addAttribute("wareHouseList", wareHouseList);
		return "/admin/admin-add";
	}

	@RequestMapping(value = "/admin/admin-index.do")
	public String adminIndex(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		List<Customer> customers = customerService.getAllCustomer();
		String customerSelected = req.getParameter("customerSelected");
		if (StringUtils.isNotEmpty(customerSelected)) {
			model.addAttribute("customerSelected", customerSelected);
			String customerCode = customerSelected;
			for (Customer customer : customers) {
				if (customer.getCustomerCode().equals(customerCode)) {
					customer.setChecked(Boolean.TRUE);
					break;
				}
			}
		} else {
			model.addAttribute("customerSelected", "");
		}

		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		String warehouseSelected = req.getParameter("warehouseSelected");
		if (StringUtils.isNotEmpty(customerSelected)) {
			model.addAttribute("warehouseSelected", warehouseSelected);
			String wareHouseCode = warehouseSelected;
			for (WareHouse wareHouse : wareHouses) {
				if (wareHouse.getWareHouseCode().equals(wareHouseCode)) {
					wareHouse.setChecked(Boolean.TRUE);
					break;
				}
			}
		} else {
			model.addAttribute("warehouseSelected", "");
		}

		model.addAttribute("customers", customers);
		model.addAttribute("wareHouses", wareHouses);

		String searchName = req.getParameter("searchName");
		String searchCount = req.getParameter("searchCount");
		String currentPage = req.getParameter("currentPage");
		int totalPage = 0;
		List<User> res = null;
		try {
			totalPage = userService.getTotalSize(searchName, searchCount, customerSelected, warehouseSelected);
		} catch (Exception e1) {
		}

		if (StringUtils.isBlank(currentPage))
			currentPage = "1";
		Integer page = Integer.parseInt(currentPage);
		if (!(totalPage == 0 || page > totalPage)) {
			try {
				res = userService.getSearchUsers(searchName, searchCount, customerSelected, warehouseSelected, page - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (null == res)
			res = new ArrayList<User>();

		model.addAttribute("res", res);
		if (StringUtils.isNotBlank(searchName))
			model.addAttribute("searchName", searchName);
		else
			model.addAttribute("searchName", "");
		if (StringUtils.isNotBlank(searchCount))
			model.addAttribute("searchCount", searchCount);
		else
			model.addAttribute("searchCount", "");
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

		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("prePage", ((page - 1) < 1 ? 1 : (page - 1)) + "");
		model.addAttribute("nextPage", ((page + 1) > totalPage ? totalPage : (page + 1)) + "");

		return "/admin/admin-index";
	}

	@RequestMapping(value = "/admin/check-user-login.do")
	public String adminCheckUser(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		String userCount = req.getParameter("user_name");
		String userPassword = req.getParameter("password");

		User user = new User();
		user.setUserCount(userCount);
		user.setUserPassword(userPassword);

		user = userService.checkUser(user);

		if (user == null) {
			// 输入有错误

			model.addAttribute("userCount", userCount);
			model.addAttribute("errorLogin", Boolean.TRUE);

			return "redirect:/admin/admin-login.do";
		} else {
			HttpSession session = req.getSession();
			session.setAttribute(Constants.USER_TOKEN_IDENTIFY, user);
			return "redirect:/admin/admin-index.do";
		}
	}

	@RequestMapping(value = "/admin/admin-Logout.do")
	public String loginout(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.USER_TOKEN_IDENTIFY);
		if (user != null) {
			session.removeAttribute(Constants.USER_TOKEN_IDENTIFY);
		}

		return "redirect:/admin/admin-login.do";
	}

	@RequestMapping(value = "/admin/admin-setting-view.do")
	public String passwordView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		model.addAttribute("userId", req.getParameter("userId"));

		return "/admin/admin-password-update";
	}

	@RequestMapping(value = "/admin/update-member.do")
	public String updateMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		User user = userService.findUser(Long.parseLong(req.getParameter("userId")));

		String userName = req.getParameter("userName");
		String[] userCustomers = req.getParameterValues("userCustomer");
		String userCustomer = "";

		if (userCustomers != null && userCustomers.length > 0) {
			boolean isOr = false;
			for (String customer : userCustomers) {
				if (customer.trim().length() > 0) {
					if (isOr)
						userCustomer += ",";
					userCustomer += customer;
					isOr = true;
				}
			}
		}

		String[] wareHouses = req.getParameterValues("userWareHouse");
		String userWareHouses = "";
		if (null != wareHouses && wareHouses.length > 0) {
			boolean isOr = false;
			for (String warehouse : wareHouses) {
				if (warehouse.trim().length() > 0) {
					if (isOr)
						userWareHouses += ",";
					userWareHouses += warehouse;
					isOr = true;
				}
			}
		}

		String[] userProperties = req.getParameterValues("userProperties");
		String properties = "";
		if (null != userProperties && userProperties.length > 0) {
			boolean isOr = false;
			for (String property : userProperties) {
				if (property.trim().length() > 0) {
					if (isOr)
						properties += ",";
					properties += property;
					isOr = true;
				}
			}
		}

		user.setUserName(userName);
		user.setUserPassword(CommonUtils.decoder(user.getUserPassword()));
		user.setUserCustomers(userCustomer);
		user.setUserWareHouses(userWareHouses);
		user.setUserWareHousesProperties(properties);

		userService.updateMember(user);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-pas-reset.do")
	public String resetPasswordMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String userId = req.getParameter("userId");
		String currentPage = req.getParameter("currentPage");
		String searchName = req.getParameter("searchName");
		String searchCount = req.getParameter("searchCount");
		String warehouseSelected = req.getParameter("warehouseSelected");
		String customerSelected = req.getParameter("customerSelected");

		userService.resetPasswordMember(userId);

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("searchName", searchName);
		model.addAttribute("searchCount", searchCount);
		model.addAttribute("warehouseSelected", warehouseSelected);
		model.addAttribute("customerSelected", customerSelected);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-update-view.do")
	public String adminUpdateView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		User user = userService.findUser(Long.parseLong(req.getParameter("userId")));
		model.addAttribute("modifyUser", user);
		Set<String> customerCodes = user.getCustomerCodes();
		List<Customer> customers = customerService.getAllCustomer();
		for (Customer customer : customers) {
			if (customer.getCustomerName().length() > 10)
				customer.setCustomerName(customer.getCustomerName().substring(0, 10));
			if (customerCodes.contains(customer.getCustomerCode())) {
				customer.setChecked(Boolean.TRUE);
			}
		}

		Set<String> wareHousesCode = user.getWareHouseCodes();
		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		for (WareHouse wareHouse : wareHouses) {
			if (wareHousesCode.contains(wareHouse.getWareHouseCode())) {
				wareHouse.setChecked(Boolean.TRUE);
			}
		}

		List<List<Customer>> customerList = new ArrayList<List<Customer>>();
		int size = customers.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			customerList.add(customers.subList(start, end));
		}

		List<List<WareHouse>> wareHouseList = new ArrayList<List<WareHouse>>();
		size = wareHouses.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			wareHouseList.add(wareHouses.subList(start, end));
		}

		model.addAttribute("customerList", customerList);
		model.addAttribute("wareHouseList", wareHouseList);

		return "/admin/admin-update";
	}

	@RequestMapping(value = "/admin/admin-view.do")
	public String adminView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		User user = userService.findUser(Long.parseLong(req.getParameter("userId")));
		model.addAttribute("modifyUser", user);
		Set<String> customerCodes = user.getCustomerCodes();
		List<Customer> customers = customerService.getAllCustomer();
		for (Customer customer : customers) {
			if (customer.getCustomerName().length() > 10)
				customer.setCustomerName(customer.getCustomerName().substring(0, 10));
			if (customerCodes.contains(customer.getCustomerCode())) {
				customer.setChecked(Boolean.TRUE);
			}
		}

		Set<String> wareHousesCode = user.getWareHouseCodes();
		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		for (WareHouse wareHouse : wareHouses) {
			if (wareHousesCode.contains(wareHouse.getWareHouseCode())) {
				wareHouse.setChecked(Boolean.TRUE);
			}
		}

		List<List<Customer>> customerList = new ArrayList<List<Customer>>();
		int size = customers.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			customerList.add(customers.subList(start, end));
		}

		List<List<WareHouse>> wareHouseList = new ArrayList<List<WareHouse>>();
		size = wareHouses.size();
		for (int index = 0; index < size; index++) {
			int start = index * SPLIT_SIZE;
			if (start > size)
				break;
			int end = start + SPLIT_SIZE;
			if (end > size)
				end = size;
			wareHouseList.add(wareHouses.subList(start, end));
		}

		model.addAttribute("customerList", customerList);
		model.addAttribute("wareHouseList", wareHouseList);

		return "/admin/admin-view";
	}

	@RequestMapping(value = "/admin/admin-delete.do")
	public String deleteMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String userId = req.getParameter("userId");
		String currentPage = req.getParameter("currentPage");
		String searchName = req.getParameter("searchName");
		String searchCount = req.getParameter("searchCount");
		String warehouseSelected = req.getParameter("warehouseSelected");
		String customerSelected = req.getParameter("customerSelected");

		userService.deleteMember(userId);

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("searchName", searchName);
		model.addAttribute("searchCount", searchCount);
		model.addAttribute("warehouseSelected", warehouseSelected);
		model.addAttribute("customerSelected", customerSelected);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-update-password.do")
	public String modifyPassword(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		Long userId = Long.parseLong(req.getParameter("userId"));
		String oldPassword = req.getParameter("old_passowrd");
		String newPassword = req.getParameter("new_passowrd");
		model.addAttribute("userId", userId);

		int modifyCount = userService.updateUserPassword(userId, oldPassword, newPassword);

		if (modifyCount == 0) {
			model.addAttribute("error", Boolean.TRUE);
			return "/admin/admin-password-update";
		} else {
			model.addAttribute("error", Boolean.FALSE);
			model.addAttribute("modifyStatus", Boolean.TRUE);
			return "/admin/admin-password-update";
		}
	}

}
