package com.sangyeol.control.application;

import com.sangyeol.common.exception.BusinessException;

public class ControlUnavailableException extends BusinessException {

    public ControlUnavailableException(Throwable cause) {
        super(ControlErrorType.CONTROL_UNAVAILABLE, cause);
    }
}
