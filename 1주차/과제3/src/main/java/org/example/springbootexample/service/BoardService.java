package org.example.springbootexample.service;

import org.example.springbootexample.dto.BoardDto;
import java.util.List;

/**
 * 게시판 서비스 인터페이스
 * - 게시판 관련 비즈니스 로직을 정의합니다.
 */
public interface BoardService {

    /**
     * 게시글 등록
     * @param request 게시글 등록 요청 DTO
     * @param userId 작성자 ID
     * @return 등록된 게시글 ID
     */
    Long createBoard(BoardDto.Request request, Long userId);

    /**
     * 게시글 수정
     * @param boardId 게시글 ID
     * @param request 게시글 수정 요청 DTO
     * @param userId 사용자 ID (권한 확인용)
     * @return 수정된 게시글 상세 정보
     */
    BoardDto.DetailResponse updateBoard(Long boardId, BoardDto.Request request, Long userId);

    /**
     * 게시글 삭제
     * @param boardId 게시글 ID
     * @param userId 사용자 ID (권한 확인용)
     * @return 삭제 성공 여부
     */
    boolean deleteBoard(Long boardId, Long userId);

    /**
     * 게시글 상세 조회
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    BoardDto.DetailResponse getBoardDetail(Long boardId);

    /**
     * 게시글 목록 조회
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 게시글 목록
     */
    List<BoardDto.ListResponse> getBoardList(int page, int size);

    /**
     * 사용자별 게시글 목록 조회
     * @param userId 사용자 ID
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 게시글 목록
     */
    List<BoardDto.ListResponse> getBoardListByUserId(Long userId, int page, int size);

    /**
     * 전체 게시글 수 조회
     * @return 게시글 수
     */
    int getTotalBoardCount();

    /**
     * 사용자별 게시글 수 조회
     * @param userId 사용자 ID
     * @return 게시글 수
     */
    int getTotalBoardCountByUserId(Long userId);
}

