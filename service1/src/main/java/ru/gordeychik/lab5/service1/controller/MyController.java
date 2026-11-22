package ru.gordeychik.lab5.service1.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestClientException;
import ru.gordeychik.lab5.service1.service.ModifyRequestService;
import ru.gordeychik.lab5.service1.service.RequestForwardingService;
import ru.gordeychik.lab5.service1.util.RequestTimingFilter;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gordeychik.lab5.service1.exception.UnsupportedCodeException;
import ru.gordeychik.lab5.service1.exception.ValidationFailedException;
import ru.gordeychik.lab5.service1.model.*;
import ru.gordeychik.lab5.service1.service.AnnualBonusService;
import ru.gordeychik.lab5.service1.service.ModifyResponseService;
import ru.gordeychik.lab5.service1.service.ValidationService;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    private final ModifyRequestService systemNameModifier;
    private final ModifyRequestService sourceModifier;
    private final RequestForwardingService forwardingService;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService modifyResponseService,
            @Qualifier("modifySystemNameRequestService")
            ModifyRequestService systemNameModifier,
            @Qualifier("modifySourceRequestService")
            ModifyRequestService sourceModifier,
            RequestForwardingService forwardingService,
            AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.systemNameModifier = systemNameModifier;
        this.sourceModifier = sourceModifier;
        this.forwardingService = forwardingService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult,
            HttpServletRequest servletRequest) {
        long receivedAt = (Long) servletRequest.getAttribute(
                RequestTimingFilter.RECEIVED_AT);
        log.info("Получен request: {}", request);
        Response response = Response.success(request);

        try {
            validationService.isValid(bindingResult, request);
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uid=123 не поддерживается");
            }
            systemNameModifier.modify(request);
            sourceModifier.modify(request);
            forwardingService.forward(request, receivedAt,
                    servletRequest.getHeader("X-Client-Sent-At"));
            response = modifyResponseService.modify(response);
            if (request.getPosition() != null) {
                response.setAnnualBonus(annualBonusService.calculate(request.getPosition(),
                        request.getSalary(), request.getBonus(), request.getWorkDays()));
            }
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
            return failure(response, ErrorCodes.VALIDATION_EXCEPTION,
                    ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый uid: {}", e.getMessage());
            return failure(response, ErrorCodes.UNSUPPORTED_EXCEPTION,
                    ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST);
        } catch (RestClientException e) {
            log.error("Ошибка связи с Сервисом 2: {}", e.getMessage());
            return failure(response, ErrorCodes.UNKNOWN_EXCEPTION,
                    ErrorMessages.UNKNOWN, HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка обработки request", e);
            return failure(response, ErrorCodes.UNKNOWN_EXCEPTION,
                    ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Возвращается response: {}", response);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Response> failure(Response response,
            ErrorCodes errorCode, ErrorMessages errorMessage, HttpStatus status) {
        return ResponseEntity.status(status).body(response.failure(errorCode, errorMessage));
    }
}
