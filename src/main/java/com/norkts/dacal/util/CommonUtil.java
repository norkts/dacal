package com.norkts.dacal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Maps;
import com.norkts.dacal.domain.Config;
import com.norkts.dacal.domain.GamblingData;
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
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public static void main(String[] args) {

        GamblingData g = pasreObject("{\"rollSummary\":{\"summaryHistorys\":[],\"g2Num\":0,\"g5Num\":0,\"g10Num\":2,\"g100Num\":0,\"g2AfterG5Num\":0,\"g2AfterG10Num\":0,\"g5AfterG10Num\":0,\"g10AfterG100Num\":2,\"lastG2Time\":0,\"lastG5Time\":0,\"lastG10Time\":1668508623956,\"lastG100Time\":0,\"summary\":\"0-2-0-0\",\"g2Count\":\"0\",\"lastGiftTime\":\"00:56\"},\"planetSummary\":{\"g1Num\":0,\"g2Num\":0,\"g10Num\":0,\"mg10Num\":0,\"tg10Num\":0,\"bg10Num\":0,\"g1afterG2\":0,\"g1afterbG10\":0,\"g2aftertG10\":0,\"lastG1Time\":0,\"lastG2Time\":0,\"lastG10Time\":\"00:00,00:00\",\"lastbG10Time\":0,\"lasttG10Time\":0,\"lastmG10Time\":0,\"summary\":\"0-0\"},\"cardSummary\":{\"g3Num\":0,\"g10Num\":0,\"g50Num\":0,\"lastG3Time\":0,\"lastG10Time\":0,\"lastG50Time\":0,\"lastG5time\":0,\"g5TimePeriod\":0,\"bigCardSummaryHistorys\":[],\"yuanYangSummaryHistorys\":[],\"yuanYangPeriod\":\"00:00\",\"yuanYangTime\":\"00:00\",\"bigCardSummary\":\"0-0\"}}"
                ,GamblingData.class);

        System.out.println(toJSONString(g));
    }
}
