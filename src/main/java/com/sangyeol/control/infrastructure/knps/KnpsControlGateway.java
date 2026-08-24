package com.sangyeol.control.infrastructure.knps;

import com.sangyeol.control.domain.ControlGateway;
import com.sangyeol.control.domain.ControlSnapshot;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KnpsControlGateway implements ControlGateway {

    private static final String CONTROL_LIST_URI = "/front/portal/safe/acsCtrList.do?menuNo=8000340";

    private final RestClient knpsRestClient;

    public KnpsControlGateway(RestClient knpsRestClient) {
        this.knpsRestClient = knpsRestClient;
    }

    @Override
    public List<ControlSnapshot> fetch() {
        String html = knpsRestClient.get()
                .uri(CONTROL_LIST_URI)
                .retrieve()
                .body(String.class);
        return KnpsControlParser.parse(html);
    }
}
