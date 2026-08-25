package com.sangyeol.control.ui.dto;

import com.sangyeol.control.domain.ControlSnapshot;
import java.util.List;

public record ControlsResponse(List<ControlResponse> controls) {

    public static ControlsResponse from(List<ControlSnapshot> snapshots) {
        return new ControlsResponse(snapshots.stream()
                .map(ControlResponse::from)
                .toList());
    }
}
