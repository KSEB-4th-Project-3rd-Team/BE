package com.example.smart_wms_be.service;

import com.example.smart_wms_be.dto.AmrResponse;
import com.example.smart_wms_be.repository.AmrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AMR 상태 조회 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class AmrService {

    private final AmrRepository amrRepository;

    public List<AmrResponse> getAllAmrs() {
        return amrRepository.findAll().stream()
                .map(AmrResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
