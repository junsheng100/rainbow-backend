package com.rainbow.base.utils;

import com.rainbow.base.annotation.ExcelCell;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.poi.ExcelModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellBase;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @Author：QQ: 304299340
 * @Filename：ExcelUtils
 * @Description：execl 工具类
 */
@Slf4j
@Component
public class ExcelUtils<T> {

  public final String XLS_TYPE = "xls";
  public final String XLSX_TYPE = "xlsx";

  @SneakyThrows
  public List<T> readExcel(String filePath, Class<T> clazz, int titleRowIndex) {
    List<T> list = new ArrayList<>();

    String fileType = checkFileType(filePath);

    if (XLS_TYPE.equals(fileType)) {
      list = readXls(filePath, clazz, titleRowIndex);
    }
    if (XLSX_TYPE.equals(fileType)) {
      list = readXlsx(filePath, clazz, titleRowIndex);
    }

    return list;
  }

  @SneakyThrows
  public List<T> writeExcel(ExcelModel<T> model) {
    List<T> list = model.getDataList();
    String filePath = model.getFileName();


    if (filePath.endsWith(XLS_TYPE)) {
      writeXls(model);
    }
    if (filePath.endsWith(XLSX_TYPE)) {
      writeXlsx(model);
    }

    return list;
  }

  @SneakyThrows
  private void writeXlsx(ExcelModel<T> model) {
    FileOutputStream outputStream = new FileOutputStream(model.getFileName());

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(model.getSheetName());
    int rowNum = 0;
    XSSFRow titleRow = sheet.createRow(rowNum);


    for (int i = 0; i < model.getTitles().size(); i++) {
      String title = model.getTitles().get(i);
      XSSFCell cell = titleRow.createCell(i);
      cell.setCellValue(title);
    }
    rowNum++;

    Class clazz = model.getDataList().get(0).getClass();
    Map<Integer, Field> excelCellMap = getExcelCellMap(clazz);

    List<T> dataList = model.getDataList();
    List<ExcelCell> excelCellList = getExcelCellList(clazz);
    int rowSize = model.getDataList().size();
    int first = rowNum;
    int last = first + rowSize - 1;

    for (int i = first; i <= last; i++) {
      T data = dataList.get(i - rowNum);
      XSSFRow row = sheet.createRow(i);
      setXlsxCellValue(row, data, excelCellList, excelCellMap);

    }

    workbook.write(outputStream);
    outputStream.close();

  }


  @SneakyThrows
  private void writeXls(ExcelModel<T> model) {

    FileOutputStream outputStream = new FileOutputStream(model.getFileName());
    HSSFWorkbook workbook = new HSSFWorkbook();

    HSSFSheet sheet = workbook.createSheet(model.getSheetName());

    int rowNum = 0;
    HSSFRow titleRow = sheet.createRow(rowNum);

    for (int i = 0; i < model.getTitles().size(); i++) {
      String title = model.getTitles().get(i);
      HSSFCell cell = titleRow.createCell(i);
      cell.setCellValue(title);
    }
    rowNum++;

    Class clazz = model.getDataList().get(0).getClass();
    Map<Integer, Field> excelCellMap = getExcelCellMap(clazz);

    List<T> dataList = model.getDataList();
    List<ExcelCell> excelCellList = getExcelCellList(clazz);
    int rowSize = model.getDataList().size();
    int first = rowNum;
    int last = first + rowSize - 1;

    for (int i = first; i <= last; i++) {
      T data = dataList.get(i - rowNum);
      HSSFRow row = sheet.createRow(i);
      setXlsCellValue(row, data, excelCellList, excelCellMap);

    }

    workbook.write(outputStream);
    outputStream.close();

  }

  @SneakyThrows
  private void setXlsxCellValue(XSSFRow row, T data, List<ExcelCell> excelCellList, Map<Integer, Field> excelCellMap) {

    for (int i = 0; i < excelCellList.size(); i++) {
      ExcelCell excelCell = excelCellList.get(i);
      XSSFCell cell = row.createCell(i);

      Field field = excelCellMap.get(excelCell.value());
      if (null != field) {
        field.setAccessible(true);
        Object value = field.get(data);
        String fieldName = field.getClass().getSimpleName();

        if (value instanceof String) {
          cell.setCellValue((String) value);
        } else if (value instanceof Date) {
          cell.setCellValue((Date) value);
        } else if (value instanceof Number) {
          cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
          cell.setCellValue((Boolean) value);
        } else if (value instanceof Character) {
          cell.setCellValue((Character) value);
        } else if (value instanceof Byte) {
          cell.setCellValue((Byte) value);
        }
      }
    }

  }

