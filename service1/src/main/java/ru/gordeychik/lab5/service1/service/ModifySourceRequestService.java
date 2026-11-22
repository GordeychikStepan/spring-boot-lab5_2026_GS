package ru.gordeychik.lab5.service1.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gordeychik.lab5.service1.model.Request;

@Slf4j
@Service("modifySourceRequestService")
public class ModifySourceRequestService
        implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        String previous = request.getSource();
        request.setSource("Service 1");
        log.info("request.source: {} -> {}", previous,
                request.getSource());
    }
}
