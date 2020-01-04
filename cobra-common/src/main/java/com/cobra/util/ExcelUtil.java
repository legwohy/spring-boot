package com.cobra.util;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtil {

    /**
     *
     * //
     *    response.setContentType("application/ms-excel,charset=UTF-8");
     response.setHeader("Content-Disposition", "attachment;filename=" + new String("合同.xls".getBytes("GB2312"), "iso8859-1"));

     ServletOutputStream out =  response.getOutputStream();

     * 创建Excel文件
     *
     * @param headMap 表头
     * @param contentList 每行的单元格
     */
    public static void writeExcelNew(OutputStream  outputStream,LinkedHashMap<String,String> headMap,
                                  List<Map<String, Object>> contentList) throws IOException {
        try {
            // xlsx  xls返回的是HSSFWorkbook
            Workbook workbook = new XSSFWorkbook();

            // 生成一个表格
            Sheet sheet = workbook.createSheet();

            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);

            // 创建标题行
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;
            for (Map.Entry<String,String> en: headMap.entrySet()){
                // 创建标题列
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(en.getValue());
                cellIndex++;
            }

            // 第二行 写入正文
            if(null == contentList || contentList.isEmpty()){
                workbook.write(outputStream);
                return;
            }

            for (Map<String,Object> content:contentList){
                // 创建行
                rowIndex++;
                row = sheet.createRow(rowIndex);

                // 写入列
                cellIndex = 0;
                for (Map.Entry<String,String> en: headMap.entrySet()){
                    Cell cell = row.createCell(cellIndex);
                    Object obj = content.get(en.getKey());
                    // 类型转换
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (obj instanceof Double) {
                        cell.setCellValue((Double) obj);
                    } else if (obj instanceof Date) {
                        String time = simpleDateFormat.format((Date) obj);
                        cell.setCellValue(time);
                    } else if (obj instanceof Calendar) {
                        Calendar calendar = (Calendar) obj;
                        String time = simpleDateFormat.format(calendar.getTime());
                        cell.setCellValue(time);
                    } else if (obj instanceof Boolean) {
                        cell.setCellValue((Boolean) obj);
                    } else {
                        if (obj != null) {
                            cell.setCellValue(obj.toString());
                        }
                    }
                    cellIndex++;

                }

            }


            workbook.write(outputStream);


        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(null != outputStream){
                outputStream.close();
            }


        }



    }



}