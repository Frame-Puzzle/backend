package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
