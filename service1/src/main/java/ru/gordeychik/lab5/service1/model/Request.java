package ru.gordeychik.lab5.service1.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    /**
     * Уникальный идентификатор сообщения.
     */
    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32, message = "uid не более 32 символов")
    private String uid;

    /**
     * Уникальный идентификатор операции.
     */
    @NotBlank(message = "operationUid обязателен")
    @Size(max = 32, message = "operationUid не более 32 символов")
    private String operationUid;

    /**
     * Имя системы, из которой поступило сообщение.
     */
    private Systems systemName;

    /**
     * Дата и время формирования сообщения в системе источника.
     */
    @NotBlank(message = "systemTime обязателен")
    private String systemTime;

    /**
     * Источник сообщения.
     */
    private String source;

    /**
     * Должность сотрудника.
     */
    private Positions position;

    /**
     * Заработная плата сотрудника.
     */
    private Double salary;

    /**
     * Коэффициент премии.
     */
    private Double bonus;

    /**
     * Количество рабочих дней для расчёта премии.
     */
    private Integer workDays;

    /**
     * Идентификатор коммуникации.
     */
    @NotNull(message = "communicationId обязателен")
    @Min(value = 1, message = "communicationId не меньше 1")
    @Max(value = 100000, message = "communicationId не больше 100000")
    private Integer communicationId;

    /**
     * Идентификатор шаблона.
     */
    private Integer templateId;
    /**
     * Код продукта.
     */
    private Integer productCode;
    /**
     * Код из СМС.
     */
    private Integer smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position=" + position +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", workDays=" + workDays +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
