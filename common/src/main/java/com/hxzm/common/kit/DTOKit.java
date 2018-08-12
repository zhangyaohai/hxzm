package com.hxzm.common.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTOKit {

    /*
     * 请保证属性的名字 一样
     * */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object fromObj, Object toObj) {
        if (fromObj == null || toObj == null)
            return null;
        Field[] fromFields = getAllFields(fromObj.getClass());
        Field[] toFields = getAllFields(toObj.getClass());
        try {
            for (Field f : toFields) {
                if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                f.setAccessible(true);
                for (Field f2 : fromFields) {
                    f2.setAccessible(true);
                    if (f.getName().equals(f2.getName())) {
                        f.set(toObj, f2.get(fromObj));
                    }
                }
            }
            return (T) toObj;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> convertList(List<?> fromList, Class<?> classz) {
        if (fromList == null || fromList.size() == 0)
            return null;
        Object[] objs = new Object[fromList.size()];
        for (int i = 0; i < fromList.size(); i++) {
            try {
                objs[i] = DTOKit.convert(fromList.get(i), classz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (List<T>) Arrays.asList(objs);
    }

    public static Map<String, Object> convertObjToMap(Object fromObj) {
        if (fromObj == null)
            return null;

        Map<String, Object> m = new HashMap<String, Object>();
        Field[] fields = fromObj.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                m.put(f.getName(), f.get(fromObj));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return m;
    }

    @SuppressWarnings("rawtypes")
    public static Field[] getAllFields(final Class classz) {
        //获取size
        int i = 0;
        Class initClass = classz;
        do {
            i += initClass.getDeclaredFields().length;
            initClass = initClass.getSuperclass();
        }
        while (initClass != null);

        Field[] fields = new Field[i];
        int k = i;

        initClass = classz;
        do {
            for (Field f : initClass.getDeclaredFields()) {
                fields[k - 1] = f;
                k--;
            }

            initClass = initClass.getSuperclass();

        }
        while (initClass != null);

        return fields;
    }


    public static List<?> sortByNumberAsc(List list, String fieldName) {

        if (list == null)
            return null;

        Object objs[] = list.toArray();

        for (int j = 0; j < objs.length - 1; j++) {

            for (int i = j; i < objs.length - 1; i++) {

                Class classz = objs[j].getClass();
                Field[] fields = getAllFields(classz);
                Object l1 = getObjectValueByFieldName(fields, objs[j], fieldName);
                Object l2 = getObjectValueByFieldName(fields, objs[i + 1], fieldName);

                if ((l1 != null && l2 != null && new BigDecimal(l1.toString()).compareTo(new BigDecimal(l2.toString())) == 1) || l1 == null) {
                    Object temp = objs[j];
                    objs[j] = objs[i + 1];
                    objs[i + 1] = temp;
                }

            }
        }
        return Arrays.asList(objs);
    }

    static Object getObjectValueByFieldName(Field[] fields, Object obj, String fieldName) {

        try {
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals(fieldName)) {
                    return f.get(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
