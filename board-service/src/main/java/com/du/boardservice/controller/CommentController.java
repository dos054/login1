package com.du.boardservice.controller;

import com.du.boardservice.model.Comment;
import com.du.boardservice.service.CommentService;
import com.du.boardservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    
    public CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }
    
    // 특정 게시글의 댓글 목록 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Comment>> getCommentsByBoardId(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getCommentsByBoardId(boardId));
    }
    
    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        try {
            Comment comment = new Comment();
            comment.setBoardId(request.getBoardId());
            comment.setContent(request.getContent());
            comment.setAuthor(request.getAuthor());
            
            Comment savedComment = commentService.createComment(comment);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "댓글이 작성되었습니다.");
            response.put("commentId", savedComment.getId());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 댓글 삭제 (JWT에서 role 추출하여 ADMIN 또는 본인만 삭제 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Bearer 토큰에서 JWT 추출
            String token = authHeader.replace("Bearer ", "");
            
            // JWT에서 userId, role 추출
            String userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            
            commentService.deleteComment(id, userId, role);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "댓글이 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 특정 작성자의 댓글 목록 조회
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Comment>> getCommentsByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(commentService.getCommentsByAuthor(author));
    }
    
    // 댓글 생성 요청 DTO
    public static class CreateCommentRequest {
        private Long boardId;
        private String content;
        private String author;
        
        public Long getBoardId() { return boardId; }
        public void setBoardId(Long boardId) { this.boardId = boardId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
    }
}
