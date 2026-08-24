package com.sangyeol.control.ui;

import com.sangyeol.control.application.ControlService;
import com.sangyeol.control.ui.dto.ControlResponse;
import com.sangyeol.control.ui.dto.ControlsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/controls")
public class ControlController {

    private final ControlService controlService;

    public ControlController(ControlService controlService) {
        this.controlService = controlService;
    }

    @GetMapping
    public ResponseEntity<ControlsResponse> findControls() {
        ControlsResponse response = new ControlsResponse(controlService.findControls().stream()
                .map(ControlResponse::from)
                .toList());
        return ResponseEntity.ok(response);
    }
}
