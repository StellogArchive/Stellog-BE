package org.example.stellog.starbucks.domain.repository;

import org.example.stellog.starbucks.domain.StarbucksRoute;
import org.example.stellog.starbucks.domain.StarbucksRouteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarbucksRouteItemRepository extends JpaRepository<StarbucksRouteItem, Long> {
    List<StarbucksRouteItem> findByStarbucksRouteOrderBySequenceOrder(StarbucksRoute route);

    void deleteAllByStarbucksRoute(StarbucksRoute route);

    List<StarbucksRouteItem> findByStarbucksRoute(StarbucksRoute route);
}
