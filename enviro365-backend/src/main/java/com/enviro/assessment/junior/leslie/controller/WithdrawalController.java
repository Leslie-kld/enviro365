package com.enviro.assessment.junior.leslie.controller;

import com.enviro.assessment.junior.leslie.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.leslie.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.leslie.entity.WithdrawalStatus;
import com.enviro.assessment.junior.leslie.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
@CrossOrigin(origins = "*")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping
    public ResponseEntity<WithdrawalResponseDTO> createWithdrawal(@Valid @RequestBody WithdrawalRequestDTO request) {
        WithdrawalResponseDTO response = withdrawalService.createWithdrawal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WithdrawalResponseDTO>> getWithdrawals(
            @RequestParam(required = false) Long investorId,
            @RequestParam(required = false) WithdrawalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return ResponseEntity.ok(withdrawalService.getWithdrawals(investorId, status, fromDate, toDate));
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportWithdrawals(
            @RequestParam(required = false) Long investorId,
            @RequestParam(required = false) WithdrawalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        String csv = withdrawalService.exportWithdrawalsToCsv(investorId, status, fromDate, toDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=withdrawal-statements.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
