package com.cobra.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@MapperScan(basePackages = "com.cobra.mapper", sqlSessionTemplateRef = "dataUserSqlSessionTemplate")
public class UserDataSourceConfig
{

    @Bean(name = "dataUserDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource setDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dataUserTransactionManager")
    public DataSourceTransactionManager setTransactionDataSourceManager(@Qualifier("dataUserDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "dataUserSqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("dataUserDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*.xml"));
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);


        return bean.getObject();
    }

    @Bean(name = "dataUserSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("dataUserSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
