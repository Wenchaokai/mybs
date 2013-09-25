package com.best.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.best.Constants;
import com.best.domain.District;
import com.best.domain.OrderData;
import com.best.domain.SalesOrder;
import com.best.domain.ShippingOrderData;
import com.best.domain.Treasure;
import com.best.domain.WareHouse;

/**
 * ClassName:CommonUtils Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-28
 */
public class CommonUtils {
	private static BASE64Decoder decoder = new BASE64Decoder();
	private static BASE64Encoder encoder = new BASE64Encoder();
	private static String[] distincts = new String[] { "北京", "上海", "重庆市", "天津", "河北", "山西", "河南", "辽宁", "吉林省", "黑龙江", "内蒙古自治区",
			"江苏", "山东", "安徽", "浙江", "福建", "湖北", "湖南", "广东", "广西", "江西", "四川", "海南省", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏",
			"新疆", "台湾", "香港", "澳门" };

	public static Integer getSecurity(String[] securitys) {
		Integer userSecurity = 0;
		if (null != securitys) {
			for (String security : securitys) {
				userSecurity = userSecurity | Integer.parseInt(security);
			}
		}
		return userSecurity;
	}

	public static List<District> getDistricts() {
		List<District> districtList = new ArrayList<District>();
		for (int index = 0; index < distincts.length; index++) {
			District district = new District();
			district.setDistrictId(index);
			district.setDistrictName(distincts[index]);
			districtList.add(district);
		}
		return districtList;
	}

	public static String encoder(String plainText) {
		if (StringUtils.isBlank(plainText))
			return plainText;
		String encoderText = encoder.encode(encoder.encode(plainText.getBytes()).getBytes());
		return encoderText;
	}

	public static String decoder(String encoderText) throws IOException {
		if (StringUtils.isBlank(encoderText))
			return encoderText;
		byte[] decode = decoder.decodeBuffer(new String(decoder.decodeBuffer(encoderText)));
		return new String(decode);
	}

	public static Boolean checkAdminSessionTimeOut(HttpServletRequest request) throws IOException {
		Object obj = request.getSession().getAttribute(Constants.ADMIN_USER_TOKEN_IDENTIFY);
		if (null == obj)
			return Boolean.FALSE;
		return Boolean.TRUE;
	}

	public static Boolean checkSessionTimeOut(HttpServletRequest request) throws IOException {
		Object obj = request.getSession().getAttribute(Constants.USER_TOKEN_IDENTIFY);
		if (null == obj)
			return Boolean.FALSE;
		return Boolean.TRUE;
	}

	private static JSONObject createMonitorJSON(List<Treasure> treasures) {
		JSONObject style = new JSONObject();
		style.put("color", "#0080FF");
		JSONObject dataSet = new JSONObject();
		dataSet.put("name", "监控项目数");
		JSONArray values = new JSONArray();
		for (Treasure treasure : treasures) {
			values.add(treasure.getMonitorNums());
		}
		dataSet.put("values", values);
		dataSet.put("style", style);
		return dataSet;
	}

	private static JSONObject createAlarmJSON(List<Treasure> treasures) {
		JSONObject style = new JSONObject();
		style.put("color", "#FF7300");
		JSONObject dataSet = new JSONObject();
		dataSet.put("name", "报警项目数");
		JSONArray values = new JSONArray();
		for (Treasure treasure : treasures) {
			values.add(treasure.getAlarmNums());
		}
		dataSet.put("values", values);
		dataSet.put("style", style);
		return dataSet;
	}

	public static String createJSON(List<Treasure> treasures) {
		JSONArray dataSet = new JSONArray();
		dataSet.add(createMonitorJSON(treasures));
		dataSet.add(createAlarmJSON(treasures));

		JSONObject valueAxis = new JSONObject();
		valueAxis.put("name", "数量");
		valueAxis.put("unit", "个");

		JSONArray labels = new JSONArray();
		for (Treasure treasure : treasures) {
			labels.add(treasure.getDateTime());
		}

		JSONObject indexAxis = new JSONObject();
		indexAxis.put("name", "时间");
		indexAxis.put("unit", "");
		indexAxis.put("labels", labels);

		JSONObject data = new JSONObject();
		data.put("indexAxis", indexAxis);
		data.put("valueAxis", valueAxis);
		data.put("dataSets", dataSet);

		JSONObject res = new JSONObject();
		res.put("data", data);

		return res.toString();
	}

