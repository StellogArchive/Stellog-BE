package org.example.stellog.starbucks.domain.repository;

import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarbucksRepository extends JpaRepository<Starbucks, Long> {
}
