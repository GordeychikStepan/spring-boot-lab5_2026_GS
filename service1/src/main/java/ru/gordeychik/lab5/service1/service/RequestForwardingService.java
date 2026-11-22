package ru.gordeychik.lab5.service1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gordeychik.lab5.service1.model.Codes;
import ru.gordeychik.lab5.service1.model.Request;
import ru.gordeychik.lab5.service1.model.Response;

@Slf4j
@Service
public class RequestForwardingService {
    private final RestTemplate restTemplate;
    private final String url;

    public RequestForwardingService(RestTemplate restTemplate,
            @Value("${service2.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public void forward(Request request, long receivedAt,
            String clientSentAt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Service1-Received-At",
                Long.toString(receivedAt));
        if (clientSentAt != null && !clientSentAt.isBlank()) {
            headers.set("X-Client-Sent-At", clientSentAt);
        }
        log.info("Пересылка request в {}: {}", url, request);
        Response response = restTemplate.postForObject(url,
                new HttpEntity<>(request, headers), Response.class);
        if (response == null || response.getCode() != Codes.SUCCESS) {
            throw new RestClientException(
                    "Сервис 2 не подтвердил обработку");
        }
        log.info("Сервис 2 подтвердил обработку uid={}",
                request.getUid());
    }
}
