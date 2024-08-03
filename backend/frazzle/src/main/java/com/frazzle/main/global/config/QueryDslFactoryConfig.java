package com.frazzle.main.global.config;

import com.frazzle.main.domain.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslFactoryConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){

        return new JPAQueryFactory(em);
    }

    @Bean
    public QBoard qBoard() {
        return new QBoard("board");
    }
}