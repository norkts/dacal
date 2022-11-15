package com.norkts.dacal.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@Configuration
@MapperScan(sqlSessionTemplateRef =
        "sqlSessionTemplate")
public class DbConfig {


    private String dataSourceUrl;

    @Bean("dataSource")
    public DataSource dataSource(){

        org.sqlite.SQLiteConfig config = new org.sqlite.SQLiteConfig();
        config.setReadOnly(true);
        config.setPageSize(4096); //in bytes
        config.setCacheSize(2000); //number of pages
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        config.setJournalMode(SQLiteConfig.JournalMode.OFF);
        config.setReadOnly(false);

        SQLiteDataSource dataSource = new SQLiteConnectionPoolDataSource(config);
        try {
            String url = ResourceUtils.getURL("classpath:db/dacal.db").getPath();
            dataSource.setUrl("jdbc:sqlite:" + url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return dataSource;
    }


    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Value("classpath:db/mybatis-config.xml") org.springframework.core.io.Resource resource
            , @Qualifier("dataSource") DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(resource);
        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactoryBean){
        return new SqlSessionTemplate(sqlSessionFactoryBean);
    }

}
