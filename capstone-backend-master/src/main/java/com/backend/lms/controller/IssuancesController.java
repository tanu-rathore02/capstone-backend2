package com.backend.lms.controller;

import com.backend.lms.dto.IssuancesDto;
import com.backend.lms.model.Issuances;
import com.backend.lms.service.IssuancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IssuancesController {

    @Autowired
    private IssuancesService issuancesService;

    // Get all issuances
    @GetMapping("/allIssuances")
    public ResponseEntity<List<Issuances>> getAllIssuances() {
        List<Issuances> list = issuancesService.getAllIssuances();
        if (list.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(list);
    }

    // Get issuance by id
    @GetMapping("/getIssuance/{id}")
    public ResponseEntity<IssuancesDto> getIssuanceById(@PathVariable int id) {
        IssuancesDto issuanceDto = issuancesService.getIssuanceById(id);
        if (issuanceDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(issuanceDto);
    }

    // Create a new issuance
    @PostMapping("/createIssuance")
    public ResponseEntity<IssuancesDto> createIssuance(@RequestBody IssuancesDto issuanceDto) {
        try {
            IssuancesDto createdIssuance = issuancesService.createIssuance(issuanceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIssuance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update an issuance
    @PutMapping("/updateIssuance/{id}")
    public ResponseEntity<IssuancesDto> updateIssuance(@PathVariable int id, @RequestBody IssuancesDto issuanceDto) {
        try {
            IssuancesDto updatedIssuance = issuancesService.updateIssuance(id, issuanceDto);
            return ResponseEntity.ok().body(updatedIssuance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete an issuance
    @DeleteMapping("/deleteIssuance/{id}")
    public ResponseEntity<Void> deleteIssuance(@PathVariable int id) {
        try {
            issuancesService.deleteIssuance(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
