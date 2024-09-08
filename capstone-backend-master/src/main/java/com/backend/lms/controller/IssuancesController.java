package com.backend.lms.controller;

import com.backend.lms.dto.Issuances.IssuanceInDto;
import com.backend.lms.dto.Issuances.IssuanceOutDto;
import com.backend.lms.service.IIssuancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/issuances", produces = {MediaType.APPLICATION_JSON_VALUE})
public class IssuancesController {

    private final IIssuancesService iIssuancesService;


    @GetMapping("/allIssuances")
    public ResponseEntity<?> getIssuances(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<IssuanceOutDto> issuanceOutDTOPage = iIssuancesService.getIssuancesPaginated(pageable, search);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOPage);
    }


    @GetMapping("/type/count")
    public ResponseEntity<Long> getIssuanceByTypeCount() {
        return ResponseEntity.ok(iIssuancesService.getIssuanceCountByType());
    }

    @GetMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDto> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDto issuanceOutDTO = iIssuancesService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IssuanceOutDto>> getIssuancesByUserId(@PathVariable Long userId) {
        List<IssuanceOutDto> issuanceOutDtoList = iIssuancesService.getIssuanceByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDtoList);
    }


    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<IssuanceOutDto>> getIssuancesByBookId(@PathVariable Long bookId) {
        List<IssuanceOutDto> issuanceOutDtoList = iIssuancesService.getIssuanceByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDtoList);
    }

    @PostMapping("/createIssuance")
    public ResponseEntity<IssuanceOutDto> createIssuance(@RequestBody IssuanceInDto issuanceInDto) {
        IssuanceOutDto issuanceOutDto = iIssuancesService.createIssuance(issuanceInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(issuanceOutDto);
    }

    @PutMapping("/updateIssuance/{id}")
    public ResponseEntity<IssuanceOutDto> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDto issuanceInDto) {
        IssuanceOutDto updatedIssuanceOutDto = iIssuancesService.updateIssuance(id, issuanceInDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedIssuanceOutDto);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<IssuanceOutDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("newStatus");
        IssuanceOutDto issuanceOutDTO = iIssuancesService.updateStatus(id, newStatus);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @DeleteMapping("/deleteIssuance/{id}")
    public ResponseEntity<IssuanceOutDto> deleteIssuance(@PathVariable Long id) {
        IssuanceOutDto issuanceOutDTO = iIssuancesService.deleteIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }
}




