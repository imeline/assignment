package org.example.springbootexample.service;

import org.example.springbootexample.dto.BoardDto;
import org.example.springbootexample.model.Board;
import org.example.springbootexample.repository.BoardRepository;
import org.example.springbootexample.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시판 서비스 구현 클래스
 * - 게시판 관련 비즈니스 로직을 구현합니다.
 */
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 생성자 주입
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    /**
     * 게시글 등록
     */
    @Override
    @Transactional
    public Long createBoard(BoardDto.Request request, Long userId) {
        // 사용자 존재 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 게시글 객체 생성
        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setUserId(userId);

        // 게시글 저장
        Board savedBoard = boardRepository.save(board);

        return savedBoard.getId();
    }

    /**
     * 게시글 수정
     */
    @Override
    @Transactional
    public BoardDto.DetailResponse updateBoard(Long boardId, BoardDto.Request request, Long userId) {
        // 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!board.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        // 게시글 수정
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());

        // 게시글 저장
        Board updatedBoard = boardRepository.save(board);

        // 응답 DTO 반환
        return new BoardDto.DetailResponse(
                updatedBoard.getId(),
                updatedBoard.getTitle(),
                updatedBoard.getContent(),
                updatedBoard.getUsername(),
                updatedBoard.getUserId(),
                updatedBoard.getViewCount(),
                updatedBoard.getCreatedAt(),
                updatedBoard.getUpdatedAt()
        );
    }

    /**
     * 게시글 삭제
     */
    @Override
    @Transactional
    public boolean deleteBoard(Long boardId, Long userId) {
        // 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!board.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }

        // 게시글 삭제
        return boardRepository.deleteById(boardId);
    }

    /**
     * 게시글 상세 조회
     */
    @Override
    @Transactional
    public BoardDto.DetailResponse getBoardDetail(Long boardId) {
        // 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 실전 예제!!!!
        // 조회수 증가
        boardRepository.incrementViewCount(boardId);
        board.setViewCount(board.getViewCount() + 1);

        // 응답 DTO 반환
        return new BoardDto.DetailResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUsername(),
                board.getUserId(),
                board.getViewCount(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    /**
     * 게시글 목록 조회
     */
    @Override
    public List<BoardDto.ListResponse> getBoardList(int page, int size) {
        // 페이징 처리
        int offset = page * size;

        // 게시글 목록 조회
        List<Board> boards = boardRepository.findAll(offset, size);

        // 응답 DTO 목록으로 변환
        return boards.stream()
                .map(board -> new BoardDto.ListResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getUsername(),
                        board.getViewCount(),
                        board.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 사용자별 게시글 목록 조회
     */
    @Override
    public List<BoardDto.ListResponse> getBoardListByUserId(Long userId, int page, int size) {
        // 사용자 존재 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 페이징 처리
        int offset = page * size;

        // 사용자별 게시글 목록 조회
        List<Board> boards = boardRepository.findByUserId(userId, offset, size);

        // 응답 DTO 목록으로 변환
        return boards.stream()
                .map(board -> new BoardDto.ListResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getUsername(),
                        board.getViewCount(),
                        board.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 전체 게시글 수 조회
     */
    @Override
    public int getTotalBoardCount() {
        return boardRepository.count();
    }

    /**
     * 사용자별 게시글 수 조회
     */
    @Override
    public int getTotalBoardCountByUserId(Long userId) {
        // 사용자 존재 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return boardRepository.countByUserId(userId);
    }
}

