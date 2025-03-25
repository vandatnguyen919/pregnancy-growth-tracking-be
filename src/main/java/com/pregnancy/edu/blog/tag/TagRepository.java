package com.pregnancy.edu.blog.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByNameIgnoreCase(String name);
}
