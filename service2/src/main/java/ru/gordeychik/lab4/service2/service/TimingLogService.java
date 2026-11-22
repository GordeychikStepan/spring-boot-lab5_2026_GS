package ru.gordeychik.lab4.service2.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimingLogService {
    public void logTiming(String uid, long receivedAt,
            String service1Time, String clientTime) {
        logInterval(uid, "Сервис 1 -> Сервис 2", receivedAt,
                service1Time);
        logInterval(uid, "Postman pre-request -> Сервис 2",
                receivedAt, clientTime);
    }

    private void logInterval(String uid, String label,
            long receivedAt, String startTime) {
        if (startTime == null || startTime.isBlank()) {
            log.info("uid={} {}: метка времени отсутствует",
                    uid, label);
            return;
        }
        try {
            long start = Long.parseLong(startTime);
            if (start <= 0 || start > receivedAt) {
                throw new IllegalArgumentException("неверная метка");
            }
            log.info("uid={} {}: {} мс (t1={}, t2={})", uid,
                    label, receivedAt - start, start, receivedAt);
        } catch (IllegalArgumentException exception) {
            log.warn("uid={} {}: некорректная метка времени",
                    uid, label);
        }
    }
}
