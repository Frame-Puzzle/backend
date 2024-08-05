package com.frazzle.main.domain.userdirectory.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDirectoryRepository extends JpaRepository<UserDirectory, Integer>, UserDirectoryRepositoryCustom {

    boolean existsByDirectoryAndUserAndIsAccept(Directory directory, User user, boolean isAccept);
    Optional<UserDirectory> findByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(int userId, int directoryId, boolean isAccept);
    void deleteByUser(User user);
    void deleteByDirectory(Directory directory);
    boolean existsByUserAndDirectory(User user, Directory directory);
    void deleteByUserAndDirectory(User user, Directory directory);
    boolean existsByDirectoryAndIsAccept(Directory directory, boolean isAccept);
    List<UserDirectory> findByUser(User user);
    List<UserDirectory> findByDirectory(Directory directory);
    @Override
    List<Integer> findDirectoryIdByUserAndIsAccept(User user, boolean isAccept);
}
