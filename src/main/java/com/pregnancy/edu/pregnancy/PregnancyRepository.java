package com.pregnancy.edu.pregnancy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PregnancyRepository extends JpaRepository<Pregnancy, Long> {
    List<Pregnancy> findByUserId(Long userId);

    Page<Pregnancy> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Pregnancy p WHERE p.user.id = :userId AND p.status = :status")
    List<Pregnancy> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    boolean existsByUserIdAndStatus(Long userId, String status);

}
