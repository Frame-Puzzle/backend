package com.frazzle.main.global.openvidu.repository;

import com.frazzle.main.global.openvidu.entity.Openvidu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpenviduRepository extends JpaRepository<Openvidu, Integer> {
    Optional<Openvidu> findByBoardId(int boardId);
}
