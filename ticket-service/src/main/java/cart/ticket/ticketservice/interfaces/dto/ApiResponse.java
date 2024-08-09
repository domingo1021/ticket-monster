package cart.ticket.ticketservice.interfaces.dto;

import org.slf4j.MDC;

public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private String requestId;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.requestId = MDC.get("requestId");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(20000, "Success", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getRequestId() {
        return requestId;
    }
}


