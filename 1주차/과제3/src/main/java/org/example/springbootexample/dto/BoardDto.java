package org.example.springbootexample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 게시판 DTO 클래스
 * - 클라이언트와 서버 간의 데이터 전송에 사용됩니다.
 * - 입력 유효성 검사를 위한 어노테이션이 포함되어 있습니다.
 */
public class BoardDto {

    // 게시글 등록/수정 요청 DTO
    public static class Request {
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 최대 200자까지 입력 가능합니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        // Getter와 Setter
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // 게시글 목록 응답 DTO
    public static class ListResponse {
        private Long id;
        private String title;
        private String username;
        private int viewCount;
        private String createdAt;

        public ListResponse(Long id, String title, String username, int viewCount, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.username = username;
            this.viewCount = viewCount;
            this.createdAt = formatDateTime(createdAt);
        }

        // Getter 메서드
        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUsername() {
            return username;
        }

        public int getViewCount() {
            return viewCount;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    // 게시글 상세 응답 DTO
    public static class DetailResponse {
        private Long id;
        private String title;
        private String content;
        private String username;
        private Long userId;
        private int viewCount;
        private String createdAt;
        private String updatedAt;

        public DetailResponse(Long id, String title, String content, String username, Long userId,
                              int viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
            this.userId = userId;
            this.viewCount = viewCount;
            this.createdAt = formatDateTime(createdAt);
            this.updatedAt = formatDateTime(updatedAt);
        }

        // Getter 메서드
        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getUsername() {
            return username;
        }

        public Long getUserId() {
            return userId;
        }

        public int getViewCount() {
            return viewCount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }

    // 날짜 포맷팅 유틸리티 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
