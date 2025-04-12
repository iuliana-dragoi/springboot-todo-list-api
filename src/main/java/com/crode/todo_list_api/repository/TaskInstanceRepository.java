package com.crode.todo_list_api.repository;

import com.crode.todo_list_api.model.TaskInstance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TaskInstanceRepository extends JpaRepository<TaskInstance, Long> {

    Optional<TaskInstance> findByTaskIdAndDate(Long taskId, LocalDateTime date);

    @Query("SELECT ti FROM TaskInstance ti WHERE ti.task.id = :taskId AND ti.date >= :start AND ti.date < :end")
    Optional<TaskInstance> findByTaskIdAndDateRange(@Param("taskId") Long taskId,
                                                    @Param("start") LocalDateTime startOfDay,
                                                    @Param("end") LocalDateTime endOfDay);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskInstance ti WHERE ti.task.id = :taskId AND (ti.date < :startDate OR ti.date > :endDate)")
    void deleteOutsideDateRange(@Param("taskId") Long taskId,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(ti) FROM TaskInstance ti WHERE ti.task.id = :taskId AND ti.completed = true")
    long countCompletedByTaskId(@Param("taskId") Long taskId);
}
