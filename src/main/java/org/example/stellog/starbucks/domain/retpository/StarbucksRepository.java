package org.example.stellog.starbucks.domain.retpository;

import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarbucksRepository extends JpaRepository<Starbucks, Long> {
}
