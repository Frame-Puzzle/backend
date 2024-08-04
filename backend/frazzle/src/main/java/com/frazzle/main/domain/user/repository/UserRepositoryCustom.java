package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserProfileRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findUsersByEmail(String email, Directory directory);

    Long updateUser(User findUser ,User updateUser);

    Long updateRefreshToken(User updateUser, String refreshToken);

    List<User> findDirectoryUsers(Directory directory);
}