	public static void createIdoSkuExcelFile(String startTime, String endTime, Map<String, Map<String, Integer>> datas,
			Map<String, WareHouse> wareHouses, String baseDir, String key) throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		List<String> titles = new ArrayList<String>();
		titles.add("日期");
		List<String> codeOrder = new ArrayList<String>();
		for (WareHouse wareHouse : wareHouses.values()) {
			titles.add(wareHouse.getWareHouseName() + "订单量");
			titles.add(wareHouse.getWareHouseName() + "订单量占比");
			codeOrder.add(wareHouse.getWareHouseCode());
		}
		titles.add("合计");

		try {
			// t.xls为要新建的文件名
			File parentFile = new File(baseDir, "/monitor/dingdang/");
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, key + ".xls");
			if (file.exists())
				file.delete();
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);

			// 写入内容
			for (int i = 0; i < titles.size(); i++)
				sheet.addCell(new Label(i, 0, titles.get(i)));

			for (int i = 0; i < dateTimes.size(); i++) {
				String dateTime = dateTimes.get(dateTimes.size() - i - 1);
				sheet.addCell(new Label(0, i + 1, dateTime));
				Map<String, Integer> numCountsMap = datas.get(dateTime);
				if (numCountsMap != null) {
					int totalCount = 0;
					for (Integer num : numCountsMap.values()) {
						if (num == null)
							num = 0;
						totalCount += num;
					}
					int index = 1;
					for (int j = 0; j < codeOrder.size(); j++) {
						Integer idoNum = numCountsMap.get(codeOrder.get(j));
						if (null == idoNum)
							idoNum = 0;
						double app = 0;
						if (idoNum != 0 && totalCount != 0) {
							app = (idoNum + 0.0) / totalCount * 100;
						}
						sheet.addCell(new Label(index++, i + 1, idoNum + ""));
						sheet.addCell(new Label(index++, i + 1, app + "%"));
					}
					sheet.addCell(new Label(index++, i + 1, totalCount + ""));
				} else {
					int totalCount = 0;
					int index = 1;
					for (int j = 0; j < codeOrder.size(); j++) {
						Integer idoNum = 0;
						double app = 0;
						sheet.addCell(new Label(index++, i + 1, idoNum + ""));
						sheet.addCell(new Label(index++, i + 1, app + "%"));
					}
					sheet.addCell(new Label(index++, i + 1, totalCount + ""));
				}
			}

			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createIdoSkuJSON(String startTime, String endTime, Map<String, Map<String, Integer>> datas,
			Map<String, WareHouse> wareHouses, String title, int showSize, String colors) throws ParseException {
		String[] COLOR = colors.split(",");
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);

		JSONArray labels = new JSONArray();
		for (String dateTime : dateTimes) {
			labels.add(dateTime);
		}

		JSONObject indexAxis = new JSONObject();
		indexAxis.put("name", "时间");
		indexAxis.put("unit", "");
		indexAxis.put("labels", labels);

		JSONObject valueAxis = new JSONObject();
		valueAxis.put("name", "数量");
		valueAxis.put("unit", "个");

		JSONArray dataSet = new JSONArray();

		int index = 0;
		for (Entry<String, Map<String, Integer>> entry : datas.entrySet()) {
			String wareHouseCode = entry.getKey();
			WareHouse wareHouse = wareHouses.get(wareHouseCode);
			if (wareHouse == null)
				continue;
			String color = COLOR[index];
			JSONObject style = new JSONObject();
			style.put("color", color);
			JSONObject data = new JSONObject();
			data.put("name", wareHouse.getWareHouseName() + title);
			JSONArray values = new JSONArray();
			for (String dateTime : dateTimes) {
				Integer numCount = entry.getValue().get(dateTime);
				if (numCount == null)
					numCount = 0;
				values.add(numCount);
			}
			data.put("values", values);
			data.put("style", style);

			dataSet.add(data);

			index++;

			if (index >= showSize)
				break;
		}

		JSONObject data = new JSONObject();
		data.put("indexAxis", indexAxis);
		data.put("valueAxis", valueAxis);
		data.put("dataSets", dataSet);

		JSONObject res = new JSONObject();
		res.put("data", data);

		return res.toString();
	}

	static class Color {
		double number;
		String color;

		public Color(double number, String color) {
			this.number = number;
			this.color = color;
		}
	}

	static List<Color> colorList = new ArrayList<CommonUtils.Color>();

	static {
		colorList.add(new Color(0.005, "#FFFFB2"));
		colorList.add(new Color(0.02, "#FECC5C"));
		colorList.add(new Color(0.05, "#FD8D3C"));
		colorList.add(new Color(0.08, "#F03B20"));
		colorList.add(new Color(1, "#BD0026"));
	}

	public static String createIdoDisSkuJSON(String startTime, String endTime, Map<String, Integer> provinceMap) {
		int totalCount = 0;
		Map<String, Integer> mapCount = new HashMap<String, Integer>();
		for (String province : distincts) {
			mapCount.put(province, 0);
		}
		for (Map.Entry<String, Integer> entry : provinceMap.entrySet()) {
			String province = entry.getKey();
			Integer numCount = entry.getValue();
			totalCount += numCount;
			for (String element : distincts) {
				if (province.startsWith(element)) {
					Integer count = mapCount.get(element);
					count += numCount;
					mapCount.put(element, count);
					break;
				}
			}
		}

		JSONArray data = new JSONArray();
		for (String element : distincts) {
			Integer count = mapCount.get(element);
			double percent = 0;
			if (totalCount != 0)
				percent = (count + 0.0) / totalCount;
			String colorPatten = "";
			for (Color color : colorList) {
				if (percent <= color.number) {
					colorPatten = color.color;
					break;
				}
			}
			data.add(element + "-" + colorPatten + "-" + count + "-" + percent * 100);
		}

		JSONObject res = new JSONObject();
		res.put("data", data);
		res.put("success", true);
		return res.toString();
	}

	public static void createIdoDisSkuExcelFile(String startTime, String endTime, Map<String, Map<String, Integer>> dateMap,
			String baseDir, String key) throws ParseException {

		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		List<String> titles = new ArrayList<String>();
		titles.add("日期");
		for (String province : distincts) {
			titles.add(province);
		}
		titles.add("合计(单)");

		try {
			// t.xls为要新建的文件名
			File parentFile = new File(baseDir, "/monitor/dingdang/");
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, key + ".xls");
			if (file.exists())
				file.delete();
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);

			// 写入内容
			for (int i = 0; i < titles.size(); i++)
				sheet.addCell(new Label(i, 0, titles.get(i)));

			for (int i = 0; i < dateTimes.size(); i++) {
				String dateTime = dateTimes.get(dateTimes.size() - i - 1);
				sheet.addCell(new Label(0, i + 1, dateTime));
				Map<String, Integer> numCountsMap = dateMap.get(dateTime);

				int totalCount = 0;
				Map<String, Integer> mapCount = new HashMap<String, Integer>();
				for (String province : distincts) {
					mapCount.put(province, 0);
				}
				if (null != numCountsMap) {
					for (Map.Entry<String, Integer> entry : numCountsMap.entrySet()) {
						String province = entry.getKey();
						Integer numCount = entry.getValue();
						totalCount += numCount;
						for (String element : distincts) {
							if (province.startsWith(element)) {
								Integer count = mapCount.get(element);
								count += numCount;
								mapCount.put(element, count);
								break;
							}
						}
					}
				}

				int index = 1;
				for (String element : distincts) {
					Integer count = mapCount.get(element);
					sheet.addCell(new Label(index++, i + 1, count + ""));
				}
				sheet.addCell(new Label(index++, i + 1, totalCount + ""));

			}

			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String createIdoSkuPercentJSON(String startTime, String endTime, Map<String, Map<String, Double>> percentMap,
			Map<Double, String> skuPercentMap, String title, Integer showSize, String colors) throws ParseException {
		// percentMap key是DateTime，value是<skucode,percent>
		String[] COLOR = colors.split(",");
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);

		JSONArray labels = new JSONArray();
		for (String dateTime : dateTimes) {
			labels.add(dateTime);
		}

		JSONObject indexAxis = new JSONObject();
		indexAxis.put("name", "时间");
		indexAxis.put("unit", "");
		indexAxis.put("labels", labels);

		JSONObject valueAxis = new JSONObject();
		valueAxis.put("name", "数量");
		valueAxis.put("unit", "占比%");

		JSONArray dataSet = new JSONArray();

		int index = 0;
		Map<String, Map<String, Double>> skuCodeMap = new HashMap<String, Map<String, Double>>();
		// key是skucode，value是<DayTIME,percent>

		for (Entry<String, Map<String, Double>> entry : percentMap.entrySet()) {
			String dateTime = entry.getKey();
			Map<String, Double> value = entry.getValue();
			for (Entry<String, Double> tempEntry : value.entrySet()) {
				Map<String, Double> temp = skuCodeMap.get(tempEntry.getKey());
				if (null == temp) {
					temp = new HashMap<String, Double>();
					skuCodeMap.put(tempEntry.getKey(), temp);
				}
				temp.put(dateTime, tempEntry.getValue());
			}
		}

		for (Entry<Double, String> orderPercent : skuPercentMap.entrySet()) {
			index++;
			String skuCode = orderPercent.getValue();
			if (index >= showSize)
				break;
			Map<String, Double> datePercent = skuCodeMap.get(skuCode);

			String color = COLOR[index];
			JSONObject style = new JSONObject();
			style.put("color", color);
			JSONObject data = new JSONObject();
			data.put("name", skuCode + title);
			JSONArray values = new JSONArray();
			for (String dateTime : dateTimes) {
				Double numCount = datePercent.get(dateTime);
				if (numCount == null)
					numCount = 0.0;
				values.add(numCount);
			}
			data.put("values", values);
			data.put("style", style);

			dataSet.add(data);

		}

		JSONObject data = new JSONObject();
		data.put("indexAxis", indexAxis);
		data.put("valueAxis", valueAxis);
		data.put("dataSets", dataSet);

		JSONObject res = new JSONObject();
		res.put("data", data);

		return res.toString();
	}

	public static void createIdoSkuPerExcelFile(String startTime, String endTime, Map<String, Map<String, Double>> percentMap,
			Map<Double, String> skuPercentMap, String baseDir, String excelFileName) throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		List<String> titles = new ArrayList<String>();
		titles.add("日期");
		for (String skuCode : skuPercentMap.values()) {
			titles.add(skuCode + "占比");
		}

		try {
			// t.xls为要新建的文件名
			File parentFile = new File(baseDir, "/monitor/dingdang/");
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, excelFileName + ".xls");
			if (file.exists())
				file.delete();
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);

			// 写入内容
			for (int i = 0; i < titles.size(); i++)
				sheet.addCell(new Label(i, 0, titles.get(i)));

			for (int i = 0; i < dateTimes.size(); i++) {
				String dateTime = dateTimes.get(dateTimes.size() - i - 1);
				sheet.addCell(new Label(0, i + 1, dateTime));

				Map<String, Double> percents = percentMap.get(dateTime);
				int index = 1;
				for (String skuCode : skuPercentMap.values()) {
					if (null != percents) {
						Double zhanbi = percents.get(skuCode);
						if (zhanbi == null)
							zhanbi = 0.0;
						sheet.addCell(new Label(index++, i + 1, zhanbi + "%"));
					} else {
						sheet.addCell(new Label(index++, i + 1, "0%"));
					}
				}

			}

			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createCarrierDistributedJSON(String startTime, String endTime,
			Map<String, Map<String, Integer>> dataMap, Integer showSize) throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);

		JSONArray labels = new JSONArray();
		for (String dateTime : dateTimes) {
			labels.add(dateTime);
		}

		JSONArray dataSet = new JSONArray();

		int index = 0;

		for (Entry<String, Map<String, Integer>> entry : dataMap.entrySet()) {
			String carrierName = entry.getKey();

			JSONObject data = new JSONObject();
			data.put("name", carrierName + "揽件量");
			JSONArray values = new JSONArray();
			for (String dateTime : dateTimes) {
				Integer numCount = entry.getValue().get(dateTime);
				if (numCount == null)
					numCount = 0;
				values.add(numCount);
			}
			data.put("data", values);

			dataSet.add(data);

			index++;

			if (index >= showSize)
				break;
		}

		if (index == 0) {
			JSONObject data = new JSONObject();
			data.put("name", "揽件量");
			JSONArray values = new JSONArray();
			for (String dateTime : dateTimes) {
				values.add(0);
			}
			data.put("data", values);

			dataSet.add(data);
		}

		JSONObject res = new JSONObject();
		res.put("success", true);
		res.put("type", "column");
		res.put("title", "承运商订单分布");
		res.put("yAxis", "订单量 (单)");
		res.put("categories", labels);
		res.put("series", dataSet);

		return res.toString();
	}

	public static void createCarrierDistributedExcelFile(String startTime, String endTime,
			Map<String, Map<String, Integer>> dataMap, Map<String, Map<String, Integer>> dateMap, String baseDir, String key)
			throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		List<String> titles = new ArrayList<String>();
		titles.add("日期");
		for (String code : dataMap.keySet()) {
			titles.add(code + "揽件量");
		}

		try {
			// t.xls为要新建的文件名
			File parentFile = new File(baseDir, "/monitor/dingdang/");
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, key + ".xls");
			if (file.exists())
				file.delete();
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);

			// 写入内容
			for (int i = 0; i < titles.size(); i++)
				sheet.addCell(new Label(i, 0, titles.get(i)));

			for (int i = 0; i < dateTimes.size(); i++) {
				String dateTime = dateTimes.get(dateTimes.size() - i - 1);
				sheet.addCell(new Label(0, i + 1, dateTime));

				Map<String, Integer> percents = dateMap.get(dateTime);
				int index = 1;
				for (String skuCode : dataMap.keySet()) {
					if (null != percents) {
						Integer zhanbi = percents.get(skuCode);
						if (zhanbi == null)
							zhanbi = 0;
						sheet.addCell(new Label(index++, i + 1, zhanbi + ""));
					} else {
						sheet.addCell(new Label(index++, i + 1, "0"));
					}
				}

			}

			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createCarrierPromiseJSON(String startTime, String endTime, Map<String, Map<String, Integer>> dataMap,
			Integer showSize) throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);

		JSONArray labels = new JSONArray();
		for (String dateTime : dateTimes) {
			labels.add(dateTime);
		}

		JSONArray dataSet = new JSONArray();

		int index = 0;

		for (Entry<String, Map<String, Integer>> entry : dataMap.entrySet()) {
			String carrierName = entry.getKey();

			JSONObject data = new JSONObject();
			String title = "次日达（发货之后，第二天签收）";
			if (carrierName.equals("2"))
				title = "隔日达（发货之后，第三天签收）";
			else if (carrierName.equals("1"))
				title = "三日达（发货之后，第四天签收）";
			data.put("name", title);
			JSONArray values = new JSONArray();
			for (String dateTime : dateTimes) {
				Integer numCount = entry.getValue().get(dateTime);
				if (numCount == null)
					numCount = 0;
				values.add(numCount);
			}
			data.put("data", values);

			dataSet.add(data);

			index++;

			if (index >= showSize)
				break;
		}

		if (index == 0) {
			for (int i = 1; i <= 3; i++) {
				String title = "次日达（发货之后，第二天签收）";
				if (i == 2)
					title = "隔日达（发货之后，第三天签收）";
				else if (i == 3)
					title = "三日达（发货之后，第四天签收）";
				JSONObject data = new JSONObject();
				data.put("name", title);
				JSONArray values = new JSONArray();
				for (String dateTime : dateTimes) {
					values.add(0);
				}
				data.put("data", values);
				dataSet.add(data);
			}

		}

		JSONObject res = new JSONObject();
		res.put("success", true);
		res.put("type", "column");
		res.put("title", "各承运商的承诺时效签收情况");
		res.put("yAxis", "订单量 (单)");
		res.put("categories", labels);
		res.put("series", dataSet);

		return res.toString();
	}

	public static void createCarrierPromiseExcelFile(String startTime, String endTime, Map<String, Map<String, Integer>> dataMap,
			Map<String, Map<String, Integer>> dateMap, String baseDir, String key) throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		List<String> titles = new ArrayList<String>();
		titles.add("日期");
		for (String code : dataMap.keySet()) {
			String title = "次日达（发货之后，第二天签收）";
			if (code.equals("2"))
				title = "隔日达（发货之后，第三天签收）";
			else if (code.equals("1"))
				title = "三日达（发货之后，第四天签收）";
			titles.add(title);
		}

		try {
			// t.xls为要新建的文件名
			File parentFile = new File(baseDir, "/monitor/dingdang/");
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, key + ".xls");
			if (file.exists())
				file.delete();
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("第一页", 0);

			// 写入内容
			for (int i = 0; i < titles.size(); i++)
				sheet.addCell(new Label(i, 0, titles.get(i)));

			for (int i = 0; i < dateTimes.size(); i++) {
				String dateTime = dateTimes.get(dateTimes.size() - i - 1);
				sheet.addCell(new Label(0, i + 1, dateTime));

				Map<String, Integer> percents = dateMap.get(dateTime);
				int index = 1;
				for (String skuCode : dataMap.keySet()) {
					if (null != percents) {
						Integer zhanbi = percents.get(skuCode);
						if (zhanbi == null)
							zhanbi = 0;
						sheet.addCell(new Label(index++, i + 1, zhanbi + ""));
					} else {
						sheet.addCell(new Label(index++, i + 1, "0"));
					}
				}

			}

			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createOrderDataJson(OrderData currentDateOrder, OrderData orderData, OrderData preOrderData) {

		JSONObject res = new JSONObject();
		res.put("success", true);
		JSONArray labels = new JSONArray();
		labels.add("近1天");
		labels.add(orderData.getMonth());
		labels.add("与上个月/" + preOrderData.getMonth() + "对比");
		res.put("label", labels);
		JSONArray values = new JSONArray();
		// 总订单量
		JSONArray orderSum = new JSONArray();
		orderSum.add("总订单量(单)");
		orderSum.add(currentDateOrder.getOrderSum() + "");
		orderSum.add(orderData.getOrderSum() + "");
		JSONObject status = new JSONObject();
		int dis = orderData.getOrderSum() - preOrderData.getOrderSum();
		String ss = "up";
		if (dis < 0)
			ss = "down";
		status.put("state", ss);
		if (preOrderData.getOrderSum() == 0) {
			if (orderData.getOrderSum() == 0)
				status.put("num", "0%");
			else
				status.put("num", "100%");
		} else
			status.put("num", (((int) ((dis + 0.0) / preOrderData.getOrderSum() * 10000)) + 0.0) / 100 + "%");
		orderSum.add(status);
		values.add(orderSum);

		// 总销售额
		JSONArray totalMount = new JSONArray();
		totalMount.add("销售额(元)");
		totalMount.add(currentDateOrder.getTotalMount() + "");
		totalMount.add(orderData.getTotalMount() + "");
		status = new JSONObject();
		dis = orderData.getTotalMount() - preOrderData.getTotalMount();
		ss = "up";
		if (dis < 0)
			ss = "down";
		status.put("state", ss);
		if (preOrderData.getTotalMount() == 0) {
			if (orderData.getTotalMount() == 0)
				status.put("num", "0%");
			else
				status.put("num", "100%");
		} else
			status.put("num", (((int) ((dis + 0.0) / preOrderData.getTotalMount() * 10000)) + 0.0) / 100 + "%");
		totalMount.add(status);
		values.add(totalMount);

		// 发货量(单)
		JSONArray delevered = new JSONArray();
		delevered.add("发货量(单)");
		delevered.add(currentDateOrder.getDeliveredCount() + "");
		delevered.add(orderData.getDeliveredCount() + "");
		status = new JSONObject();
		dis = orderData.getDeliveredCount() - preOrderData.getDeliveredCount();
		ss = "up";
		if (dis < 0)
			ss = "down";
		status.put("state", ss);
		if (preOrderData.getDeliveredCount() == 0) {
			if (orderData.getDeliveredCount() == 0)
				status.put("num", "0%");
			else
				status.put("num", "100%");
		} else
			status.put("num", (((int) ((dis + 0.0) / preOrderData.getDeliveredCount() * 10000)) + 0.0) / 100 + "%");
		delevered.add(status);
		values.add(delevered);

		// 取消订单量(单)
		JSONArray cancel = new JSONArray();
		cancel.add("取消订单量(单)");
		cancel.add(currentDateOrder.getCancelCount() + "");
		cancel.add(orderData.getCancelCount() + "");
		status = new JSONObject();
		dis = orderData.getCancelCount() - preOrderData.getCancelCount();
		ss = "up";
		if (dis < 0)
			ss = "down";
		status.put("state", ss);
		if (preOrderData.getCancelCount() == 0) {
			if (orderData.getCancelCount() == 0)
				status.put("num", "0%");
			else
				status.put("num", "100%");
		} else
			status.put("num", (((int) ((dis + 0.0) / preOrderData.getCancelCount() * 10000)) + 0.0) / 100 + "%");
		cancel.add(status);
		values.add(cancel);

		// COD订单量(单)
		JSONArray cod = new JSONArray();
		cod.add("COD订单量(单)");
		cod.add(currentDateOrder.getPaymentCount() + "");
		cod.add(orderData.getPaymentCount() + "");
		status = new JSONObject();
		dis = orderData.getPaymentCount() - preOrderData.getPaymentCount();
		ss = "up";
		if (dis < 0)
			ss = "down";
		status.put("state", ss);
		if (preOrderData.getPaymentCount() == 0) {
			if (orderData.getPaymentCount() == 0)
				status.put("num", "0%");
			else
				status.put("num", "100%");
		} else
			status.put("num", (((int) ((dis + 0.0) / preOrderData.getPaymentCount() * 10000)) + 0.0) / 100 + "%");
		cod.add(status);
		values.add(cod);

		res.put("data", values);

		return res.toString();
	}

	public static String createDayOrderDataJson(List<OrderData> orderDatas, String startTime, String endTime)
			throws ParseException {
		List<String> dateTimes = DateUtil.getDateTimes(startTime, endTime);
		Map<String, OrderData> map = new HashMap<String, OrderData>();
		for (OrderData orderData : orderDatas) {
			map.put(orderData.getMonth(), orderData);
		}
		JSONArray orderSum = new JSONArray();
		JSONArray totalMount = new JSONArray();
		JSONArray delivered = new JSONArray();
		JSONArray canceled = new JSONArray();
		JSONArray paymentCount = new JSONArray();

		JSONArray labels = new JSONArray();
		for (String dateTime : dateTimes) {
			labels.add(dateTime);
			OrderData orderData = map.get(dateTime);
			if (null != orderData) {
				orderSum.add(orderData.getOrderSum());
				totalMount.add(orderData.getTotalMount());
				delivered.add(orderData.getDeliveredCount());
				canceled.add(orderData.getCancelCount());
				paymentCount.add(orderData.getPaymentCount());
			} else {
				orderSum.add(0);
				totalMount.add(0);
				delivered.add(0);
				canceled.add(0);
				paymentCount.add(0);
			}
		}
		JSONObject orderSumObj = new JSONObject();
		orderSumObj.put("name", "总订单量(单)");
		orderSumObj.put("data", orderSum);

		JSONObject totalMountObj = new JSONObject();
		totalMountObj.put("name", "销售额(元)");
		totalMountObj.put("data", totalMount);

		JSONObject deliveredObj = new JSONObject();
		deliveredObj.put("name", "发货量(单)");
		deliveredObj.put("data", delivered);

		JSONObject canceledObj = new JSONObject();
		canceledObj.put("name", "取消订单量(单)");
		canceledObj.put("data", canceled);

		JSONObject paymentCountObj = new JSONObject();
		paymentCountObj.put("name", "COD订单量(单)");
		paymentCountObj.put("data", paymentCount);

		JSONArray dataSet = new JSONArray();
		dataSet.add(orderSumObj);
		dataSet.add(totalMountObj);
		dataSet.add(deliveredObj);
		dataSet.add(canceledObj);
		dataSet.add(paymentCountObj);

		JSONObject res = new JSONObject();
		res.put("success", true);
		res.put("type", "area");
		res.put("title", "走势图");
		res.put("yAxis", "");
		res.put("categories", labels);
		res.put("series", dataSet);

		return res.toString();
	}

	public static int caculateHashCode(List<Object> list) {
		int hashCode = 1;
		Iterator<Object> i = list.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}

	public static void createShippingOrderDataExcelFile(String userCount, String key, List<ShippingOrderData> res,
			String baseDir, Model model) throws IOException, RowsExceededException, WriteException {
		File parentFile = new File(baseDir, userCount);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		File file = new File(parentFile, key + ".xls");
		if (file.exists()) {
			model.addAttribute("excelFilePath", "/json/" + userCount + "/" + file.getName());
			return;
		}

		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成名为“第一页”的工作表，参数0表示这是第一页
		WritableSheet sheet = book.createSheet("第一页", 0);
		String[] titles = new String[] { "销售平台订单号", "仓库", "收货人姓名", "承运商", "收货人手机", "运单号", "发货时间", "订单状态" };
		// 写入内容
		for (int i = 0; i < titles.length; i++)
			sheet.addCell(new Label(i, 0, titles[i]));

		for (int index = 1; index < res.size() + 1; index++) {
			ShippingOrderData shippingOrderData = res.get(index - 1);
			sheet.addCell(new Label(0, index, shippingOrderData.getRelationNo()));
			sheet.addCell(new Label(1, index, shippingOrderData.getWareHouseName()));
			sheet.addCell(new Label(2, index, shippingOrderData.getConsigneeName()));
			sheet.addCell(new Label(3, index, shippingOrderData.getLogisticsProviderName()));
			sheet.addCell(new Label(4, index, shippingOrderData.getConsigneePhone()));
			sheet.addCell(new Label(5, index, shippingOrderData.getShippingOrderNo()));
			sheet.addCell(new Label(6, index, shippingOrderData.getShippingTime()));
			sheet.addCell(new Label(7, index, shippingOrderData.getStatusName()));
		}

		// 写入数据
		book.write();
		// 关闭文件
		book.close();

		model.addAttribute("excelFilePath", "/json/" + userCount + "/" + file.getName());
	}

	public static String getWMSStatus(String status) {
		if ("WMS_DRAFT".equalsIgnoreCase(status))
			return "待审核";
		else if ("WMS_ACCEPTED".equalsIgnoreCase(status))
			return "仓库已接单";
		else if ("WMS_ACCEPTED_FAIL".equalsIgnoreCase(status))
			return "仓库接单失败";
		else if ("WMS_DELIVERED".equalsIgnoreCase(status))
			return "仓库已发货";
		else if ("WMS_CANCELED".equalsIgnoreCase(status))
			return "订单取消";
		else if ("WMS_CLOSED".equalsIgnoreCase(status))
			return "订单关闭";
		else if ("WMS_PRINTED".equalsIgnoreCase(status) || "WMS_PICKUPED".equalsIgnoreCase(status)
				|| "WMS_CHECKED".equalsIgnoreCase(status) || "WMS_PACKAGED".equalsIgnoreCase(status)
				|| "WMS_WEIGHTED".equalsIgnoreCase(status))
			return "订单生产中";
		return "未知状态";

	}

	public static void convertStatus(SalesOrder salesOrder) {
		if (null == salesOrder)
			return;
		if ("draft".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("待审核");
		else if ("accepted".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("已接单");
		else if ("accepted".equalsIgnoreCase(salesOrder.getStatus())
				&& "fail".equalsIgnoreCase(salesOrder.getAcceptFailNotifyStatus()))
			salesOrder.setStatusName("接单失败");
		else if ("delivering".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("发货中");
		else if ("delivered".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("已发货");
		else if ("delivered".equalsIgnoreCase(salesOrder.getStatus())
				&& "success".equalsIgnoreCase(salesOrder.getDeliveredNotiifyStatus()))
			salesOrder.setStatusName("发货完成");
		else if ("close".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("已关闭");
		else if ("cancled".equalsIgnoreCase(salesOrder.getStatus()))
			salesOrder.setStatusName("已取消 ");
		else if ("close".equalsIgnoreCase(salesOrder.getStatus())
				&& "success".equalsIgnoreCase(salesOrder.getCanceledNotifyStatus()))
			salesOrder.setStatusName("已关闭");

	}
}
