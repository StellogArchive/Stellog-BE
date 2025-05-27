package org.example.stellog.badge.domain.repository;

import org.example.stellog.badge.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
