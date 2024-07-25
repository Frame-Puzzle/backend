package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findUsersByEmail(String email, Directory directory);
}
