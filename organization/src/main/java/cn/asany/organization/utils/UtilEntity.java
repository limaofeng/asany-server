package cn.asany.organization.utils;

import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * 类 名 称：UtilEntity
 * 类 描 述：TODO
 * 创建时间：2020/4/21 2:35 下午
 * 创 建 人：z7
 */
public class UtilEntity {
    /**
     * 转化ListList<Map<String, Object>>类型数据转为List<T>数据格式
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> castEntity(List<Map<String, Object>> list, Class<T> clazz) {
        List<T> returnList = new ArrayList<T>();
        OgnlUtil ognlUtil = OgnlUtil.getInstance();
        for (Map<String, Object> map : list) {
            T object = ClassUtil.newInstance(clazz);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                ognlUtil.setValue(entry.getKey(), object, entry.getValue());
            }
            returnList.add(object);
        }
        return returnList;
    }


    /**
     * 计算年龄
     * @param birthDay
     * @return
     * @throws ParseException
     */
    public static int getAgeByBirth(Date birthDay) throws ParseException {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    /**
     * list转map
     * @param list
     * @param <T>
     * @return
     */
    public static  <T> List<Map<String,Object>> listConvert(List<T> list){
        List<Map<String,Object>> list_map=new ArrayList<Map<String,Object>>();
        try {
            for (T t : list) {
                Field[] fields=t.getClass().getDeclaredFields();
                Map<String, Object> m = new HashMap<String, Object>();
                for(Field field:fields){
                    String keyName=field.getName();
                    PropertyDescriptor pd = new PropertyDescriptor(keyName,t.getClass());
                    Method getMethod = pd.getReadMethod();// 获得getter方法
                    Object o = getMethod.invoke(t);// 执行get方法返回一个Object
                    m.put(keyName, o);
                }
                list_map.add(m);
            }
            return list_map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
