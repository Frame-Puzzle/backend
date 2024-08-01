package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //Board findBoardByBoardId(int boardId);

}
