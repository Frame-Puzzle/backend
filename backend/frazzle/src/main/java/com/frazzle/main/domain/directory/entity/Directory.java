package com.frazzle.main.domain.directory.entity;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

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
}
