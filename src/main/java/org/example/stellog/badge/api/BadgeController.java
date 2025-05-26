package org.example.stellog.badge.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.badge.application.BadgeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/badges")
public class BadgeController {
    private final BadgeService badgeService;
}
