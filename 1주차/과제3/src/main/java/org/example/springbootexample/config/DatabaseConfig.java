package org.example.springbootexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * JdbcTemplate 빈 설정
     * - Spring의 JdbcTemplate을 사용하여 데이터베이스 작업을 수행합니다.
     * - SQL 쿼리 실행 및 결과 매핑을 간소화합니다.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 트랜잭션 매니저 설정
     * - 데이터베이스 트랜잭션을 관리합니다.
     * - @Transactional 어노테이션을 사용하여 트랜잭션을 선언적으로 관리할 수 있습니다.
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

