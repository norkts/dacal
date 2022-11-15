package com.norkts.dacal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Maps;
import com.norkts.dacal.domain.Config;
import com.norkts.dacal.types.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T pasreObject(String content, Class<T> clazz){
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructType(clazz));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> clazz){
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toJSONString(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> object2DbMap(Object objectDo){

        Map<String, Object> dbMap = Maps.newHashMap();
        for(Field field : objectDo.getClass().getDeclaredFields()){

            if(Modifier.isFinal(field.getModifiers())
                || Modifier.isStatic(field.getModifiers())){
                continue;
            }

            field.setAccessible(true);
            try {
                dbMap.put(toDbColumn(field.getName()), field.get(objectDo));
            } catch (IllegalAccessException e) {
                dbMap.put(toDbColumn(field.getName()),null);
            }
        }

        return dbMap;
    }

    public static <T> T dbMap2Object(Map<String,Object> objectMap, T result){

        for(Field field : result.getClass().getDeclaredFields()){
            if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
                continue;
            }

            field.setAccessible(true);
            try {
                Object val = objectMap.get(toDbColumn(field.getName()));
                if(field.getType().isAssignableFrom(Date.class)){
                    if(val instanceof Number){
                        Date d = new Date();
                        d.setTime(((Number)val).longValue());
                        field.set(result, d);
                    }
                }else{
                    field.set(result, val);
                }


            } catch (IllegalAccessException e) {
                try {
                    field.set(result,null);
                } catch (IllegalAccessException ex) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    /**
     * 驼峰命名字符转换为下划线分隔
     * @param fieldName
     * @return
     */
    public static String toDbColumn(String fieldName){
        if(fieldName == null || fieldName.isEmpty()){
            return fieldName;
        }

        return fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    /**
     * 下划线分隔字符转换为驼峰命名
     * @param columnName
     * @return
     */
    public static String toFieldName(String columnName){

        if(columnName == null || columnName.isEmpty()){
            return columnName;
        }

        return Arrays.stream(columnName.split("_"))
                .map(s -> s.substring(0,1).toUpperCase() + s.substring(1)).collect(Collectors.joining());
    }

    public static void main(String[] args) {
        Config config = new Config();
        Map<String, Object> configMap = Maps.newHashMap();
        configMap.put("key", Constants.GAMBLING_DATA_KEY);
        configMap.put("value", "{}");
        configMap.put("gmt_modified", System.currentTimeMillis());
        configMap.put("gmt_create", System.currentTimeMillis());


        System.out.println(toJSONString(dbMap2Object(configMap,config)));
    }
}
