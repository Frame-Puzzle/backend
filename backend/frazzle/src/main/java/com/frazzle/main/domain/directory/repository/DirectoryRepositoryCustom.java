package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepositoryCustom {

    List<Directory> findMyDirectory(User user, String category);

    Optional<Directory> findByBoardId(int boardId);
}
