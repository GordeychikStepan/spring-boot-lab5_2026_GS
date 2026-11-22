package ru.gordeychik.lab5.service1.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gordeychik.lab5.service1.model.Request;
import ru.gordeychik.lab5.service1.model.Systems;

@Slf4j
@Service("modifySystemNameRequestService")
public class ModifySystemNameRequestService
        implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        Systems previous = request.getSystemName();
        request.setSystemName(Systems.SERVICE_1);
        log.info("request.systemName: {} -> {}", previous,
                request.getSystemName());
    }
}
