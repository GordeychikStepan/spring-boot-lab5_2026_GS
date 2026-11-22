package ru.gordeychik.lab4.service2.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import ru.gordeychik.lab4.service2.service.TimingLogService;
import ru.gordeychik.lab4.service2.util.RequestTimingFilter;
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
import ru.gordeychik.lab4.service2.exception.UnsupportedCodeException;
import ru.gordeychik.lab4.service2.exception.ValidationFailedException;
import ru.gordeychik.lab4.service2.model.*;
import ru.gordeychik.lab4.service2.service.ModifyResponseService;
import ru.gordeychik.lab4.service2.service.ValidationService;
import ru.gordeychik.lab4.service2.util.DateTimeUtil;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    private final TimingLogService timingLogService;

    @Autowired
    public MyController(ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService modifyResponseService,
            TimingLogService timingLogService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.timingLogService = timingLogService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult,
            HttpServletRequest servletRequest) {
        long receivedAt = (Long) servletRequest.getAttribute(
                RequestTimingFilter.RECEIVED_AT);
        log.info("Получен request: {}", request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat()
                        .format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан response: {}", response);

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                log.error("Выбрасывается UnsupportedCodeException: uid=123");
                throw new UnsupportedCodeException("uid=123 не поддерживается");
            }
            timingLogService.logTiming(request.getUid(), receivedAt,
                    servletRequest.getHeader("X-Service1-Received-At"),
                    servletRequest.getHeader("X-Client-Sent-At"));
            response = modifyResponseService.modify(response);
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Изменён response после валидации: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый uid: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Изменён response для uid=123: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка обработки request", e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Изменён response после исключения: {}", response);
            return new ResponseEntity<>(response,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Возвращается response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
