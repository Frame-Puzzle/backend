package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    User findByLoginUserId(String loginUserId);

    User findByUserId(int id);

    User findByRefreshToken(String refreshToken);

    Long deleteByUserId(int userId);

    Boolean existsByNickname(String nickname);

    @Override
    List<User> findUsersByEmail(String email, Directory directory);
}
