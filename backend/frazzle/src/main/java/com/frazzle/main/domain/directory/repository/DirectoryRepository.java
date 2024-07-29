package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Integer>, DirectoryRepositoryCustom {

    Optional<Directory> findByDirectoryId(int directoryId);

    @Override
    long updateNameByDirectoryId(int directoryId, String directoryName);
}
