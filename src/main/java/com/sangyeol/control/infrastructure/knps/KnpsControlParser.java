package com.sangyeol.control.infrastructure.knps;

import com.sangyeol.control.domain.ControlSnapshot;
import com.sangyeol.control.domain.ControlStatus;
import com.sangyeol.control.domain.ControlZone;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

final class KnpsControlParser {

    private static final String PARK_NAME = "무등산";
    private static final String SOURCE = "국립공원공단";
    private static final int EXPECTED_ROW_COUNT = 2;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private KnpsControlParser() {
    }

    static List<ControlSnapshot> parse(String html) {
        Document document = Jsoup.parse(html);
        List<ControlSnapshot> snapshots = document.select("tr").stream()
                .filter(row -> PARK_NAME.equals(row.select("td.parkNm").text().strip()))
                .map(KnpsControlParser::toSnapshot)
                .toList();
        if (snapshots.size() != EXPECTED_ROW_COUNT) {
            throw new IllegalArgumentException("무등산 통제 행이 " + EXPECTED_ROW_COUNT + "건이 아닙니다: " + snapshots.size());
        }
        return snapshots;
    }

    private static ControlSnapshot toSnapshot(Element row) {
        List<Element> cells = row.select("td");
        ControlZone zone = KnpsZoneMapper.toZone(cells.get(1).text().strip());
        ControlStatus status = KnpsStatusMapper.toStatus(cells.get(2).text().strip());
        LocalDateTime referenceTime = LocalDateTime.parse(cells.get(3).text().strip(), TIME_FORMAT);
        return new ControlSnapshot(zone, status, referenceTime, SOURCE);
    }
}
