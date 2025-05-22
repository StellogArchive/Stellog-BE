package org.example.stellog.starbucks.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarbucksRouteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequenceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starbucks_route_id")
    private StarbucksRoute starbucksRoute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starbucks_id")
    private Starbucks starbucks;

    @Builder
    private StarbucksRouteItem(int sequenceOrder, StarbucksRoute starbucksRoute, Starbucks starbucks) {
        this.sequenceOrder = sequenceOrder;
        this.starbucksRoute = starbucksRoute;
        this.starbucks = starbucks;
    }

    public void updateSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}

