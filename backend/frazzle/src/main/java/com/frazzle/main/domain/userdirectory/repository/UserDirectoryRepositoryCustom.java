package com.frazzle.main.domain.userdirectory.repository;

import com.frazzle.main.domain.user.entity.User;

import java.util.List;

public interface UserDirectoryRepositoryCustom {

    List<Integer> findDirectoryIdByUserAndIsAccept(User user, boolean isAccept);
}
