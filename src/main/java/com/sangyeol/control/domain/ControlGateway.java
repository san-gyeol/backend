package com.sangyeol.control.domain;

import java.util.List;

public interface ControlGateway {
    List<ControlSnapshot> fetch();
}