  @SneakyThrows
  private void setXlsCellValue(HSSFRow row, T data, List<ExcelCell> excelCellList, Map<Integer, Field> excelCellMap) {

    for (int i = 0; i < excelCellList.size(); i++) {
      ExcelCell excelCell = excelCellList.get(i);
      HSSFCell cell = row.createCell(i);

      Field field = excelCellMap.get(excelCell.value());
      if (null != field) {
        field.setAccessible(true);
        Object value = field.get(data);
        String fieldName = field.getClass().getSimpleName();

        if (value instanceof String) {
          cell.setCellValue((String) value);
        } else if (value instanceof Date) {
          cell.setCellValue((Date) value);
        } else if (value instanceof Number) {
          cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
          cell.setCellValue((Boolean) value);
        } else if (value instanceof Character) {
          cell.setCellValue((Character) value);
        } else if (value instanceof Byte) {
          cell.setCellValue((Byte) value);
        }
      }
    }
  }


  @SneakyThrows
  private List<T> readXlsx(String filePath, Class<T> clazz, int titleRowIndex) {
    List<T> list = Lists.newArrayList();


    Map<Integer, Field> excelCellMap = getExcelCellMap(clazz);
    FileInputStream fileInputStream = new FileInputStream(new File(filePath));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    try {
      Iterator<Sheet> sheetIterator = workbook.sheetIterator();

      while (sheetIterator.hasNext()) {
        XSSFSheet sheet = (XSSFSheet) sheetIterator.next();
        Iterator<Row> rowIterator = sheet.rowIterator();
        int rowNum = 0;

        while (rowIterator.hasNext()) {
          XSSFRow row = (XSSFRow) rowIterator.next();
          if (rowNum > titleRowIndex) {
            T data = getXlsxData(row, clazz, excelCellMap);
            if (null != data)
              list.add(data);
          }
          rowNum++;
        }
      }
    } catch (Exception e) {
      log.error("{}", e);
      throw new BizException(e.getMessage());
    } finally {
      fileInputStream.close();
      workbook.close();
    }
    return list;
  }

  @SneakyThrows
  private List<T> readXls(String filePath, Class<T> clazz, int titleRowIndex) {
    List<T> list = Lists.newArrayList();
    Map<Integer, Field> excelCellMap = getExcelCellMap(clazz);

    FileInputStream fileInputStream = new FileInputStream(new File(filePath));

    HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
    try {
      Iterator<Sheet> sheetIterator = workbook.sheetIterator();

      while (sheetIterator.hasNext()) {
        HSSFSheet sheet = (HSSFSheet) sheetIterator.next();
        Iterator<Row> rowIterator = sheet.rowIterator();
        int rowNum = 0;
        while (rowIterator.hasNext()) {
          HSSFRow row = (HSSFRow) rowIterator.next();
          if (rowNum > titleRowIndex) {
            T data = getXlsData(row, clazz, excelCellMap);
            if (null != data)
              list.add(data);
          }
          rowNum++;
        }
      }
    } catch (Exception e) {
      log.error("{}", e);
      throw new BizException(e.getMessage());
    } finally {
      fileInputStream.close();
      workbook.close();
    }

    return list;
  }


  private Map<Integer, Field> getExcelCellMap(Class clazz) {
    Map<Integer, Field> map = new LinkedHashMap<>();
    List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());

    for (Field field : fieldList) {
      field.setAccessible(true);
      ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
      if (null != excelCell) {
        int index = excelCell.value();
        if (index > -1) {
          map.put(index, field);
        } else {
          log.warn("{}字段{}的index属性值不能小于0", clazz.getName(), field.getName());
        }

      }
    }

