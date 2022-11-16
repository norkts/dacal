package com.norkts.dacal.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * DB通用新增、删除操作
 */
@Mapper
public interface CommonMapper {

    /**
     * 数据新增
     * @param dataInfo
     * @return
     */
    Integer insert(@Param("table") String table, @Param("dataInfo") Map<String,Object> dataInfo);

    /**
     * 数据插入
     * @param table
     * @param entityIdKey
     * @param entityIdValue
     * @param dataInfo
     * @return
     */
    Integer update(@Param("table") String table
            , @Param("entityIdKey") String entityIdKey
            , @Param("entityIdValue") Long entityIdValue
            , @Param("dataInfo") Map<String,Object> dataInfo);

    /**
     * 数据删除
     * @param table
     * @param entityIdKey
     * @param ids
     * @return
     */
    Integer deleteByIds(@Param("table") String table
            , @Param("entityIdKey") String entityIdKey
            , @Param("ids") List<Long> ids);


    List<Map<String,Object>> queryDataByWhere(@Param("table") String table
            , @Param("where") String where
            , @Param("start") Long start
            , @Param("limit") Long limit);

    @Select("SELECT * FROM ${table} WHERE  ${entityIdKey}=#{entityId}")
    Map<String, Object> getData(@Param("table") String table
            , @Param("entityIdKey") String entityIdKey
            , @Param("entityId") Long entityId);

    @Select("${sql}")
    Object execute(@Param("sql") String sql);
}
