package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.QBoard;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserProfileRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.entity.QUser;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.QUserDirectory;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private QUser user = QUser.user;
    private QUserDirectory userDirectory = QUserDirectory.userDirectory;
    private QBoard board = QBoard.board;

    //이메일로 찾기
    @Override
    public List<User> findUsersByEmail(String email, Directory directory) {
        JPQLQuery<User> subquery = JPAExpressions
                .select(userDirectory.user)
                .from(userDirectory)
                .where(userDirectory.directory.eq(directory));

        return queryFactory
                .select(user)
                .from(user)
                .where(user.email.like(email + "%")
                        .and(user.notIn(subquery)))
                .fetch();
    }

    @Override
    public Long updateUser(User findUser ,User updateUser) {
        //유저 정보를 이용해 업데이트
        Long result = queryFactory
                .update(user)
                .where(user.eq(findUser))
                .set(user.loginUserId, updateUser.getLoginUserId())
                .set(user.email, updateUser.getEmail())
                .set(user.refreshToken, updateUser.getRefreshToken())
                .execute();

        em.flush();
        em.clear();

        return result;
    }

    @Override
    //리프레시 토큰 업데이트
    public Long updateRefreshToken(User updateUser, String refreshToken) {
        Long result = queryFactory
                .update(user)
                .where(user.eq(updateUser))
                .set(user.refreshToken, refreshToken)
                .execute();

        em.flush();
        em.clear();


        return result;
    }

    @Override
    public List<User> findDirectoryUsers(Directory directory, boolean isAccept) {
        JPQLQuery<User> subQuery = JPAExpressions
                .select(userDirectory.user)
                .from(userDirectory)
                .where(userDirectory.directory.eq(directory)
                        .and(userDirectory.isAccept.eq(isAccept)));

        return queryFactory.selectFrom(user)
                .where(user.in(subQuery))
                .fetch();
    }

    @Override
    public List<User> findAllUserByBoardId(int boardId) {
        JPQLQuery<Directory> subQuery = JPAExpressions
                .select(board.directory)
                .from(board)
                .where(board.boardId.eq(boardId));


        return queryFactory.select(userDirectory.user)
                .from(userDirectory)
                .where(userDirectory.directory.in(subQuery)
                        .and(userDirectory.isAccept.eq(true)))
                .fetch();
    }


}
