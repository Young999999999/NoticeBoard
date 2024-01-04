package com.codingrecipe.BootNoticeBoard.entity;

// DB의 테이블 역할을 하는 클래스.
// spring data jpa 를 쓰면 무조건 써야함
// DB와 직접적인 연관이 있기 때문에 service와 repository에서만 사용함

import com.codingrecipe.BootNoticeBoard.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_table") // 필수는 아님 따로 이름을 설정하는 거임
// 스프링 데이터 JPA 를 사용하면 별도로 DB에 테이블을 안만들어줘도 됨
public class BoardEntity extends BaseEntity{
    @Id // pk 컬럼 지정. pk니까 필수,y
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql 기준 auto_increment를 할 수 있음 오라클은 다르게 생김
    private Long id;

    @Column(length = 20, nullable = false) //컬럼의 크기값을 정할 수 있음. 널 값을 허용하지 않음
    private String boardWriter;

    @Column //따로 값 안 넣으면 크기 255, null 가능으로 됨
    private String boardPass;
    
    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    //시간은 따로 별도의 엔티티로 관리함.

    // ID가 없으므로 insert !!
    public static BoardEntity toSaveEntity(BoardDTO boardDTO){
        // DTO의 값을 Entity 객체로 옮기는 작업
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;


    }

    //ID가 있으므로 Update !!
    public static BoardEntity toUpdateEntity(BoardDTO boardDTO){

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;

    }



}
