package ljx.ashin.utils.content;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * 对象的内容处理工具
 * Created by AshinLiang on 2018/1/13.
 */
public class ObjContentUtil {
    /**
     * 对对象的内容进行处理
     * @param object
     * @return
     */
    public static Object processContent(Object object){
        //1、获取所有的属性
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);//设置访问权限
            String type = field.getType().toString();//获得属性类型
            if (type.endsWith("String")){
                try {
                    String fieldValue = (String)field.get(object);
                    if (StringUtils.isBlank(fieldValue)){
                        field.set(object,null);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

   /* public static void main(String[] args) {
        TSysSecondAptitude tSysSecondAptitude = new TSysSecondAptitude();
        tSysSecondAptitude.setId(24);
        tSysSecondAptitude.setSecondAptitude("");
        tSysSecondAptitude.setSecondAptitudeId("4343");
        tSysSecondAptitude = (TSysSecondAptitude) ObjContentUtil.processContent(tSysSecondAptitude);
        System.out.println(tSysSecondAptitude);
    }*/
}
