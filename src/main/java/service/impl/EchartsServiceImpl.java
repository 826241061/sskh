package service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import service.EchartsService;

@Service
public class EchartsServiceImpl implements EchartsService {

	@Override
	public Map<String, Object> getExcelPosition() {
		String excelPath = "C:\\Users\\00255\\Documents\\WeChat Files\\dece____\\FileStorage\\File\\2019-10\\一维数值聚类算法.xlsx";
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		Map<String, Object> m = new HashMap<String, Object>();

		try {
			// String encoding = "GBK";
			File excel = new File(excelPath);
			if (excel.isFile() && excel.exists()) { // 判断文件是否存在

				String[] split = excel.getName().split("\\."); // .是特殊字符，需要转义！！！！！
				Workbook wb;
				// 根据文件后缀（xls/xlsx）进行判断
				if ("xls".equals(split[1])) {
					FileInputStream fis = new FileInputStream(excel); // 文件流对象
					wb = new HSSFWorkbook(fis);
				} else if ("xlsx".equals(split[1])) {
					wb = new XSSFWorkbook(excel);
				} else {
					System.out.println("文件类型错误!");
					return null;
				}

				// 开始解析
				XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0); // 读取sheet 0

				int firstRowIndex = sheet.getFirstRowNum() + 1; // 第一行是列名，所以不读
				int lastRowIndex = sheet.getLastRowNum();

				Row firstrow = sheet.getRow(firstRowIndex);
				Cell c = (Cell) firstrow.getCell(0);
				ArrayList<Double> first = new ArrayList<Double>();
				first.add(Double.parseDouble(c.toString()));
				first.add(1.0);
				data.add(first);

				for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) { // 遍历行

					Row row = sheet.getRow(rIndex);
					if (row != null) {
						int firstCellIndex = row.getFirstCellNum();

						ArrayList<Double> current = new ArrayList<Double>();

						Cell cell = (Cell) row.getCell(firstCellIndex);

						double point = Double.parseDouble(cell.toString());
						ArrayList<Double> lastdata = data.get(data.size() - 1);
						if (lastdata.get(0) == point) {
							lastdata.set(1, lastdata.get(1) + 1);
						} else {
							current.add(point);
							current.add(1.0);
							data.add(current);
						}

					}
				}

				m.put("xcount", data);// data中[数值,个数]

			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (Map<String, Object>) m;
	}

