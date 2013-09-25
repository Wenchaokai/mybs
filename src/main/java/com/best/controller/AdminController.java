package com.best.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import com.best.service.CustomerService;
import com.best.service.UserService;
import com.best.service.WareHouseService;
import com.best.utils.CommonUtils;
import com.best.utils.DateUtil;

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

	@Autowired
	private BillService billService;

	@Autowired
	private BillTypeService billTypeService;

	private static final Integer SPLIT_SIZE = 4;

	@RequestMapping(value = "/admin/admin-login.do")
	public String adminLoginUser(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		if (CommonUtils.checkAdminSessionTimeOut(req)) {
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

		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String userCount = req.getParameter("userCount");
		Integer count = userService.checkUserCountExisted(userCount);
		if (count > 0) {
			model.addAttribute("memberExisted", Boolean.TRUE);
			return "redirect:/admin/user-add-view.do";
		}
		String userName = req.getParameter("userName");
		String userRole = req.getParameter("userRole");

		User user = new User();
		user.setUserCount(userCount);
		user.setUserName(userName);
		user.setUserPassword("Abc123");
		user.setUserRole(Integer.parseInt(userRole));
		user.setUserCustomers("");
		user.setUserWareHouses("");
		user.setUserWareHousesProperties("");

		if (Integer.parseInt(userRole) == 0) {
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
						String value = req.getParameter("userProperties" + property);
						properties += property + "#" + value;
						isOr = true;
					}
				}
			}

			String[] security = req.getParameterValues("security");
			user.setUserSecurity(CommonUtils.getSecurity(security));

			user.setUserCustomers(userCustomer);
			user.setUserWareHouses(userWareHouses);
			user.setUserWareHousesProperties(properties);
		}

		userService.addMember(user);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/user-add-view.do")
	public String addUserView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
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
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String resetFlag = req.getParameter("resetFlag");
		if (StringUtils.isNotBlank(resetFlag)) {
			model.addAttribute("error", Boolean.TRUE);
			model.addAttribute("resetFlag", Boolean.parseBoolean(resetFlag));
		}
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
		if (StringUtils.isNotEmpty(warehouseSelected)) {
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
		String deleteFlag = req.getParameter("deleteFlag");
		if (null != deleteFlag) {
			if ((page > totalPage)) {
				page = page - 1;
				if (page < 1)
					page = 1;
			}
		}
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
		user.setUserRole(1);

		user = userService.checkUser(user);

		if (user == null) {
			// 输入有错误

			model.addAttribute("userCount", userCount);
			model.addAttribute("errorLogin", Boolean.TRUE);

			return "redirect:/admin/admin-login.do";
		} else {
			HttpSession session = req.getSession();
			session.setAttribute(Constants.ADMIN_USER_TOKEN_IDENTIFY, user);
			return "redirect:/admin/admin-index.do";
		}
	}

	@RequestMapping(value = "/admin/admin-Logout.do")
	public String loginout(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.ADMIN_USER_TOKEN_IDENTIFY);
		if (user != null) {
			session.removeAttribute(Constants.ADMIN_USER_TOKEN_IDENTIFY);
		}

		return "redirect:/admin/admin-login.do";
	}

	@RequestMapping(value = "/admin/admin-setting-view.do")
	public String passwordView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		model.addAttribute("userId", req.getParameter("userId"));

		return "/admin/admin-password-update";
	}

	@RequestMapping(value = "/admin/update-member.do")
	public String updateMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		User user = userService.findUser(Long.parseLong(req.getParameter("userId")));

		String userName = req.getParameter("userName");
		String userRole = req.getParameter("userRole");

		user.setUserName(userName);
		user.setUserPassword(CommonUtils.decoder(user.getUserPassword()));
		user.setUserRole(Integer.parseInt(userRole));

		if (Integer.parseInt(userRole) == 0) {
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
						String value = req.getParameter("userProperties" + property);
						properties += property + "#" + value;
						isOr = true;
					}
				}
			}
			String[] security = req.getParameterValues("security");
			user.setUserSecurity(CommonUtils.getSecurity(security));

			user.setUserCustomers(userCustomer);
			user.setUserWareHouses(userWareHouses);
			user.setUserWareHousesProperties(properties);
		} else {
			user.setUserCustomers("");
			user.setUserWareHouses("");
			user.setUserWareHousesProperties("");
		}

		userService.updateMember(user);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-pas-reset.do")
	public String resetPasswordMember(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String userId = req.getParameter("userId");
		// String currentPage = req.getParameter("currentPage");
		// String searchName = req.getParameter("searchName");
		// String searchCount = req.getParameter("searchCount");
		// String warehouseSelected = req.getParameter("warehouseSelected");
		// String customerSelected = req.getParameter("customerSelected");
		// model.addAttribute("currentPage", currentPage);
		// model.addAttribute("searchName", searchName);
		// model.addAttribute("searchCount", searchCount);
		// model.addAttribute("warehouseSelected", warehouseSelected);
		// model.addAttribute("customerSelected", customerSelected);
		model.addAllAttributes(req.getParameterMap());

		try {
			userService.resetPasswordMember(userId);
			model.addAttribute("resetFlag", Boolean.TRUE);
		} catch (Exception e) {
			model.addAttribute("resetFlag", Boolean.FALSE);
		}
		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-update-view.do")
	public String adminUpdateView(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
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

		if (!CommonUtils.checkAdminSessionTimeOut(req))
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

		if (!CommonUtils.checkAdminSessionTimeOut(req))
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
		model.addAttribute("deleteFlag", Boolean.TRUE);

		return "redirect:/admin/admin-index.do";
	}

	@RequestMapping(value = "/admin/admin-update-password.do")
	public String modifyPassword(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
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

	@RequestMapping(value = "/admin/admin-update-bill.do")
	public String updateBill(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String billType = null;
		String year = null;
		String month = null;
		String customerSelected = null;
		String warehouseSelected = null;
		String billAccount = null;
		String customerCode = null;
		String customerName = null;
		String wareHouseCode = null;
		String wareHouseName = null;
		File summaryFile = null;
		File detailFile = null;
		File baseFile = null;
		String billId = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(req);// 得到所有的文件
				Iterator<FileItem> itr = items.iterator();
				while (itr.hasNext()) {// 依次处理每个文件
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						if (name.equals("billId"))
							billId = value;
						else if (name.equals("billType"))
							billType = value;
						else if (name.equals("billYear"))
							year = value;
						else if (name.equals("billMonth"))
							month = value;
						else if (name.equals("customerSelected")) {
							customerSelected = value;
							String[] parts = customerSelected.split("#");
							customerCode = parts[0];
							if (parts.length > 1)
								customerName = parts[1];
						} else if (name.equals("warehouseSelected")) {
							warehouseSelected = value;
							String[] parts = warehouseSelected.split("#");
							wareHouseCode = parts[0];
							if (parts.length > 1)
								wareHouseName = parts[1];
						} else if (name.equals("billAccount"))
							billAccount = value;
					}
				}
				BillInfo billInfo = billService.getBill(Long.parseLong(billId));
				itr = items.iterator();
				baseFile = new File(billService.baseDir, "/" + wareHouseCode + "/" + customerCode);
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (!item.isFormField()) {
						String name = item.getFieldName();
						String fileName = item.getName();
						if (StringUtils.isEmpty(fileName))
							continue;
						int index = fileName.lastIndexOf(".");
						String extend = "";
						if (index > 0)
							extend = fileName.substring(index);
						if (name.equals("summaryFile")) {
							summaryFile = new File(baseFile, "summaryFile" + extend);
							if (summaryFile.exists())
								summaryFile.delete();
							File parentFile = summaryFile.getParentFile();
							if (!parentFile.exists())
								parentFile.mkdirs();
							summaryFile.createNewFile();
							item.write(summaryFile);
						} else if (name.equals("detailFile")) {
							detailFile = new File(baseFile, "detailFile" + extend);
							if (detailFile.exists())
								detailFile.delete();
							File parentFile = detailFile.getParentFile();
							if (!parentFile.exists())
								parentFile.mkdirs();
							detailFile.createNewFile();
							item.write(detailFile);
						}
					}
				}

				billInfo.setBillAccount(Long.parseLong(billAccount));
				billInfo.setBillCustomerCode(customerCode);
				billInfo.setBillCustomerName(customerName);
				billInfo.setBillDate(year + month);
				if (detailFile != null)
					billInfo.setBillDetailPath("/" + wareHouseCode + "/" + customerCode + "/" + detailFile.getName());
				if (summaryFile != null)
					billInfo.setBillSummaryPath("/" + wareHouseCode + "/" + customerCode + "/" + summaryFile.getName());
				billInfo.setBillType(Integer.parseInt(billType));
				billInfo.setBillWarehouseCode(wareHouseCode);
				billInfo.setBillWarehouseName(wareHouseName);

				billService.updateBill(billInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/admin/admin-bill-manager.do";
	}

	@RequestMapping(value = "/admin/admin-add-bill.do")
	public String billAddBill(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ParseException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String billType = null;
		String year = null;
		String month = null;
		String customerSelected = null;
		String warehouseSelected = null;
		String billAccount = null;
		String customerCode = null;
		String customerName = null;
		String wareHouseCode = null;
		String wareHouseName = null;
		File summaryFile = null;
		File detailFile = null;
		File baseFile = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(req);// 得到所有的文件
				Iterator<FileItem> itr = items.iterator();
				while (itr.hasNext()) {// 依次处理每个文件
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						if (name.equals("billType"))
							billType = value;
						else if (name.equals("billYear"))
							year = value;
						else if (name.equals("billMonth"))
							month = value;
						else if (name.equals("customerSelected")) {
							customerSelected = value;
							String[] parts = customerSelected.split("#");
							customerCode = parts[0];
							if (parts.length > 1)
								customerName = parts[1];
						} else if (name.equals("warehouseSelected")) {
							warehouseSelected = value;
							String[] parts = warehouseSelected.split("#");
							wareHouseCode = parts[0];
							if (parts.length > 1)
								wareHouseName = parts[1];
						} else if (name.equals("billAccount"))
							billAccount = value;
					}
				}
				itr = items.iterator();
				baseFile = new File(billService.baseDir, "/" + wareHouseCode + "/" + customerCode);
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (!item.isFormField()) {
						String name = item.getFieldName();
						String fileName = item.getName();
						int index = fileName.lastIndexOf(".");
						String extend = "";
						if (index > 0)
							extend = fileName.substring(index);
						if (name.equals("summaryFile")) {
							summaryFile = new File(baseFile, "summaryFile" + extend);
							if (summaryFile.exists())
								summaryFile.delete();
							File parentFile = summaryFile.getParentFile();
							if (!parentFile.exists())
								parentFile.mkdirs();
							summaryFile.createNewFile();
							item.write(summaryFile);
						} else if (name.equals("detailFile")) {
							detailFile = new File(baseFile, "detailFile" + extend);
							if (detailFile.exists())
								detailFile.delete();
							File parentFile = detailFile.getParentFile();
							if (!parentFile.exists())
								parentFile.mkdirs();
							detailFile.createNewFile();
							item.write(detailFile);
						}
					}
				}

				BillInfo billInfo = new BillInfo();
				billInfo.setBillAccount(Long.parseLong(billAccount));
				billInfo.setBillCustomerCode(customerCode);
				billInfo.setBillCustomerName(customerName);
				billInfo.setBillDate(year + month);
				if (detailFile != null)
					billInfo.setBillDetailPath("/" + wareHouseCode + "/" + customerCode + "/" + detailFile.getName());
				else
					billInfo.setBillDetailPath("");
				if (summaryFile != null)
					billInfo.setBillSummaryPath("/" + wareHouseCode + "/" + customerCode + "/" + summaryFile.getName());
				else
					billInfo.setBillDetailPath("");
				billInfo.setBillType(Integer.parseInt(billType));
				billInfo.setBillWarehouseCode(wareHouseCode);
				billInfo.setBillWarehouseName(wareHouseName);

				billService.insertBill(billInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/admin/admin-bill-manager.do";
	}

	@RequestMapping(value = "/admin/admin-bill-update-view.do")
	public String billUpdateViewManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException,
			ParseException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";
		String billId = req.getParameter("billId");
		BillInfo billInfo = billService.getBill(Long.parseLong(billId));

		model.addAttribute("bill", billInfo);

		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		for (WareHouse wareHouse : wareHouses) {
			if (wareHouse.getWareHouseCode().equals(billInfo.getBillWarehouseCode())) {
				wareHouse.setChecked(Boolean.TRUE);
			}
		}
		List<Customer> customers = customerService.getAllCustomer();
		for (Customer customer : customers) {
			if (customer.getCustomerCode().equals(billInfo.getBillCustomerCode())) {
				customer.setChecked(Boolean.TRUE);
				break;
			}
		}
		List<BillType> billTypes = billTypeService.fillBills();
		for (BillType billType : billTypes) {
			if (billType.getBillType() == billInfo.getBillType()) {
				billType.setChecked(Boolean.TRUE);
				break;
			}
		}
		model.addAttribute("billTypes", billTypes);
		model.addAttribute("customers", customers);
		model.addAttribute("wareHouses", wareHouses);
		List<String> years = DateUtil.getYears();
		model.addAttribute("years", years);
		model.addAttribute("year", billInfo.getBillDate().substring(0, 4));
		List<String> months = DateUtil.getMonth();
		model.addAttribute("months", months);
		model.addAttribute("month", billInfo.getBillDate().substring(4));
		return "/admin/admin-bill-update";
	}

	@RequestMapping(value = "/admin/admin-bill-add-view.do")
	public String billAddViewManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException,
			ParseException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
		List<Customer> customers = customerService.getAllCustomer();
		List<BillType> billTypes = billTypeService.fillBills();
		model.addAttribute("billTypes", billTypes);
		model.addAttribute("customers", customers);
		model.addAttribute("wareHouses", wareHouses);
		List<String> years = DateUtil.getYears();
		model.addAttribute("years", years);
		List<String> months = DateUtil.getMonth();
		model.addAttribute("months", months);
		return "/admin/admin-bill-add";
	}

	@RequestMapping(value = "/admin/admin-bill-status-update.do")
	public String billStatusUpdateManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";

		String billId = req.getParameter("billId");
		BillInfo billInfo = billService.getBill(Long.parseLong(billId));
		billInfo.setBillStatus(2);
		billService.updateBillStatus(billInfo);
		model.addAllAttributes(req.getParameterMap());
		model.addAttribute("handler", Boolean.TRUE);
		return "redirect:/admin/admin-bill-manager.do";
	}

	@RequestMapping(value = "/admin/admin-bill-manager.do")
	public String billManager(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		if (!CommonUtils.checkAdminSessionTimeOut(req))
			return "redirect:/admin/admin-login.do";
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
		if (dateType == null)
			dateType = "4";
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
		if (StringUtils.isNotEmpty(warehouseSelected)) {
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

		String currentPage = req.getParameter("currentPage");
		int totalPage = 0;
		List<BillInfo> res = null;
		totalPage = billService.getTotalSize(customerSelected, warehouseSelected, Integer.parseInt(billType), startTime, endTime);

		if (StringUtils.isBlank(currentPage))
			currentPage = "1";
		Integer page = Integer.parseInt(currentPage);
		if (!(totalPage == 0 || page > totalPage)) {
			res = billService.fillBills(customerSelected, warehouseSelected, Integer.parseInt(billType), startTime, endTime,
					page - 1);
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
		return "/admin/bill-manager";
	}
}
