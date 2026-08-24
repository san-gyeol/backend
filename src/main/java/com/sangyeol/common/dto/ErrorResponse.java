package com.sangyeol.common.dto;

public record ErrorResponse(
        String message,
        String errorCode
) {
}
