
package com.fu.cinema.repository;

import com.fu.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
            SELECT t.seatCode
            FROM Ticket t
            JOIN t.booking b
            JOIN b.showtime s
            WHERE s.showtimeId = :showtimeId
              AND b.status <> com.fu.cinema.enums.BookingStatus.CANCELLED
            """)
    List<String> findSoldSeatCodes(
            @Param("showtimeId") Long showtimeId);
}
