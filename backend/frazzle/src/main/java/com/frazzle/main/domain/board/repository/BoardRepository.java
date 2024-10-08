package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, BoardRepositoryCustom {

    Optional<Board> findByBoardId(int boardId);

    List<Board> findByDirectoryDirectoryId(int directoryId);

    @Override
    List<Board> findBoards(int directoryId);

    @Override
    void deleteBoardByDirectory(Directory directory);

    @Override
    Optional<String> findThumbnailUrlByBoardId(int boardId);

    List<Board> findAllByUser(User user);
}
