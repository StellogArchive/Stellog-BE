package org.example.stellog.badge.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.badge.domain.repository.BadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {
    private final BadgeRepository badgeRepository;
}
