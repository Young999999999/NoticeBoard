package com.codingrecipe.BootNoticeBoard.repository;

import com.codingrecipe.BootNoticeBoard.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> { // Entity 클래스의 PK 타입
    //select나 정렬은 jpa로 자동 쿼리를 잘만들어줌
    // 조회수 증가같은 특수 목적의 쿼리들은 따로 만들어줘야함.

    // update board_table set board_hits = board_hits+1 where id = ?
    @Modifying //update나 Delete 쿼리라면 모디파잉 어노테이션을 필수로 붙임 !
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") //엔티티 기준 쿼리 작성
    void updateHits(@Param("id") Long id);



}





