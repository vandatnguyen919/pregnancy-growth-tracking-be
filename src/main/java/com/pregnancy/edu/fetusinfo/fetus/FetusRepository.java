package com.pregnancy.edu.fetusinfo.fetus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FetusRepository extends JpaRepository<Fetus, Long> {
    List<Fetus> findAllByUserId(Long userId);
}
