package com.enviro.assessment.junior.leslie.service;

import com.enviro.assessment.junior.leslie.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.leslie.entity.Investor;
import com.enviro.assessment.junior.leslie.entity.Portfolio;
import com.enviro.assessment.junior.leslie.exception.WithdrawalValidationException;
import com.enviro.assessment.junior.leslie.repository.InvestorRepository;
import com.enviro.assessment.junior.leslie.repository.PortfolioRepository;
import com.enviro.assessment.junior.leslie.repository.WithdrawalNoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

    @Mock
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private WithdrawalService withdrawalService;

    private Investor eligibleInvestor;
    private Investor youngInvestor;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        eligibleInvestor = new Investor("John", "Smith", 70, "john.smith@example.com");
        eligibleInvestor.setId(1L);

        youngInvestor = new Investor("Sarah", "Johnson", 60, "sarah.johnson@example.com");
        youngInvestor.setId(2L);

        portfolio = new Portfolio(eligibleInvestor, new BigDecimal("100000.00"));
        portfolio.setId(1L);
    }

    @Test
    void validWithdrawal_isApproved() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(eligibleInvestor));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(withdrawalNoticeRepository.save(any())).thenAnswer(invocation -> {
            var notice = invocation.getArgument(0, com.enviro.assessment.junior.leslie.entity.WithdrawalNotice.class);
            notice.setId(100L);
            return notice;
        });

        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(1L);
        request.setPortfolioId(1L);
        request.setWithdrawalAmount(new BigDecimal("50000.00"));

        var response = withdrawalService.createWithdrawal(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("50000.00"), response.getWithdrawalAmount());
        verify(withdrawalNoticeRepository, times(1)).save(any());
    }

    @Test
    void investorYoungerThanRequiredAge_isRejected() {
        Portfolio youngPortfolio = new Portfolio(youngInvestor, new BigDecimal("100000.00"));
        youngPortfolio.setId(2L);

        when(investorRepository.findById(2L)).thenReturn(Optional.of(youngInvestor));
        when(portfolioRepository.findById(2L)).thenReturn(Optional.of(youngPortfolio));

        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(2L);
        request.setPortfolioId(2L);
        request.setWithdrawalAmount(new BigDecimal("10000.00"));

        WithdrawalValidationException ex = assertThrows(WithdrawalValidationException.class,
                () -> withdrawalService.createWithdrawal(request));

        assertTrue(ex.getMessage().contains("older than 65"));
    }

    @Test
    void withdrawalExceedingBalance_isRejected() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(eligibleInvestor));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(1L);
        request.setPortfolioId(1L);
        request.setWithdrawalAmount(new BigDecimal("150000.00")); // exceeds balance of 100000

        WithdrawalValidationException ex = assertThrows(WithdrawalValidationException.class,
                () -> withdrawalService.createWithdrawal(request));

        assertTrue(ex.getMessage().contains("available portfolio balance"));
    }

    @Test
    void withdrawalExceeding90Percent_isRejected() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(eligibleInvestor));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(1L);
        request.setPortfolioId(1L);
        request.setWithdrawalAmount(new BigDecimal("95000.00")); // 95% of 100000

        WithdrawalValidationException ex = assertThrows(WithdrawalValidationException.class,
                () -> withdrawalService.createWithdrawal(request));

        assertTrue(ex.getMessage().contains("90%"));
    }

    @Test
    void invalidWithdrawalAmount_zeroOrNegative_isRejected() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(eligibleInvestor));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(1L);
        request.setPortfolioId(1L);
        request.setWithdrawalAmount(new BigDecimal("0.00"));

        WithdrawalValidationException ex = assertThrows(WithdrawalValidationException.class,
                () -> withdrawalService.createWithdrawal(request));

        assertTrue(ex.getMessage().contains("greater than zero"));
    }
}
