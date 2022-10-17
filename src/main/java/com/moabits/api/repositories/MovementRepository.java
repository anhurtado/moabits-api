package com.moabits.api.repositories;

import com.moabits.api.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query(value = "SELECT m.balance FROM movements m " +
        "WHERE m.client_id = :clientId " +
        "ORDER BY m.id DESC LIMIT 1", nativeQuery = true)
    Double findCurrentBalanceByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT m.balance FROM movements m " +
        "WHERE m.client_id = :clientId AND m.created_at BETWEEN :createdAtStart AND :createdAtEnd " +
        "ORDER BY m.id DESC LIMIT 1", nativeQuery = true)
    Double findCurrentBalanceByClientIdAndDates(
        @Param("clientId") Long clientId,
        @Param("createdAtStart") Date createdAtStart,
        @Param("createdAtEnd") Date createdAtEnd);

    @Query(value = "SELECT m.id, m.account_number, m.amount, m.created_at, m.balance, mt.name FROM movements m " +
        "JOIN movement_types mt ON m.movement_type_id = mt.id " +
        "WHERE m.client_id = :clientId " +
        "ORDER BY m.id DESC OFFSET :page LIMIT :limit", nativeQuery = true)
    List<Map<String, ?>> findAllByClientIdAndPaginate(
        @Param("clientId") Long clientId,
        @Param("limit") Long limit,
        @Param("page") Long page);

    @Query(value = "SELECT count(m.id) total FROM movements m WHERE m.client_id = :clientId", nativeQuery = true)
    Long getTotalByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT m.id, m.account_number, m.amount, m.created_at, m.balance, mt.name FROM movements m " +
        "JOIN movement_types mt ON m.movement_type_id = mt.id " +
        "WHERE m.client_id = :clientId AND m.created_at BETWEEN :createdAtStart AND :createdAtEnd " +
        "ORDER BY m.id DESC OFFSET :page LIMIT :limit", nativeQuery = true)
    List<Map<String, ?>> findAllByClientIdAndDatesAndPaginate(
        @Param("clientId") Long clientId,
        @Param("createdAtStart") Date createdAtStart,
        @Param("createdAtEnd") Date createdAtEnd,
        @Param("limit") Long limit,
        @Param("page") Long page);

    @Query(value = "SELECT count(m.id) total FROM movements m WHERE m.client_id = :clientId AND m.created_at BETWEEN :createdAtStart AND :createdAtEnd", nativeQuery = true)
    Long getTotalByClientIdAndDates(
        @Param("clientId") Long clientId,
        @Param("createdAtStart") Date createdAtStart,
        @Param("createdAtEnd") Date createdAtEnd);
}
