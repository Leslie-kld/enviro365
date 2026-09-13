package com.enviro.assessment.junior.leslie.repository;

import com.enviro.assessment.junior.leslie.entity.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JpaSpecificationExecutor lets the service layer build up a dynamic
 * WHERE clause (investorId / status / date range) without needing a
 * different query method for every combination of filters.
 */
public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long>,
        JpaSpecificationExecutor<WithdrawalNotice> {
}
