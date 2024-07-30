package com.frazzle.main.domain.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Board {

    @Id @GeneratedValue
    private int board_id;
}
