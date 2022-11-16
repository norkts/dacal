package com.norkts.dacal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Maps;
import com.norkts.dacal.domain.Config;
import com.norkts.dacal.domain.GamblingData;
import com.norkts.dacal.types.Constants;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
    }
    public static <T> T pasreObject(String content, Class<T> clazz){
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructType(clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> clazz){
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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

    public static byte[] toBinByteArr(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ots = new ObjectOutputStream(baos);
        ots.writeObject(o);
        ots.close();

        return baos.toByteArray();
    }

    public static String toBinString(Object o) {
        try {
            return new String(toBinByteArr(o), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T formBinByte(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        T result = (T) ois.readObject();
        return result;
    }

    public static <T> T fromBinString(String data){

        if(data == null){
            return null;
        }

        try {
            return formBinByte(data.getBytes(StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        GamblingData g = new GamblingData();
        g.rollSummary.onG100();
        g.cardSummary.onG50();
        g.cardSummary.onG5();
        System.out.println(toJSONString(g));

        GamblingData g2 = fromBinString(toBinString(g));
        if(g2 == null){
            return;
        }

        System.out.println(g2.rollSummary.getSummaryHistorys());
        System.out.println(g2.cardSummary.getYuanYangPeriod());

    }
}
