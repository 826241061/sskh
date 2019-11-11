package q;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class test {

	
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		ArrayList<Double> n = new ArrayList<Double>();
		
		
		
		
		
		String excelPath = "C:\\Users\\00255\\Documents\\WeChat Files\\dece____\\FileStorage\\File\\2019-10\\一维数值聚类算法.xlsx";

        try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("文件类型错误!");
                    return;
                }

                //开始解析
                XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
               // System.out.println("firstRowIndex: "+firstRowIndex);
                //System.out.println("lastRowIndex: "+lastRowIndex);
                System.out.println("数据条数："+lastRowIndex);

                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    //System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
//                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
//                            Cell cell = (Cell) row.getCell(cIndex);
//                            if (cell != null) {
//                                System.out.println(cell.toString());
//                            }
//                        }

                        Cell cell = (Cell) row.getCell(firstCellIndex);
                        if (cell != null) {
                            n.add(Double.parseDouble(cell.toString()));
                        }
                        
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
		
		
		
		
		
		System.out.println("请输入中间值：");
		Scanner sc = new Scanner(System.in);
		double division = sc.nextDouble();
		if(n.size()>2 && division >= 0 && division <= 1) {
			cluster(n,division);
		}

	}


	public static int cluster(ArrayList<Double> nums, double division) {
		ArrayList<Double> ds = new ArrayList<Double>();//差值
		ArrayList<Classifier> classes = new ArrayList<Classifier>();//所有聚类
		double d;
		ds.add(nums.get(1)-nums.get(0));
		for(int i = 2; i < nums.size(); i++) {//将差值d放入ArrayList
			d=nums.get(i)-nums.get(i-1);
			if(d<ds.get(0))	
				ds.add(0, d);
			else if(d>ds.get(ds.size()-1)) 
				ds.add(d);
			else {
				for(int j = 1; d>ds.get(j-1)&&j < ds.size(); j++) {
					if(d>ds.get(j-1) && d<ds.get(j))
						ds.add(j, d);
				}
			}
		}
		
		int numOfK = (int) (ds.size()*division);
		classes.add(new Classifier());
		classes.get(0).classifier_cell.add(nums.get(0));
		classes.get(0).min = classes.get(0).max = nums.get(0);
		
		for(int i = 1; i < nums.size(); i++) {
			if(nums.get(i) - nums.get(i-1) > numOfK) {
				classes.add(new Classifier());
				classes.get(classes.size()-1).min = nums.get(i);
			}
			Classifier lastclass = classes.get(classes.size()-1);
			if(lastclass.classifier_cell.size()>0){
				double distance = nums.get(i)-lastclass.classifier_cell.get(lastclass.classifier_cell.size()-1);
				if(distance>0) {
					if(lastclass.min_distance==0 || distance<lastclass.min_distance)
						lastclass.min_distance=distance;
				}
			}
			lastclass.classifier_cell.add(nums.get(i));
			lastclass.max = nums.get(i);
		}
		
		System.out.println("类的数量："+classes.size());
		for(int i = 0; i < classes.size(); i++) {
			System.out.println("第"+i+"个类有"+classes.get(i).classifier_cell.size()+"个数");
			System.out.println("它的最小值为："+classes.get(i).min);
			System.out.println("它的最大值为："+classes.get(i).max);
		}
		
		
		
		
//		Map<String, String> dataMap=new HashMap<String, String>();
//        dataMap.put("BankName", "BankName");
//        dataMap.put("Addr", "Addr");
//        dataMap.put("Phone", "Phone");
//        List<Map> dataList=new ArrayList<Map>();
//        dataList.add(dataMap);
//        int cloumnCount=3;
        String finalXlsxPath = "C:\\Users\\00255\\Documents\\WeChat Files\\dece____\\FileStorage\\File\\2019-10\\一维数值聚类算法.xlsx";
//       // writeExcel(list, 3, "D:/writeExcel.xlsx");
//		
//		OutputStream out = null;
//        try {
//            // 获取总列数
//            int columnNumCount = cloumnCount;
//            // 读取Excel文档
//            File finalXlsxFile = new File(finalXlsxPath);
//            Workbook workBook = getWorkbok(finalXlsxFile);
//            // sheet 对应一个工作页
//            Sheet sheet = workBook.getSheetAt(0);
//            /**
//             * 删除原有数据，除了属性列
//             */
//            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
//            System.out.println("原始数据总行数，除属性列：" + rowNumber);
//            for (int i = 1; i <= rowNumber; i++) {
//                Row row = sheet.getRow(i);
//                sheet.removeRow(row);
//            }
//            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
//            out =  new FileOutputStream(finalXlsxPath);
//            workBook.write(out);
//            /**
//             * 往Excel中写新数据
//             */
//            double cluster;
//            double num;
//            for (int j = sheet.getFirstRowNum()+1; j <= sheet.getLastRowNum(); j++) {
//                // 创建一行：从第二行开始，跳过属性列
//                Row row = sheet.getRow(j);
//                // 得到要插入的每一条记录
//
//                // 在一行内循环
//                num = Double.parseDouble(row.getCell(0).toString());
//                for(int i = 0; i < ds.size(); i++) {
//                	if(num >= classes.get(i).min && num <= classes.get(i).max && classes.get(i).classifier_cell.size()>1) {
//                		row.createCell(2);
//                		 Cell second = row.getCell(2);
//                         second.setCellValue(i);
//                	}
//                }
//                
//        
//
//
//            }
//            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
//            out =  new FileOutputStream(finalXlsxPath);
//            workBook.write(out);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally{
//            try {
//                if(out != null){
//                    out.flush();
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
		
		
        File file = new File(finalXlsxPath);
   	  //定义输入流对象
   	  FileInputStream excelFileInputStream;
   	  try {
   	   excelFileInputStream = new FileInputStream(file);
   	   // 拿到文件转化为JavaPoi可操纵类型
   	   Workbook workbook = WorkbookFactory.create(excelFileInputStream);
   	   excelFileInputStream.close();
   	   ////获取excel表格
   	   Sheet sheet = workbook.getSheetAt(0);
   	   //获取单元格的row和cell
   	   // 获取行
//   	   String x = sheet.getRow(6).getCell(0).toString();
//
//   	   Row row = sheet.getRow(6);
//   	   // 获取列
//   	   row.createCell(4);
//   	   Cell cell = row.getCell(4);
//   	   //设置单元的值
//   	   cell.setCellValue(x);
   	   double num;
   	   for(int j=1; j<=sheet.getLastRowNum();j++) {
   		   num = Double.parseDouble(sheet.getRow(j).getCell(0).toString());
   		for(int i = 0; i < classes.size(); i++) {
        	if(num >= classes.get(i).min && num <= classes.get(i).max && classes.get(i).classifier_cell.size()>1) {
        		Row row = sheet.getRow(j);
        	   // 获取列
        	   row.createCell(1);
        	   Cell cell = row.getCell(1);
        	   //设置单元的值
        	   cell.setCellValue(i);
        	}
        }
   	   }
   	   
   	   sheet = workbook.getSheetAt(1);
   	   double nvariance=0;//n倍方差
   	   double average=0;//平均数
   	   double sum=0;//和
   	   double standardDeviation=0;//标准差
   	   for(int i = 0; i < classes.size(); i++) {
   		   Classifier currentclass = classes.get(i);
   		   int numOfCell=currentclass.classifier_cell.size();
   		   sheet.createRow(i+1);
   		   Row row = sheet.getRow(i+1);
   		   row.createCell(0);
   		   row.getCell(0).setCellValue(i);
   		   row.createCell(1);
		   row.getCell(1).setCellValue(currentclass.classifier_cell.size());
		   row.createCell(2);
   		   row.getCell(2).setCellValue(currentclass.min);
   		   row.createCell(3);
		   row.getCell(3).setCellValue(currentclass.max);
		   row.createCell(4);
		   row.getCell(4).setCellValue(currentclass.max-currentclass.min);
		   row.createCell(5);
		   row.getCell(5).setCellValue(currentclass.min_distance);
   		   
		   if(numOfCell==1) {
			   row.createCell(6);
			   row.getCell(6).setCellValue(0);
		   }
		   else {
		   
			   sum=0;
			   nvariance=0;
			   for(int j = 0; j < numOfCell;j++) {
				   sum+=currentclass.classifier_cell.get(j);
			   }
			   average=sum/numOfCell;
			   for(int j = 0; j < numOfCell;j++) {
				   nvariance+=(currentclass.classifier_cell.get(j)-average)*(currentclass.classifier_cell.get(j)-average);
			   }
			   standardDeviation=Math.sqrt(nvariance/numOfCell);
			   
			   
			   
			   row.createCell(6);
			   row.getCell(6).setCellValue(standardDeviation);
		   }
   	   }
   	   
   	   
   	   
   	   
   	   
   	   //写入数据
   	   FileOutputStream excelFileOutPutStream = new FileOutputStream(file);
   	   workbook.write(excelFileOutPutStream);
   	   excelFileOutPutStream.flush();
   	   excelFileOutPutStream.close();
   	   System.out.println("指定单元格设置数据写入完成");
   	  } catch (FileNotFoundException e) {
   	   e.printStackTrace();
   	  } catch (EncryptedDocumentException e) {
   	   e.printStackTrace();
   	  } catch (Exception e) {
   	   e.printStackTrace();
   	  }
        
         
       
        System.out.println("数据导出成功");
		
		
		
		
		
		
		
		
		
		return classes.size();
	}
	
	
	public static Workbook getWorkbok(File file) throws IOException{
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

class Classifier{
	public ArrayList<Double> classifier_cell = new ArrayList<Double>();
	public double min;
	public double max;
	public double min_distance=0;
}







