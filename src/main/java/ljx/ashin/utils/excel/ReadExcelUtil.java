package ljx.ashin.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel文件
 * Created by AshinLiang on 2017/12/30.
 */
public class ReadExcelUtil {


    /**
     * 需要引入POI的两个包
     * 读取后缀为xls的excel文件的数据
     * @param path
     * @return List<StudentBean>
     * @author
     */
    private<T> List<T> readXls(String path,Class<T> t) throws IllegalAccessException, InstantiationException {

        HSSFWorkbook hssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        T obj = null;
        List<T> list = new ArrayList<T>();
        if (hssfWorkbook != null) {
            // Read the Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // Read the Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
//                        t = new T();
                        T instance = t.newInstance();
                       /* HSSFCell fifthAchievementId = hssfRow.getCell(0);
                        HSSFCell fifthAchievement = hssfRow.getCell(1);
                        HSSFCell fourthAchievementId = hssfRow.getCell(2);*/
                        Field[] fields = t.getDeclaredFields();
                        List<HSSFCell> hssfCellList = new ArrayList<HSSFCell>();
                        for (int i = 0;i<fields.length;i++){
                            String fieldName = "fieldName"+i;
                            HSSFCell cell = hssfRow.getCell(i);
                            hssfCellList.add(cell);
                            Field field = fields[i];
                            field.setAccessible(true);
                            try {
                                field.set(instance,getValue(cell));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        list.add(instance);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 读取后缀为xlsx的excel文件的数据
     * @param path
     * @return List<T>
     * @author
     */
    public <T> List<T> readXlsx(String path,Class<T> t) throws IllegalAccessException, InstantiationException {

        XSSFWorkbook xssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        TSysFifthAchievement tSysFifthAchievement = null;
        List<T> list = new ArrayList<T>();
        if(xssfWorkbook!=null){
            // Read the Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                // Read the Row
                List<Object> stringList = new ArrayList<Object>();
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        T instance = t.newInstance();
                        Field[] fields = instance.getClass().getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            XSSFCell cell = xssfRow.getCell(i);
                            Field field = fields[i];
                            field.setAccessible(true);
                            try {
                                String strValue = getValue(cell);
                                String type = field.getType().toString();//得到此属性的类型
                                if (type.endsWith("String")) {
                                    if (null!=strValue){
                                        field.set(instance,strValue);
                                        if (null!=strValue){
                                            stringList.add(strValue);
                                        }
                                    }else {
                                        field.set(instance,null);
                                    }
                                }else if(type.endsWith("int") || type.endsWith("Integer")){
                                    if (null==strValue){
                                        field.set(instance,null);
                                    }else if(!"".equals(strValue)){
                                        Integer integerValue = Integer.valueOf(strValue);
                                        field.set(instance,integerValue);
                                        if (null!=integerValue){
                                            stringList.add(integerValue);
                                        }
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stringList.size()>=2){
                            list.add(instance);
                        }
                    }
                }
            }
        }
        return list;
    }


    /**
     * 获取文件扩展名
     * @param path
     * @return String
     * @author
     */
    private  String getExt(String path) {
        if (path == null || path.equals("") || !path.contains(".")) {
            return null;
        } else {
            return path.substring(path.lastIndexOf(".") + 1, path.length());
        }
    }

    /**
     * 判断后缀为xls的excel文件的数据类型
     * @param hssfCell
     * @return String
     * @author
     */
    @SuppressWarnings("static-access")
    private  String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 判断后缀为xlsx的excel文件的数据类型
     * @param xssfRow
     * @return String
     * @author
     */
    private String getValue(XSSFCell xssfRow) {
        try {
            if (null!=xssfRow){
                if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
                    return String.valueOf(xssfRow.getBooleanCellValue());
                } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
                    long longVal = Math.round(xssfRow.getNumericCellValue());
                    return String.valueOf(longVal);
                } else {
                    String stringCellValue = String.valueOf(xssfRow.getStringCellValue());
                    if (stringCellValue.contains("0")){
                        int index = stringCellValue.indexOf("0");
                        stringCellValue = stringCellValue.substring(0,index);
                    }
                    return stringCellValue;
                }
            }
        }catch (Exception e){
            System.out.println(xssfRow);
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        String path = "C://Users//AshinLiang//Documents//Tencent Files//1123842546//FileRecv//achievement.xlsx";
        ReadExcelUtil readExcelUtil = new ReadExcelUtil();
/*        List<TSysFifthAchievement> list = readExcelUtil.readExcelToTSysFifthAchievementList(path);
        System.out.println(list);
        System.out.println(list.get(0).getFifthAchievementId());
        System.out.println(list.get(0).getFifthAchievement());
        System.out.println(list.get(0).getFourthAchievementId());*/
    }

}