	@Override
	public Map<String, Object> getClassifier(Integer division, Integer isDistinct) {// isDistinct==1差值去重，isDistinct==0差值不去重

		// 精度控制
		DecimalFormat df = new DecimalFormat();
		String style = "0.#####";// 定义要显示的数字的格式
		df.applyPattern(style);// 将格式应用于格式化器

		Map<String, Object> m = new HashMap<String, Object>();

		ArrayList<Double> nums = new ArrayList<Double>();// 获取所有的数

		String excelPath = "C:\\Users\\00255\\Documents\\WeChat Files\\dece____\\FileStorage\\File\\2019-10\\一维数值聚类算法.xlsx";

		try {
			// String encoding = "GBK";
			File excel = new File(excelPath);
			if (excel.isFile() && excel.exists()) { // 判断文件是否存在

				String[] split = excel.getName().split("\\."); // .是特殊字符，需要转义！！！！！
				Workbook wb;
				// 根据文件后缀（xls/xlsx）进行判断
				if ("xls".equals(split[1])) {
					FileInputStream fis = new FileInputStream(excel); // 文件流对象
					wb = new HSSFWorkbook(fis);
				} else if ("xlsx".equals(split[1])) {
					wb = new XSSFWorkbook(excel);
				} else {
					System.out.println("文件类型错误!");
					return null;
				}

				// 开始解析
				XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0); // 读取sheet 0

				int firstRowIndex = sheet.getFirstRowNum() + 1; // 第一行是列名，所以不读
				int lastRowIndex = sheet.getLastRowNum();

				for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) { // 遍历行
					// System.out.println("rIndex: " + rIndex);
					Row row = sheet.getRow(rIndex);
					if (row != null) {
						int firstCellIndex = row.getFirstCellNum();
						int lastCellIndex = row.getLastCellNum();

						Cell cell = (Cell) row.getCell(firstCellIndex);
						if (cell != null) {
							nums.add(Double.parseDouble(cell.toString()));
						}

					}
				}
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<Double> ds = new ArrayList<Double>();// 差值
		ArrayList<Classifier> classes = new ArrayList<Classifier>();// 所有聚类

		double d;
		ds.add(Double.parseDouble(df.format(nums.get(1) - nums.get(0))));
		int sizeOfNums = nums.size();
		if (isDistinct == 1) {
			for (int i = 2; i < sizeOfNums; i++) {// 将差值d放入ArrayList
				d = (Double.parseDouble(df.format(nums.get(i) - nums.get(i - 1))));
				if (d < ds.get(0))
					ds.add(0, d);
				else if (d > ds.get(ds.size() - 1))
					ds.add(d);
				else {
					for (int j = 1; d > ds.get(j - 1) && j < ds.size(); j++) {
						if (d > ds.get(j - 1) && d < ds.get(j))
							ds.add(j, d);
					}
				}
			}
		} else {
			for (int i = 2; i < sizeOfNums; i++) {// 将差值d放入ArrayList
				d = (Double.parseDouble(df.format(nums.get(i) - nums.get(i - 1))));
				if (d > 0) {
					if (d <= ds.get(0))
						ds.add(0, d);
					else if (d >= ds.get(ds.size() - 1))
						ds.add(d);
					else {
						for (int j = 1; d >= ds.get(j - 1) && j < ds.size(); j++) {
							if (d > ds.get(j - 1) && d < ds.get(j))
								ds.add(j, d);
						}
					}
				}
			}
		}
		if (ds.get(0) == 0)
			ds.remove(0);

		double decimal = division / 1000.0;
		if (decimal > 0.5)
			decimal = 0.5;
		if (decimal < 0)
			decimal = 0;
		int numOfIndex = (int) ((ds.size() - 1) * decimal);
		double numOfK;

		numOfK = ds.get(numOfIndex);

		m.put("numOfK", df.format(numOfK));
		classes.add(new Classifier(nums.get(0)));

		for (int i = 1; i < sizeOfNums; i++) {
			double numi = nums.get(i);
			if (nums.get(i) - nums.get(i - 1) > numOfK) {
				classes.add(new Classifier(numi));
			} else {
				Classifier lastclass = classes.get(classes.size() - 1);

				double distance = numi - lastclass.classifier_cell.get(lastclass.classifier_cell.size() - 1);
				if (distance > 0) {
					if (lastclass.min_distance == 0 || distance < lastclass.min_distance)
						lastclass.min_distance = distance;
				}
				if (distance > lastclass.max_distance)
					lastclass.max_distance = distance;

				lastclass.classifier_cell.add(numi);
				lastclass.max = numi;
			}
		}
		int sizeOfClasses = classes.size();
		// System.out.println("类的数量："+classes.size());
		ArrayList<Object> mid = new ArrayList<Object>();
		ArrayList<Object> min = new ArrayList<Object>();
		ArrayList<Object> max = new ArrayList<Object>();
		ArrayList<Object> count = new ArrayList<Object>();
		ArrayList<Object> difference_MaxMin = new ArrayList<Object>();
		ArrayList<Object> min_distance = new ArrayList<Object>();
		ArrayList<Object> max_distance = new ArrayList<Object>();
		ArrayList<Object> standard_Deviation = new ArrayList<Object>();
		for (int i = 0; i < sizeOfClasses; i++) {

			if (classes.get(i).classifier_cell.size() > 1) {
				Classifier currentclass = classes.get(i);
				mid.add(df.format((currentclass.max + currentclass.min) / 2));// 中值
				min.add(df.format(currentclass.min));// 最小值
				max.add(df.format(currentclass.max));// 最大值
				count.add(currentclass.classifier_cell.size());// count
				difference_MaxMin.add(df.format(currentclass.max - currentclass.min));// 最大值减最小值
				min_distance.add(df.format(currentclass.min_distance));// 相邻两点最小值
				max_distance.add(df.format(currentclass.max_distance));// 相邻两点最大值

				// 计算标准差
				int numOfCell = currentclass.classifier_cell.size();
				if (numOfCell == 1) {
					standard_Deviation.add(0);
				} else {

					double sum = 0;
					double nvariance = 0;
					for (int j = 0; j < numOfCell; j++) {
						sum += currentclass.classifier_cell.get(j);
					}
					double average = sum / numOfCell;
					for (int j = 0; j < numOfCell; j++) {
						nvariance += Math.pow((currentclass.classifier_cell.get(j) - average), 2);
					}
					double standardDeviation = Math.sqrt(nvariance / numOfCell);

					standard_Deviation.add(df.format(standardDeviation));
				}

				// currentarray.add(i);// classid
			}

		}

		m.put("mid", mid);
		m.put("min", min);
		m.put("max", max);
		m.put("count", count);
		m.put("difference_MaxMin", difference_MaxMin);
		m.put("min_distance", min_distance);
		m.put("max_distance", max_distance);
		m.put("standard_Deviation", standard_Deviation);

		return m;
	}

}

class Classifier {
	public ArrayList<Double> classifier_cell = new ArrayList<Double>();
	public double min;
	public double max;
	public double min_distance = 0;
	public double max_distance = 0;

	public Classifier(double x) {
		classifier_cell.add(x);
		min = max = x;
	}

	public Classifier() {
	}
}
