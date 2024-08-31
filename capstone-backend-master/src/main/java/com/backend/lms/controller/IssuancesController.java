package com.backend.lms.controller;

import com.backend.lms.dto.Issuances.IssuanceInDto;
import com.backend.lms.dto.Issuances.IssuanceOutDto;
import com.backend.lms.model.Issuances;
import com.backend.lms.service.IIssuancesService;
import com.backend.lms.service.impl.IssuancesServiceImplement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/issuances", produces = {MediaType.APPLICATION_JSON_VALUE})
public class IssuancesController {

    private final IIssuancesService iIssuancesService;

    @GetMapping("/allIssuances")
    public ResponseEntity<?> getIssuances(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            List<IssuanceOutDto> issuanceOutDTOList = iIssuancesService.getAllIssuances(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(issuanceOutDTOList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<IssuanceOutDto> issuanceOutDTOPage = iIssuancesService.getIssuancesPaginated(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOPage);
        }
    }

    @GetMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDto> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDto issuanceOutDTO = iIssuancesService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

//    @GetMapping("/issuance/userId/{id}")
//    public ResponseEntity<List<IssuanceOutDto>> getIssuancesByUserId(@PathVariable Long id) {
//        List<IssuanceOutDto> issuanceOutDTOList = iIssuancesService.getIssuanceByUserId(id);
//        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
//    }

//    @GetMapping("/issuance/mobile/{mobileNumber}")
//    public ResponseEntity<List<IssuanceOutDto>> getIssuancesByMobile(@PathVariable String mobileNumber) {
//        List<IssuanceOutDto> issuanceOutDTOList = iIssuancesService.getIssuanceByMobile(mobileNumber);
//        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
//    }

    @PostMapping("/createIssuance")
    public ResponseEntity<IssuanceOutDto> createIssuance(@RequestBody IssuanceInDto issuanceInDto) {
        IssuanceOutDto issuanceOutDto = iIssuancesService.createIssuance(issuanceInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(issuanceOutDto);
    }

    @PutMapping("/updateIssuance/{id}")
    public ResponseEntity<IssuanceOutDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("newStatus");
        IssuanceOutDto issuanceOutDTO = iIssuancesService.updateStatus(id, newStatus);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @DeleteMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDto> deleteIssuance(@PathVariable Long id) {
        IssuanceOutDto issuanceOutDTO = iIssuancesService.deleteIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }
}
