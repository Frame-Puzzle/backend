package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Integer>, DirectoryRepositoryCustom {

    Optional<Directory> findByDirectoryId(int directoryId);

    @Override
    List<Directory> findMyDirectory(User user, String category);

    @Override
    Directory findByBoardId(int boardId);
}
