package org.example.stellog.starbucks.application;

import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StarbucksRouteOptimizer {

    public List<Starbucks> findOptimalRoute(List<Starbucks> locations) {
        if (locations == null || locations.size() <= 1) {
            return locations != null ? new ArrayList<>(locations) : Collections.emptyList();
        }

        // Step 1: 최근접 이웃으로 초기 경로 구성
        List<Starbucks> route = nearestNeighbor(locations);

        // Step 2: 2-opt로 경로 개선
        route = twoOpt(route);

        return route;
    }

    /**
     * 최근접 이웃 알고리즘
     */
    private List<Starbucks> nearestNeighbor(List<Starbucks> locations) {
        List<Starbucks> unvisited = new ArrayList<>(locations);
        List<Starbucks> route = new ArrayList<>();

        Starbucks current = unvisited.remove(0); // 시작점
        route.add(current);

        while (!unvisited.isEmpty()) {
            Starbucks nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Starbucks candidate : unvisited) {
                double dist = distance(current, candidate);
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = candidate;
                }
            }

            current = nearest;
            route.add(current);
            unvisited.remove(current);
        }

        return route;
    }

    /**
     * 2-opt 최적화 알고리즘
     */
    private List<Starbucks> twoOpt(List<Starbucks> route) {
        boolean improved = true;
        int size = route.size();

        while (improved) {
            improved = false;

            for (int i = 1; i < size - 2; i++) {
                for (int j = i + 1; j < size - 1; j++) {
                    double before = distance(route.get(i - 1), route.get(i)) +
                            distance(route.get(j), route.get(j + 1));

                    double after = distance(route.get(i - 1), route.get(j)) +
                            distance(route.get(i), route.get(j + 1));

                    if (after < before) {
                        Collections.reverse(route.subList(i, j + 1));
                        improved = true;
                    }
                }
            }
        }

        return route;
    }

    /**
     * 지구 거리 계산: Haversine 공식 사용 (단위: km)
     */
    private double distance(Starbucks a, Starbucks b) {
        final int R = 6371; // 지구 반지름 (단위: km)

        double lat1 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon2 = Math.toRadians(b.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double hav = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(hav), Math.sqrt(1 - hav));

        return R * c; // 단위: km
    }
}