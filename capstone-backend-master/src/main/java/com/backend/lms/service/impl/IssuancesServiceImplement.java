package com.backend.lms.service.impl;

import com.backend.lms.dto.Issuances.IssuanceInDto;
import com.backend.lms.dto.Issuances.IssuanceOutDto;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.IssuancesMapper;
import com.backend.lms.model.Books;
import com.backend.lms.model.Issuances;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.IssuancesRepository;
import com.backend.lms.repository.UsersRepository;
import com.backend.lms.service.IIssuancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IssuancesServiceImplement implements IIssuancesService {

    private final IssuancesRepository issuancesRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    @Override
    public List<IssuanceOutDto> getAllIssuances(Sort sort) {
        return issuancesRepository.findAll(sort).stream()
                .map(issuance -> IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<IssuanceOutDto> getIssuancesPaginated(Pageable pageable, String search) {
        Page<Issuances> issuancePage;
        if (search != null && !search.isEmpty()) {
            issuancePage = issuancesRepository.findByBooks_TitleContainingIgnoreCase(search, pageable);
        } else {
            issuancePage = issuancesRepository.findAll(pageable);
        }

        return issuancePage.map(issuance -> IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto()));
    }

    @Override
    public IssuanceOutDto getIssuanceById(Long id) {
        Issuances issuance = issuancesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );
        return IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto());
    }

    @Override
    public List<IssuanceOutDto> getIssuanceByUserId(Long userId) {
        List<Issuances> issuancesList = issuancesRepository.findByUsersId(userId);
        return issuancesList.stream()
                .map(issuance -> IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<IssuanceOutDto> getIssuanceByBookId(Long bookId) {
        List<Issuances> issuancesList = issuancesRepository.findByBooksId(bookId);
        return issuancesList.stream()
                .map(issuance -> IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto()))
                .collect(Collectors.toList());
    }

    @Override
    public IssuanceOutDto createIssuance(IssuanceInDto issuanceInDto) {
        Issuances issuance = IssuancesMapper.mapToIssuance(issuanceInDto, new Issuances(), usersRepository, booksRepository);
        issuance.setIssueDate(LocalDateTime.now());

        Books book = issuance.getBooks();
        if (book.getAvailability() > 0) {
            book.setAvailability(book.getAvailability() - 1);
            booksRepository.save(book);
        } else {
            throw new RuntimeException("Book is not available");
        }

        Issuances savedIssuance = issuancesRepository.save(issuance);
        return IssuancesMapper.mapToIssuanceOutDto(savedIssuance, new IssuanceOutDto());
    }

    @Override
    public IssuanceOutDto updateIssuance(Long id, IssuanceInDto issuanceInDto) {
        Issuances issuance = issuancesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        String oldStatus = issuance.getStatus();
        issuance = IssuancesMapper.mapToIssuance(issuanceInDto, issuance, usersRepository, booksRepository);

        if ("RETURNED".equals(issuanceInDto.getStatus()) && !"RETURNED".equals(oldStatus)) {
            Books book = issuance.getBooks();
            book.setAvailability(book.getAvailability() + 1);
            booksRepository.save(book);
        }

        Issuances savedIssuance = issuancesRepository.save(issuance);
        return IssuancesMapper.mapToIssuanceOutDto(savedIssuance, new IssuanceOutDto());
    }

    @Override
    public IssuanceOutDto updateStatus(Long id, String newStatus) {
        Issuances issuance = issuancesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        issuance.setStatus(newStatus);
        issuancesRepository.save(issuance);

        return IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto());
    }

    @Override
    public IssuanceOutDto deleteIssuanceById(Long id) {
        Issuances issuance = issuancesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        Books book = issuance.getBooks();
        if ("RETURNED".equals(issuance.getStatus())) {
            book.setAvailability(book.getAvailability() + 1);
            booksRepository.save(book);
        }

        issuancesRepository.delete(issuance);
        return IssuancesMapper.mapToIssuanceOutDto(issuance, new IssuanceOutDto());
    }
}




