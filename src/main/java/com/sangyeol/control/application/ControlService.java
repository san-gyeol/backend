package com.sangyeol.control.application;

import com.sangyeol.control.domain.ControlGateway;
import com.sangyeol.control.domain.ControlSnapshot;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ControlService {

    private final ControlGateway controlGateway;

    public ControlService(ControlGateway controlGateway) {
        this.controlGateway = controlGateway;
    }

    public List<ControlSnapshot> findControls() {
        try {
            return controlGateway.fetch();
        } catch (RuntimeException e) {
            throw new ControlUnavailableException(e);
        }
    }
}
