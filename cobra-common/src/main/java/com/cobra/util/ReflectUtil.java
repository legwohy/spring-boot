package com.cobra.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final Map<String, List<Field>> findAnnotationCache = new ConcurrentHashMap(256);
    private static final Map<String, Field[]> getDeclaredFields = new ConcurrentHashMap(256);

    public ReflectUtil() {
    }

    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[0], new Object[0]);
    }

    public static void invokeSetter(Object obj, String propertyName, Object value) {
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethodByName(obj, setterMethodName, new Object[]{value});
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }

    public static Object invokeMethodByName(Object obj, String methodName, Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }

    public static Method getAccessibleMethod(Object obj, String methodName, Class... parameterTypes) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(methodName, "methodName can't be blank", new Object[0]);
        Class searchType = obj.getClass();

        while (searchType != Object.class) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException var5) {
                searchType = searchType.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethodByName(Object obj, String methodName) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(methodName, "methodName can't be blank", new Object[0]);

        for (Class searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            Method[] var4 = methods;
            int var5 = methods.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Method method = var4[var6];
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }

        return null;
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static Field getAccessibleField(Object obj, String fieldName) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(fieldName, "fieldName can't be blank", new Object[0]);
        Class superClass = obj.getClass();

        while (superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Class<?> getFieldClass(Field field) {
        return null;
    }

    public static Object getFieldVal(Object obj, String filedName) {
        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static void setFieldVal(Object obj, String filedName, Object val) {
        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            if (null != field) {
                field.setAccessible(true);
                field.set(obj, val);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
            log.error(var4.getMessage(), var4);
        }

    }

    public static List<Field> findFields(Class cls, Class annotationType) {
        String key = cls.getCanonicalName() + "_" + annotationType.getCanonicalName();
        List<Field> result = (List) findAnnotationCache.get(key);
        if (result == null) {
            result = new ArrayList();
            Field[] fields = cls.getDeclaredFields();
            Field[] var5 = fields;
            int var6 = fields.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Field field = var5[var7];
                if (field.isAnnotationPresent(annotationType)) {
                    ((List) result).add(field);
                }
            }

            findAnnotationCache.put(key, result);
        }

        return (List) result;
    }

    public static void setFieldValue(Object target, String fname, Class<?> ftype, Object fvalue) {
        if (target != null && fname != null && !"".equals(fname) && (fvalue == null || ftype.isAssignableFrom(fvalue.getClass()))) {
            Class clazz = target.getClass();

            try {
                Method method = clazz.getDeclaredMethod("set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1), new Class[]{ftype});
                if (!Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(true);
                }

                method.invoke(target, new Object[]{fvalue});
            } catch (Exception var8) {
                log.error(var8.getMessage(), var8);

                try {
                    Field field = clazz.getDeclaredField(fname);
                    if (!Modifier.isPublic(field.getModifiers())) {
                        field.setAccessible(true);
                    }

                    field.set(target, fvalue);
                } catch (Exception var7) {
                    log.error(var7.getMessage(), var7);
                }
            }

        }
    }

    public static <T> Class<T> getSuperClassGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    public static Field[] getDeclaredFields(Class clazz) {
        String key = clazz.getCanonicalName();
        Field[] fs = (Field[]) getDeclaredFields.get(key);
        if (null == fs) {
            fs = clazz.getDeclaredFields();
            getDeclaredFields.put(key, fs);
        }

        return fs;
    }
}

