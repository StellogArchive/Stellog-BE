package org.example.stellog.starbucks.application;

import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StarbucksRouteOptimizer {

    public List<Starbucks> findOptimalRoute(List<Starbucks> locations) {
        if (locations == null || locations.isEmpty()) return Collections.emptyList();

        List<List<Starbucks>> permutations = generatePermutations(locations);
        double minDistance = Double.MAX_VALUE;
        List<Starbucks> optimalRoute = null;

        for (List<Starbucks> route : permutations) {
            double distance = calculateTotalDistance(route);
            if (distance < minDistance) {
                minDistance = distance;
                optimalRoute = new ArrayList<>(route);
            }
        }

        return optimalRoute != null ? optimalRoute : Collections.emptyList();
    }

    private List<List<Starbucks>> generatePermutations(List<Starbucks> locations) {
        List<List<Starbucks>> results = new ArrayList<>();
        permute(locations, 0, results);
        return results;
    }

    private void permute(List<Starbucks> arr, int k, List<List<Starbucks>> results) {
        if (k == arr.size()) {
            results.add(new ArrayList<>(arr));
        } else {
            for (int i = k; i < arr.size(); i++) {
                Collections.swap(arr, i, k);
                permute(arr, k + 1, results);
                Collections.swap(arr, i, k);
            }
        }
    }

    private double calculateTotalDistance(List<Starbucks> route) {
        double total = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            total += distance(route.get(i), route.get(i + 1));
        }
        return total;
    }

    private double distance(Starbucks a, Starbucks b) {
        double dx = a.getLatitude() - b.getLatitude();
        double dy = a.getLongitude() - b.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }
}