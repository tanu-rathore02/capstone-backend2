package com.backend.lms.controller;

import com.backend.lms.service.IBooksService;
import com.backend.lms.service.ICategoriesService;
import com.backend.lms.service.IIssuancesService;
import com.backend.lms.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DashboardController {

    private final IBooksService iBooksService;
    private final ICategoriesService iCategoriesService;
    private final IUserService iUserService;
    private final IIssuancesService iIssuancesService;

    @GetMapping("/countAll")
    public ResponseEntity<Map<String, Object>> getAllCounts() {
        Map<String, Object> counts = new HashMap<>();
        Long bookCount = iBooksService.getBookTitleCount();
        Long categoryCount = iCategoriesService.getCategoriesCount();
        Long userCount  = iUserService.getUserCount();
        Long activeUserCount  = iIssuancesService.getIssuanceCountByType();


        counts.put("bookCount", bookCount);
        counts.put("categoryCount", categoryCount);
        counts.put("userCount", userCount);
        counts.put("activeUserCount", activeUserCount);

        return ResponseEntity.status(HttpStatus.OK).body(counts);

    }
}
