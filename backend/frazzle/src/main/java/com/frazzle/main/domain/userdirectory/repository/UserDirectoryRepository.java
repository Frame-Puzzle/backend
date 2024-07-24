package com.frazzle.main.domain.userdirectory.repository;

import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDirectoryRepository extends JpaRepository<UserDirectory, Integer> {


}
