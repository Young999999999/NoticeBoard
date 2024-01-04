package com.codingrecipe.BootNoticeBoard.controller;

import com.codingrecipe.BootNoticeBoard.dto.BoardDTO;
import com.codingrecipe.BootNoticeBoard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board") //대표주소 설정
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    //save html 과 필드값들이 동일하다면 스프링이 해당하는 세터를 알아서 호출해서 각각 값을 담아준다.
    // 일일히 파라미터 안받고도 간단하게 할 수 있다 !
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/") // 전체목록에서 DB로 가져와야함
    // 데이터를 가져올 때는 모델 객체를 사용한다.
    public String findAll(Model model){
        //여러개를 가져가니까 List
        //BoardDTO가 여러개 담겨있는 리스트이다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        //가져온 DTO를 모델에다가 담는다.
        model.addAttribute("boardList",boardDTOList);
        return "list"; //list html로 이동 !
    }


    //게시글 상세 조회의 경우 2가지를 고려해야함.
    // 1. 해당 게시글의 조회수를 하나 올리고
    // 2.게시글 데이터를 가져와서 detail.html에 출력.


    @GetMapping("/{id}")
    // 경로상의 값을 가져올 때 pathVariable을 사용 데이터를 담아가야하니 model을 사용
    public String findById(@PathVariable Long id, Model model){
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);

        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board",board);
        return "detail";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    //스프링에서 지원해주는 페이지 어노테이션
    //default를 page =1 로 줌으로써  /board/paging을 -> /board/paging?page=1  처럼 나오도록 만들었다.
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model){
        //pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages(); // 3 6 9 12


        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "paging";

    }



}
