package com.enviro.assessment.junior.leslie.repository;

import com.enviro.assessment.junior.leslie.entity.WithdrawalNotice;
import com.enviro.assessment.junior.leslie.entity.WithdrawalStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Builds JPA Specifications for GET /api/withdrawals so investorId,
 * status and a fromDate/toDate range can each be applied independently
 * and combined together.
 */
public final class WithdrawalSpecifications {

    private WithdrawalSpecifications() {
    }

    public static Specification<WithdrawalNotice> hasInvestorId(Long investorId) {
        return (root, query, cb) -> investorId == null ? null : cb.equal(root.get("investor").get("id"), investorId);
    }

    public static Specification<WithdrawalNotice> hasStatus(WithdrawalStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<WithdrawalNotice> fromDate(LocalDate fromDate) {
        return (root, query, cb) -> fromDate == null ? null
                : cb.greaterThanOrEqualTo(root.get("withdrawalDate"), fromDate.atStartOfDay());
    }

    public static Specification<WithdrawalNotice> toDate(LocalDate toDate) {
        return (root, query, cb) -> toDate == null ? null
                : cb.lessThanOrEqualTo(root.get("withdrawalDate"), LocalDateTime.of(toDate, LocalTime.MAX));
    }
}
