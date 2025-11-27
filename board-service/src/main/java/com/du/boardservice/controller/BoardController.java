package com.du.boardservice.controller;

import com.du.boardservice.model.Board;
import com.du.boardservice.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    
    private final BoardService boardService;
    
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    
    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<Board>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
    
    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        try {
            Board board = boardService.getBoardById(id);
            return ResponseEntity.ok(board);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody CreateBoardRequest request) {
        try {
            Board board = new Board();
            board.setTitle(request.getTitle());
            board.setContent(request.getContent());
            board.setAuthor(request.getAuthor());
            
            Board savedBoard = boardService.createBoard(board);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "게시글이 작성되었습니다.");
            response.put("boardId", savedBoard.getId());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long id,
            @RequestBody UpdateBoardRequest request) {
        try {
            Board updatedBoard = new Board();
            updatedBoard.setTitle(request.getTitle());
            updatedBoard.setContent(request.getContent());
            
            Board board = boardService.updateBoard(id, updatedBoard, request.getAuthor());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시글이 수정되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable Long id,
            @RequestParam String author) {
        try {
            boardService.deleteBoard(id, author);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시글이 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 상단 고정 토글 (관리자 기능)
    @PostMapping("/{id}/pin")
    public ResponseEntity<?> togglePin(@PathVariable Long id) {
        try {
            Board board = boardService.togglePin(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", board.getIsPinned() ? "게시글이 상단에 고정되었습니다." : "게시글 고정이 해제되었습니다.");
            response.put("isPinned", board.getIsPinned());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBoard(@PathVariable Long id) {
        try {
            boardService.increaseLikeCount(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "좋아요!");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 특정 작성자의 게시글 조회
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Board>> getBoardsByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(boardService.getBoardsByAuthor(author));
    }
    
    // DTO 클래스들
    public static class CreateBoardRequest {
        private String title;
        private String content;
        private String author;
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
    }
    
    public static class UpdateBoardRequest {
        private String title;
        private String content;
        private String author;
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
    }
}
