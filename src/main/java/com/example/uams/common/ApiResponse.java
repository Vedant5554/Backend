package com.example.uams.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Standard response envelope for every API call.
 *
 * Success:  { "success": true,  "message": "OK",       "data": { ... } }
 * Error:    { "success": false, "message": "Not found", "data": null    }
 */
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String  message;
    private T       data;

    // ── factories ─────────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        return of(true, "Success", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return of(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return of(false, message, null);
    }

    private static <T> ApiResponse<T> of(boolean success, String message, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = success;
        r.message = message;
        r.data    = data;
        return r;
    }
}