package org.example.springbootexample.repository;

import org.example.springbootexample.model.Board;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 게시판 리포지토리 구현 클래스 (H2 DB 대응 리팩토링 완료)
 */
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: Board 객체 매핑
    private final RowMapper<Board> boardRowMapper = (rs, rowNum) -> {
        Board board = new Board();
        board.setId(rs.getLong("id"));
        board.setTitle(rs.getString("title"));
        board.setContent(rs.getString("content"));
        board.setUserId(rs.getLong("user_id"));
        board.setViewCount(rs.getInt("view_count"));
        board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        board.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        // username이 조인 결과로 올 경우
        try {
            board.setUsername(rs.getString("username"));
        } catch (Exception ignored) {
        }

        return board;
    };

    public BoardRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board save(Board board) {
        if (board.getId() == null) {
            return insertBoard(board);
        } else {
            return updateBoard(board);
        }
    }

    /**
     * 게시글 등록 (H2 DB 대응 KeyHolder 처리)
     */
    private Board insertBoard(Board board) {
        String sql = "INSERT INTO boards (title, content, user_id, view_count, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContent());
            ps.setLong(3, board.getUserId());
            ps.setInt(4, 0); // 조회수 초기값
            ps.setTimestamp(5, Timestamp.valueOf(now));
            ps.setTimestamp(6, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        // 키 추출 (H2 DB는 keys로 내려옴)
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null) {
            Object idObj = keys.getOrDefault("ID", keys.get("id"));
            if (idObj instanceof Number) {
                board.setId(((Number) idObj).longValue());
            } else {
                throw new IllegalStateException("Failed to retrieve generated board ID");
            }
        } else {
            throw new IllegalStateException("Failed to retrieve generated board key");
        }

        board.setViewCount(0);
        board.setCreatedAt(now);
        board.setUpdatedAt(now);

        return board;
    }

    /**
     * 게시글 업데이트
     */
    private Board updateBoard(Board board) {
        String sql = "UPDATE boards SET title = ?, content = ?, updated_at = ? WHERE id = ?";
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql,
                board.getTitle(),
                board.getContent(),
                Timestamp.valueOf(now),
                board.getId());

        board.setUpdatedAt(now);
        return board;
    }

    @Override
    public Optional<Board> findById(Long id) {
        String sql = "SELECT b.*, u.username FROM boards b " +
                "JOIN users u ON b.user_id = u.id " +
                "WHERE b.id = ?";
        try {
            Board board = jdbcTemplate.queryForObject(sql, boardRowMapper, id);
            return Optional.ofNullable(board);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Board> findAll(int offset, int limit) {
        String sql = "SELECT b.*, u.username FROM boards b " +
                "JOIN users u ON b.user_id = u.id " +
                "ORDER BY b.created_at DESC " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, boardRowMapper, limit, offset);
    }

    @Override
    public List<Board> findByUserId(Long userId, int offset, int limit) {
        String sql = "SELECT b.*, u.username FROM boards b " +
                "JOIN users u ON b.user_id = u.id " +
                "WHERE b.user_id = ? " +
                "ORDER BY b.created_at DESC " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, boardRowMapper, userId, limit, offset);
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM boards WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    //실전 예제!!!!!
    @Override
    public void incrementViewCount(Long id) {
        String sql = "UPDATE boards SET view_count = view_count + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM boards";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM boards WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }
}