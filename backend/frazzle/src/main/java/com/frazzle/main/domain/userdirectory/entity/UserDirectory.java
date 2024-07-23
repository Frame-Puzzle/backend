package com.frazzle.main.domain.userdirectory.entity;

import com.frazzle.main.domain.directory.entity.Directory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_directories")
public class UserDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_directory_id")
    private int userDirectoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    private UserDirectory(Directory directory, User user) {
//        this.directory = directory;
//        this.user = user;
//    }

//    public static UserDirectory createUserDirectory(Directory directory, User user) {
//        return new UserDirectory(directory, user);
//    }
}
