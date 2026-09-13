package com.enviro.assessment.junior.leslie.service;

import com.enviro.assessment.junior.leslie.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.leslie.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.leslie.entity.*;
import com.enviro.assessment.junior.leslie.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.leslie.exception.WithdrawalValidationException;
import com.enviro.assessment.junior.leslie.repository.InvestorRepository;
import com.enviro.assessment.junior.leslie.repository.PortfolioRepository;
import com.enviro.assessment.junior.leslie.repository.WithdrawalNoticeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.enviro.assessment.junior.leslie.repository.WithdrawalSpecifications.*;

@Service
public class WithdrawalService {

    // Every product held in this system is a retirement investment product,
    // so RULE 1 (retirement withdrawals require age > 65) is applied to
    // every withdrawal notice raised through this API.
    private static final int MINIMUM_RETIREMENT_AGE = 65;
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;
    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;

    public WithdrawalService(WithdrawalNoticeRepository withdrawalNoticeRepository,
                              InvestorRepository investorRepository,
                              PortfolioRepository portfolioRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public WithdrawalResponseDTO createWithdrawal(WithdrawalRequestDTO request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investor not found with id " + request.getInvestorId()));

        Portfolio portfolio = portfolioRepository.findById(request.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Portfolio not found with id " + request.getPortfolioId()));

        if (!portfolio.getInvestor().getId().equals(investor.getId())) {
            throw new WithdrawalValidationException(
                    "Portfolio " + portfolio.getId() + " does not belong to investor " + investor.getId());
        }

        validateWithdrawal(investor, portfolio, request.getWithdrawalAmount());

        // Deduct the withdrawal from the portfolio balance so subsequent
        // withdrawal checks are evaluated against the up-to-date balance.
        portfolio.setTotalBalance(portfolio.getTotalBalance().subtract(request.getWithdrawalAmount()));
        portfolioRepository.save(portfolio);

        WithdrawalNotice notice = new WithdrawalNotice(
                investor,
                portfolio,
                request.getWithdrawalAmount(),
                LocalDateTime.now(),
                WithdrawalStatus.APPROVED
        );

        WithdrawalNotice saved = withdrawalNoticeRepository.save(notice);
        return toResponseDTO(saved);
    }

    /**
     * RULE 1: Retirement withdrawals are only allowed if investor age > 65.
     * RULE 2: Withdrawal amount must not exceed the available portfolio balance.
     * RULE 3: Withdrawal amount must not exceed 90% of the portfolio balance.
     * RULE 4: Withdrawal amount must be greater than zero.
     */
    private void validateWithdrawal(Investor investor, Portfolio portfolio, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WithdrawalValidationException("Withdrawal amount must be greater than zero.");
        }

        if (investor.getAge() == null || investor.getAge() <= MINIMUM_RETIREMENT_AGE) {
            throw new WithdrawalValidationException(
                    "Retirement withdrawals are only allowed for investors older than " + MINIMUM_RETIREMENT_AGE + ".");
        }

        if (amount.compareTo(portfolio.getTotalBalance()) > 0) {
            throw new WithdrawalValidationException(
                    "Withdrawal amount cannot exceed the available portfolio balance.");
        }

        BigDecimal maxAllowed = portfolio.getTotalBalance()
                .multiply(MAX_WITHDRAWAL_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        if (amount.compareTo(maxAllowed) > 0) {
            throw new WithdrawalValidationException(
                    "Withdrawal amount cannot exceed 90% of the available portfolio balance.");
        }
    }

    public List<WithdrawalResponseDTO> getWithdrawals(Long investorId, WithdrawalStatus status,
                                                        LocalDate fromDate, LocalDate toDate) {
        Specification<WithdrawalNotice> spec = Specification
                .where(hasInvestorId(investorId))
                .and(hasStatus(status))
                .and(fromDate(fromDate))
                .and(toDate(toDate));

        return withdrawalNoticeRepository.findAll(spec).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public String exportWithdrawalsToCsv(Long investorId, WithdrawalStatus status,
                                          LocalDate fromDate, LocalDate toDate) {
        List<WithdrawalNotice> notices = withdrawalNoticeRepository.findAll(
                Specification.<WithdrawalNotice>where(hasInvestorId(investorId))
                        .and(hasStatus(status))
                        .and(fromDate(fromDate))
                        .and(toDate(toDate))
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        writer.println("Withdrawal ID,Investor Name,Portfolio ID,Withdrawal Amount,Withdrawal Date,Status");

        for (WithdrawalNotice notice : notices) {
            writer.printf("%d,%s,%d,%s,%s,%s%n",
                    notice.getId(),
                    escapeCsv(notice.getInvestor().getFullName()),
                    notice.getPortfolio().getId(),
                    notice.getWithdrawalAmount().toPlainString(),
                    notice.getWithdrawalDate().format(formatter),
                    notice.getStatus());
        }

        writer.flush();
        return stringWriter.toString();
    }

    // Wraps any value containing a comma in quotes so the CSV stays valid
    private String escapeCsv(String value) {
        if (value != null && value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }

    private WithdrawalResponseDTO toResponseDTO(WithdrawalNotice notice) {
        return new WithdrawalResponseDTO(
                notice.getId(),
                notice.getInvestor().getId(),
                notice.getInvestor().getFullName(),
                notice.getPortfolio().getId(),
                notice.getWithdrawalAmount(),
                notice.getWithdrawalDate(),
                notice.getStatus()
        );
    }
}
