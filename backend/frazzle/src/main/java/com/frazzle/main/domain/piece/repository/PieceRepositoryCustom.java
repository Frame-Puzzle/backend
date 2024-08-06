package com.frazzle.main.domain.piece.repository;

import com.frazzle.main.domain.directory.entity.Directory;

public interface PieceRepositoryCustom {

    void deletePieceByDirectory(int directoryId);

    void nullifyUserInPiecesByDirectoryAndUser(int userId, int directoryId);
}