    return map;
  }


  @SneakyThrows
  private T getXlsData(HSSFRow row, Class<T> clazz, Map<Integer, Field> fieldMap) {
    T data = null;

    if (row != null) {
      data = clazz.newInstance();

      Integer first = (int) row.getFirstCellNum();
      Integer last = (int) row.getLastCellNum();

      if (first < 0)
        return null;
      if (last < first)
        return null;
      for (Integer i = first; i < last; i++) {
        HSSFCell cell = row.getCell(i);
        if (cell != null) {
          Field field = (Field) MapUtils.getObject(fieldMap, i, null);
          if (null != field) {

            createCellFieldData(data, field, cell);
          }
        }
      }
    }

    return data;
  }

  @SneakyThrows
  public T getXlsxData(XSSFRow row, Class<T> clazz, Map<Integer, Field> fieldMap) {
    T data = null;

    if (row != null) {
      data = clazz.newInstance();

      Integer first = (int) row.getFirstCellNum();
      Integer last = (int) row.getLastCellNum();

      if (first < 0)
        return null;
      if (last < first)
        return null;

      for (Integer i = first; i < last; i++) {
        XSSFCell cell = row.getCell(i);
        if (cell != null) {
          Field field = (Field) MapUtils.getObject(fieldMap, i, null);
          if (null != field) {
            createCellFieldData(data, field, cell);
          }
        }
      }
    }

    return data;
  }

  @SneakyThrows
  private void createCellFieldData(T data, Field field, CellBase cell) {
    if (null != field) {
      if (null != cell) {
        field.setAccessible(true);
        String str = "";
        log.debug("######## Cell index:[ {},{} ]",cell.getRowIndex(),cell.getColumnIndex());
        switch (cell.getCellType()) {
          case STRING:
            str = cell.getStringCellValue();
            break;
          case NUMERIC:
            str = String.valueOf(cell.getNumericCellValue());
            break;
          case BOOLEAN:
            str = String.valueOf(cell.getBooleanCellValue());
            break;
          case BLANK:
            str = "";
            break;
          default:
            str = "";
            break;
        }

        Class type = field.getType();
        if (StringUtils.isNotBlank(str)) {
          if (type == String.class) {
            field.set(data, str);
          } else if (type == boolean.class || type == Boolean.class) {
            Boolean value = Boolean.valueOf(str);
            field.set(data, value);
          } else if (type == int.class || type == Integer.class) {
            Integer value = Double.valueOf(str).intValue();
            field.set(data, value);
          } else if (type == long.class || type == Long.class) {
            Long value = Double.valueOf(str).longValue();
            field.set(data, value);
          } else if (type == float.class || type == Float.class) {
            Float value = Float.valueOf(str);
            field.set(data, value);
          } else if (type == double.class || type == Double.class) {
            field.set(data, Double.parseDouble(str));
          } else if (type == Date.class) {
            field.set(data, DateTools.parseDate(str));
          } else if (type == BigDecimal.class) {
            field.set(data, new BigDecimal(str));
          } else if (type == BigInteger.class) {
            field.set(data, new BigInteger(str));
          }
        }
      }
    }
  }


  private List<ExcelCell> getExcelCellList(Class clazz) {

    List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
    List<ExcelCell> list = new ArrayList<>();
    for (Field field : fieldList) {
      field.setAccessible(true);
      ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
      if (null != excelCell) {
        list.add(excelCell);
      }
    }

    list.stream().sorted(Comparator.comparingInt(ExcelCell::value));
    return list;

  }


  public String checkFileType(String filePath) throws IOException {
    String type = null;
    FileInputStream fis = new FileInputStream(filePath);
    try {
      // 尝试用POIFSFileSystem读取，如果成功则是.xls文件
      POIFSFileSystem fs = new POIFSFileSystem(fis);
      type = XLS_TYPE;
    } catch (Exception e) {

    } finally {
      fis.close();
    }
    fis = new FileInputStream(filePath);
    try {
      XSSFWorkbook workbook = new XSSFWorkbook(fis);
      type = XLSX_TYPE;
    } catch (Exception e) {

    } finally {
      fis.close();
    }
    return type;

  }

  public String getCellValue(Object obj, String fieldName) {
    try {
      return obj.getClass().getDeclaredField(fieldName).get(obj).toString();
    } catch (Exception e) {
      return "";
    }
  }
}
