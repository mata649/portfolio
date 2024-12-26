package com.mata649.portfolio.shared.exceptions;

import lombok.Builder;

@Builder
public record ApiError(Object message, Integer statusCode, String path, String method) {
}