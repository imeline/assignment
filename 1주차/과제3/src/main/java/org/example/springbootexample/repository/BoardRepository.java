package org.example.springbootexample.repository;

import org.example.springbootexample.model.Board;
import java.util.List;
import java.util.Optional;

/**
 * 게시판 리포지토리 인터페이스
 * - 게시판 데이터에 접근하는 메서드를 정의합니다.
 */
public interface BoardRepository {

    /**
     * 게시글 저장
     * @param board 저장할 게시글 객체
     * @return 저장된 게시글 객체 (ID 포함)
     */
    Board save(Board board);

    /**
     * ID로 게시글 조회
     * @param id 게시글 ID
     * @return 게시글 객체 (Optional로 래핑)
     */
    Optional<Board> findById(Long id);

    /**
     * 모든 게시글 조회 (페이징)
     * @param offset 시작 위치
     * @param limit 조회할 개수
     * @return 게시글 목록
     */
    List<Board> findAll(int offset, int limit);

    /**
     * 사용자 ID로 게시글 조회
     * @param userId 사용자 ID
     * @param offset 시작 위치
     * @param limit 조회할 개수
     * @return 게시글 목록
     */
    List<Board> findByUserId(Long userId, int offset, int limit);

    /**
     * 게시글 삭제
     * @param id 게시글 ID
     * @return 삭제 성공 여부
     */
    boolean deleteById(Long id);

    /**
     * 게시글 조회수 증가
     * @param id 게시글 ID
     */
    void incrementViewCount(Long id);

    /**
     * 전체 게시글 수 조회
     * @return 게시글 수
     */
    int count();

    /**
     * 사용자별 게시글 수 조회
     * @param userId 사용자 ID
     * @return 게시글 수
     */
    int countByUserId(Long userId);
}

