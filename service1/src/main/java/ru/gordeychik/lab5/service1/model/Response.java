package ru.gordeychik.lab5.service1.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeychik.lab5.service1.util.DateTimeUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    /**
     * Уникальный идентификатор сообщения из запроса.
     */
    private String uid;
    /**
     * Уникальный идентификатор операции из запроса.
     */
    private String operationUid;
    /**
     * Дата и время формирования ответа.
     */
    private String systemTime;
    /**
     * Код результата обработки запроса.
     */
    private Codes code;
    /**
     * Рассчитанная годовая премия.
     */
    private Double annualBonus;
    /**
     * Код ошибки обработки запроса.
     */
    private ErrorCodes errorCode;
    /**
     * Текст ошибки обработки запроса.
     */
    private ErrorMessages errorMessage;

    public static Response success(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    public Response failure(ErrorCodes errorCode, ErrorMessages errorMessage) {
        this.code = Codes.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        return this;
    }
}
