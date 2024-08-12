package com.frazzle.main.domain.piece.service;

import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.global.utils.FindPeopleCountFromImg;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final FindPeopleCountFromImg findPeopleCountFromImg;
    private final PieceRepository pieceRepository;

    @Async
    @Transactional
    public void findPeople(MultipartFile imgFile, Piece piece) {
        //3. Face Detection : 사람 수 파악
        int peopleCount = findPeopleCountFromImg.analyzeImageFile(imgFile);
        piece.updatePeopleCount(peopleCount);
        pieceRepository.save(piece);
    }
}
