package com.frazzle.main.domain.userdirectory.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDirectoryRepository extends JpaRepository<UserDirectory, Integer> {

    boolean existsByDirectoryAndUserAndIsAccept(Directory directory, User user, boolean isAccept);
    boolean existsByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(int userId, int directoryId, boolean isAccept);
    Long deleteByUser(User user);
    boolean existsByUserAndDirectory(User user, Directory directory);
    void deleteByUserAndDirectory(User user, Directory directory);
    List<UserDirectory> findByUser(User user);
}
