package com.backend.lms.service;


import com.backend.lms.dto.Issuances.IssuanceInDto;
import com.backend.lms.dto.Issuances.IssuanceOutDto;
import com.backend.lms.model.Issuances;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IIssuancesService {

    List<IssuanceOutDto> getAllIssuances(Sort sort);

    Page<IssuanceOutDto> getIssuancesPaginated(Pageable pageable, String search);

    IssuanceOutDto getIssuanceById(Long id);

    Long getIssuanceCountByType();

    IssuanceOutDto createIssuance(IssuanceInDto issuanceInDto);

    IssuanceOutDto deleteIssuanceById(Long id);

    IssuanceOutDto updateIssuance(Long id, IssuanceInDto issuanceInDto);

    IssuanceOutDto updateStatus(Long id, String newStatus);

    List<IssuanceOutDto> getIssuanceByUserId(Long userId);

    List<IssuanceOutDto> getIssuanceByBookId(Long bookId);

//    Long getIssuanceCountByType();
}

