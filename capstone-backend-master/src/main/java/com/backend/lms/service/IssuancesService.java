package com.backend.lms.service;

import com.backend.lms.dto.IssuancesDto;
import com.backend.lms.model.Issuances;
import com.backend.lms.repository.IssuancesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssuancesService {

    @Autowired
    public IssuancesRepository issuancesRepository;

    // DTO mapping - method to map entity to DTO
    private IssuancesDto mapToDto(Issuances issuances) {
        IssuancesDto issuancesDto = new IssuancesDto();
        issuancesDto.setId(issuances.getId());
        issuancesDto.setStatus(issuances.getStatus());
        issuancesDto.setIssueDate(issuances.getIssueDate());
        issuancesDto.setReturnDate(issuances.getReturnDate());
        issuancesDto.setUserId(issuances.getId());
        issuancesDto.setBookId(issuances.getId());
        return issuancesDto;
    }

    // Entity mapping - method to map DTO to entity
    private Issuances mapToEntity(IssuancesDto issuancesDto) {
        Issuances issuances = new Issuances();
        issuances.setId(issuancesDto.getId());
        issuances.setStatus(issuancesDto.getStatus());
        issuances.setIssueDate(issuancesDto.getIssueDate());
        issuances.setReturnDate(issuancesDto.getReturnDate());
        issuances.setId(issuancesDto.getUserId());
        issuances.setId(issuancesDto.getBookId());
        return issuances;
    }

    // Get all issuances
    public List<Issuances> getAllIssuances () {
           return issuancesRepository.findAll();
    }

    // Get an issuance by id
    public IssuancesDto getIssuanceById(int id) {
        Issuances issuances = issuancesRepository.findById(id).orElse(null);
        if (issuances != null) {
            return mapToDto(issuances);
        } else {
            return null;
        }
    }

    // Create a new issuance
    public IssuancesDto createIssuance(IssuancesDto issuancesDto) {
        Issuances issuances = mapToEntity(issuancesDto);
        Issuances savedIssuance = issuancesRepository.save(issuances);
        return mapToDto(savedIssuance);
    }

    // Update an issuance
    public IssuancesDto updateIssuance(int id, IssuancesDto issuancesDto) {
        Issuances issuances = issuancesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issuance not found"));
        issuances.setStatus(issuancesDto.getStatus());
        issuances.setIssueDate(issuancesDto.getIssueDate());
        issuances.setReturnDate(issuancesDto.getReturnDate());
        issuances.setId(issuancesDto.getUserId());
        issuances.setId(issuancesDto.getBookId());

        Issuances updatedIssuance = issuancesRepository.save(issuances);
        return mapToDto(updatedIssuance);
    }

    // Delete an issuance
    public void deleteIssuance(int id) {
        Issuances issuances = issuancesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issuance not found"));
        issuancesRepository.delete(issuances);
    }
}
