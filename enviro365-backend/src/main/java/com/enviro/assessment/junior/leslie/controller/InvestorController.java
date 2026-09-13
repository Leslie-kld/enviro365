package com.enviro.assessment.junior.leslie.controller;

import com.enviro.assessment.junior.leslie.dto.InvestorDTO;
import com.enviro.assessment.junior.leslie.dto.InvestorPortfolioDTO;
import com.enviro.assessment.junior.leslie.service.InvestorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors")
@CrossOrigin(origins = "*")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping
    public ResponseEntity<List<InvestorDTO>> getAllInvestors() {
        return ResponseEntity.ok(investorService.getAllInvestors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorDTO> getInvestorById(@PathVariable Long id) {
        return ResponseEntity.ok(investorService.getInvestorById(id));
    }

    @GetMapping("/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioDTO> getInvestorPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(investorService.getInvestorPortfolio(id));
    }
}
