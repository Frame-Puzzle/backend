package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLoginUserId(String id);

    User findByRefreshToken(String refreshToken);

    //업데이트시 영속성 컨텍스트를 자동으로 정리해 일치하게함
    @Modifying(clearAutomatically = true)
    //업데이트 jpql쿼리
    @Transactional
    @Query("UPDATE User u SET u.loginUserId = :#{#user.loginUserId}, u.email = :#{#user.email}, u.socialType = :#{#user.socialType} WHERE u.loginUserId = :#{#user.loginUserId}")
    int updateUser(@Param("user") User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.refreshToken = :#{#user.refreshToken} WHERE u.loginUserId = :#{#user.loginUserId}")
    int updateRefreshToken(@Param("user") User user);


}
