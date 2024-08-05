package com.frazzle.main.domain.directory.entity;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "directories")
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "directory_id")
    private int directoryId;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "directory_name", nullable = false, length = 32)
    private String directoryName;

    @Column(name = "category", nullable = false, length = 12)
    private String category;

    @Column(name = "people_number", nullable = false)
    private int peopleNumber;

    @Column(name = "board_number", nullable = false)
    private int boardNumber;

    @PrePersist
    @PreUpdate
    private void validatedirectoryName() {
        if (directoryName != null) {
            int length = 0;
            int maxLength = 20;

            for (char c : directoryName.toCharArray()) {
                if (c >= 0xAC00 && c <= 0xD7A3) {
                    // 한글은 2 길이로 계산
                    length += 1;
                }
                length += 1;
            }

            if (length > maxLength) {
                throw new CustomException(ErrorCode.TOO_MAX_LENGTH_INPUT);
            }
        }
    }

    @Builder
    private Directory(String directoryName, String category, int peopleNumber) {
        this.directoryName = directoryName;
        this.category = category;
        this.peopleNumber = peopleNumber;
    }

    public static Directory createDirectory(CreateDirectoryRequestDto requestDto) {
        return Directory.builder()
                .directoryName(requestDto.getDirectoryName())
                .category(requestDto.getCategory())
                .peopleNumber(1)
                .build();
    }

    public void changeDirectoryName(String newDirectoryName) {
        this.directoryName = newDirectoryName;
    }

    public void changePeopleNumber(int number) {
        this.peopleNumber += number;
    }

    public void addBoardNumber() { this.boardNumber += 1; }

    public void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
