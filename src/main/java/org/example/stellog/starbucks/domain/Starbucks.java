package org.example.stellog.starbucks.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "starbucks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Starbucks {
    @Id
    @Column(name = "starbucks_id")
    private Long id;

    private String name;
    private String address;
    private double latitude;
    private double longitude;

    @Builder
    private Starbucks(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}