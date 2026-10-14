
package com.fu.cinema.repository;

import com.fu.cinema.entity.Showtime;
import com.fu.cinema.enums.ShowtimeStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.fu.cinema.dto.RoomShowtimeCount;


public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // ===== TODO 9: Derived queries =====

    boolean existsByMovie_MovieId(Long movieId);

    boolean existsByRoom_RoomId(Long roomId);

    @EntityGraph(attributePaths = "room")
    List<Showtime> findByMovie_MovieIdAndStatusAndStartTimeAfterOrderByStartTimeAsc(
            Long movieId,
            ShowtimeStatus status,
            LocalDateTime startTime);

    // ===== TODO 10 - Q3: Tìm suất chiếu bị chồng giờ =====

    @Query("""
            SELECT s FROM Showtime s
            WHERE s.room.roomId = :roomId
              AND s.status <> com.fu.cinema.enums.ShowtimeStatus.CANCELLED
              AND s.startTime < :end
              AND s.endTime > :start
              AND (:excludeId IS NULL OR s.showtimeId <> :excludeId)
            """)
    List<Showtime> findOverlapping(
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("excludeId") Long excludeId);

    // ===== TODO 10: Khóa bản ghi suất chiếu khi cần cập nhật =====

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Showtime s WHERE s.showtimeId = :id")
    Optional<Showtime> findByIdForUpdate(@Param("id") Long id);

    // ===== TODO 10 - Q11: Đếm suất chiếu OPEN hôm nay theo phòng =====
    @Query(value = """
            SELECT r.room_name AS roomName, COUNT(s.showtime_id) AS showtimeCount
            FROM rooms r
            LEFT JOIN showtimes s
                   ON s.room_id = r.room_id
                  AND s.status = 'OPEN'
                  AND CAST(s.start_time AS DATE) = CAST(GETDATE() AS DATE)
            GROUP BY r.room_name
            ORDER BY r.room_name
            """, nativeQuery = true)
    List<RoomShowtimeCount> countTodayShowtimesByRoom();

}
