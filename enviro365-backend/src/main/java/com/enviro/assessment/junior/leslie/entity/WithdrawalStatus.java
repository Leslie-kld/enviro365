package com.enviro.assessment.junior.leslie.entity;

/**
 * Represents the lifecycle status of a withdrawal notice.
 * Every withdrawal that passes validation is auto-approved for this
 * assessment (no external payment processing exists), but the status
 * field is kept so the workflow can be extended later (e.g. PENDING
 * review, REJECTED by compliance).
 */
public enum WithdrawalStatus {
    APPROVED,
    PENDING,
    REJECTED
}
