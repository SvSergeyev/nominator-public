package com.cml.defaultnominator.service.targetlibrary;

import lombok.Getter;

/**
 * Форматированные состояния аппрувов
 */
public enum ApprovalStates {
    NOT_CREATED("approval is not created"),
    AWAITING("awaiting"),
    TIMEOUT("timeout"),
    DECLINED("declined"),
    APPROVED("approved");

    @Getter
    private final String state;

    ApprovalStates(String state) {
        this.state = state;
    }
}
