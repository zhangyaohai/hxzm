package com.hxzm.common.kit;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 *
 * @author leno
 * @version v1.0
 *
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel {

	public void exportExcel(String[] headers, Collection<Object[]> dataset, OutputStream out) {
		exportExcel("导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	}

	/**
	 *
	 * @param response
	 * @param title 标题
	 * @param startRow 数据从第几行开始  下标从1开始
	 * @param headers 最下边标题
	 * @param mergeParams 合并参数 {{1,1,7,10,"Android"}} 第一行到第一行 第7列到第10列合并然后赋值为‘and’
	 * @param dataList
	 * @param excelName
	 */
	public static void exportExcel(HttpServletResponse response, String title,Integer startRow,String[] headers, Object[][] mergeParams,Collection<Object[]> dataList, String excelName) {
		ExportExcel ex = new ExportExcel();
		BufferedOutputStream out = null;
		try {

			response.setContentType("application/vnd.ms-excel;charset=utf8");
//			response.setHeader("Content-disposition",
//					"attachment; filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1"));

			String headerValue = "attachment;";
			headerValue += " filename=\"" + encodeURIComponent(excelName) +"\";";
			headerValue += " filename*=utf-8''" + encodeURIComponent(excelName);
			response.setHeader("Content-Disposition", headerValue);


			out = new BufferedOutputStream(response.getOutputStream());
			ex.exportExcel(title,startRow, headers,mergeParams, dataList, out, "yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 导出excel
	 *
	 * @param response
	 * @param headers
	 * @param dataList
	 * @param excelName
	 */
	public static void exportExcel(HttpServletResponse response, String[] headers, List<Object[]> dataList, String excelName) {
		ExportExcel ex = new ExportExcel();
		BufferedOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf8");
//			response.setHeader("Content-disposition",
//					"attachment; filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1"));

			String headerValue = "attachment;";
			headerValue += " filename=\"" + encodeURIComponent(excelName) +"\";";
			headerValue += " filename*=utf-8''" + encodeURIComponent(excelName);
			response.setHeader("Content-Disposition", headerValue);


			out = new BufferedOutputStream(response.getOutputStream());
			ex.exportExcel(headers, dataList, out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encodeURIComponent(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 导出excel
	 *
	 * @param response
	 * @param headers
	 * @param dataList
	 * @param excelName
	//	 */
//	public static void exportBigExcel(HttpServletResponse response, String[] headers, List<Object[]> dataList, String excelName) {
//		ExportExcel ex = new ExportExcel();
//
//		BufferedOutputStream out = null;
//		try {
//			response.setContentType("application/vnd.ms-excel;charset=utf8");
//			response.setHeader("Content-disposition",
//					"attachment; filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1"));
//			out = new BufferedOutputStream(response.getOutputStream());
//			ex.exportBigDataExcel(headers, dataList);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null)
//				try {
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//	}


	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers, Collection<Object[]> dataset, OutputStream out,
							String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);

		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("zhanghm");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<Object[]> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object[] obj = (Object[]) it.next();

			for (short i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);

				if (obj[i] instanceof Integer) {
					cell.setCellValue((Integer)obj[i]);
				}else{
					HSSFRichTextString text = new HSSFRichTextString(obj[i].toString());
					cell.setCellValue(text);
				}

				//cell.setCellStyle(style);
			}

		}
		try {
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn((short)i); //调整第二列宽度
			}

			workbook.write(out);
			// OutputStream out2 = new FileOutputStream("test.xls");
			// workbook.write(out2);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 * @param title
	 * @param headers
	 * @param startRow 开始列 下标从1开始
	 * @param mergeParams
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public void exportExcel(String title, Integer startRow, String[] headers, Object[][] mergeParams, Collection<Object[]> dataset, OutputStream out,
							String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("zhanghm");


		if (mergeParams != null) {
			HSSFCellStyle style3 = workbook.createCellStyle();
			// 设置这些样式
			style3.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style3.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont font3 = workbook.createFont();
			font3.setColor(HSSFColor.WHITE.index);
//			font.setColor(HSSFColor.VIOLET.index);
			font3.setFontHeightInPoints((short) 12);
			font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style3.setFont(font3);
			for (Object[] mergeParam : mergeParams) {
				Integer row1 = (Integer) mergeParam[0];
				Integer row2 = (Integer) mergeParam[1];
				Integer col1 = (Integer) mergeParam[2];
				Integer col2 = (Integer) mergeParam[3];
				String content = (String) mergeParam[4];
				HSSFRow hssfRow = sheet.getRow(row1 - 1);
				if (hssfRow == null) {
					hssfRow = sheet.createRow(row1 - 1);
				}
				HSSFCell hssfRowCell = hssfRow.getCell(col1 - 1);
				if (hssfRowCell == null) {
					hssfRowCell = hssfRow.createCell(col1 - 1);
				}
				hssfRowCell.setCellStyle(style3);
				hssfRowCell.setCellValue(new HSSFRichTextString(content));

				CellRangeAddress cellRangeAddress = new CellRangeAddress(row1 - 1, row2 - 1, col1 - 1, col2 - 1);
				sheet.addMergedRegion(cellRangeAddress);
//				setRegionStyle(sheet,cellRangeAddress,style3);
				setRegionBorder(1,cellRangeAddress,sheet,workbook);
			}
		}

		// 产生表格标题行
		HSSFRow row = sheet.createRow(startRow-2);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<Object[]> it = dataset.iterator();
		int index = startRow-2;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object[] obj = (Object[]) it.next();

			for (short i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);

				if (obj[i] instanceof Integer) {
					cell.setCellValue((Integer)obj[i]);
				}else{
					HSSFRichTextString text = new HSSFRichTextString(obj[i].toString());
					cell.setCellValue(text);
				}

				//cell.setCellStyle(style);
			}

		}
		try {
			workbook.write(out);
			// OutputStream out2 = new FileOutputStream("test.xls");
			// workbook.write(out2);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * @param border 边框宽度
	 * @param region 合并单元格区域范围
	 * @param sheet
	 * @param wb
	 */
	public static void setRegionBorder(int border, CellRangeAddress region, Sheet sheet, Workbook wb){
		RegionUtil.setBorderBottom(border,region, sheet, wb);
		RegionUtil.setBorderLeft(border,region, sheet, wb);
		RegionUtil.setBorderRight(border,region, sheet, wb);
		RegionUtil.setBorderTop(border,region, sheet, wb);
	}
	public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region,
									  HSSFCellStyle cs) {
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {

			HSSFRow row = sheet.getRow(i);
			if (row == null)
				row = sheet.createRow(i);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
					cell.setCellValue("");
				}
				cell.setCellStyle(cs);

			}
		}
	}
	public static void main(String[] args) {
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 *            out与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 */
	@SuppressWarnings("unchecked")
	public SXSSFWorkbook exportBigDataExcel( String[] headers, Collection<Object[]> dataset) {


		// 声明一个工作薄
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		SXSSFWorkbook workbook = new SXSSFWorkbook(100);

		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//每次缓存500条到内存，其余写到磁盘。
//		SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
		// 生成一个表格
		Sheet sheet = workbook.createSheet();
//		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式

		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);



		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<Object[]> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object[] obj = (Object[]) it.next();

			for (short i = 0; i < headers.length; i++) {
				Cell cell = row.createCell(i);

				if (obj[i] instanceof Integer) {
					cell.setCellValue((Integer)obj[i]);
				}else{
					HSSFRichTextString text = new HSSFRichTextString(obj[i].toString());
					cell.setCellValue(text);
				}

			}

		}

		return  workbook;
//			workbook.write(out);
		// OutputStream out2 = new FileOutputStream("test.xls");
		// workbook.write(out2);

	}



}