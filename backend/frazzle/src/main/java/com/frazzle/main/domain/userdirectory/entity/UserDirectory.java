package com.frazzle.main.domain.userdirectory.entity;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_directories", uniqueConstraints = {@UniqueConstraint(
        name = "USER_DIRECTORY_UNIQUE",
        columnNames = {"directory_id", "user_id"}
)})
public class UserDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_directory_id")
    private int userDirectoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_accept")
    private boolean isAccept;

    private UserDirectory(Directory directory, User user, boolean isAccept) {
        this.directory = directory;
        this.user = user;
        this.isAccept = isAccept;
    }

    public static UserDirectory createUserDirectory(Directory directory, User user, boolean isAccept) {
        return new UserDirectory(directory, user, isAccept);
    }

    public void updateAccept(Boolean isAccept) {
        this.isAccept = isAccept;
    }
}
