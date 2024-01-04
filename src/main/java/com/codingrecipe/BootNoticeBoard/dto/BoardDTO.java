package com.codingrecipe.BootNoticeBoard.dto;
import com.codingrecipe.BootNoticeBoard.entity.BoardEntity;
import lombok.*;

import java.time.*;

/// DTO란 ? data Transfer Object 데이터를 전송할 때 사용하는 객체이다.
// Entity와 DTO는 목적이 다르다.
// DTO
// 무언가 주고받을 때 파라미터들이 여러가지가 있다 이걸 따로 따로 보내기에는 관리하기 쉽지않다
// 그래서 하나의 객체에 주고 받자.


// 각각의 필드에 따라서 게터 세터를 자동으로 만들어준다. Getter,Setter -> lombok
@Getter
@Setter
@ToString //필드값 확인을 위해서 사용한다.
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; // 글 조회수
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        return boardDTO;
    }


}
