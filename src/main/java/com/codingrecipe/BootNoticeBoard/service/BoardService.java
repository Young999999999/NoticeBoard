package com.codingrecipe.BootNoticeBoard.service;

import com.codingrecipe.BootNoticeBoard.dto.BoardDTO;
import com.codingrecipe.BootNoticeBoard.entity.BoardEntity;
import com.codingrecipe.BootNoticeBoard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


// DTO ->Entity (Enitty Class)
// Entity -> DTO   (DTO class)로 변환하는 작업이 주로 이루어진다.

//컨트롤러로부터 호출받을 때는 DTO로 넘겨 받는다.
//리파지토리로 넘겨줄 때는 엔티티로 넘겨준다.
//글 조회할 때는 리파지토리로부터 엔티티를 받는다.

//엔티티는 디비와 직접적 연관이 있기 떄문에 VIEW단으로 넘기면 안된다. 서비스단까지만 오도록 설계해야한다.

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository; // 생성자 주입 방식
    //repository는 entity 클래스만 받아준다.

    public void save(BoardDTO boardDTO){
        BoardEntity saveEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(saveEntity);

    }

    public List<BoardDTO> findAll(){
        //리파지토리로부터 데이터가 오면 Entity임 그래서 리스트형태의 Entity가 넘어오게 됨
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        //Entity -> DTO로 바꿔줘야함.
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity: boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    @Transactional // 영속성 컨텍스트 처리 조회수 업데이트
    public void updateHits(Long id){
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id){
        //없을 수도 있으니까 Optional로 처리하는 거 같음.
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }

    }

    // save를 통해서 update를 한다. 근데 insert도 save를 통해서 했다. 어떻게 update와 insert를 구분할까 ? \
    // ID 값의 유무로 판단을한다. ID 값이 있다면 update일 것이다.
    // BoardEntity.toSaveEntity를 보면 ID 값이 없다. 이건 insert이다 !
    public BoardDTO update(BoardDTO boardDTO){
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);

        return findById(boardEntity.getId());
    }


    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //인자로 사용되는 page는 인덱스처럼 0부터 시작함 그래서 -1
        int pageLimit = 3; //한 페이지에 보여줄 갯수
        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬하라.
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        //DTO로 바꿔야한다. Page -> List로 변환해서 반환해도 되지만 이러면 아래의 메소드를 사용하지 못한다.


        /*
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부
        */

        // 목록에서 보여줄 것 : id, writer, title, hits, createdTime
        //board는 게시글 엔티티라고 생각하면 될 듯
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(),board.getBoardWriter(),board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime())); // For each처럼  DTO로 변환해줌
        return boardDTOS;

    }

}
