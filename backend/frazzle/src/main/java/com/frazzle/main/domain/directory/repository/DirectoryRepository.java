package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Integer> {
}
