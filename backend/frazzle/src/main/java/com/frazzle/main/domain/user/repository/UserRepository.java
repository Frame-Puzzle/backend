package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(Long id);

    User findByRefreshToken(String refreshToken);

    //업데이트시 영속성 컨텍스트를 자동으로 정리해 일치하게함
    @Modifying(clearAutomatically = true)
    @Transactional
    //업데이트 jpql쿼리
    @Query("update User u set u.userId =:#{#user.userId}, u.email =:#{#user.email}, u.socialType =:#{user.socialType}")
    int updateUser(@Param("user") User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.refreshToken =:#{#user.refreshToken} where u.userId=:#{#user.userId}")
    int updateRefreshToken(@Param("user") User user);


}
