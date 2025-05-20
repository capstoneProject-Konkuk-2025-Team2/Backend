package com.capstone.backend.core.common.web.response;

import com.capstone.backend.core.common.web.response.exception.ApiError;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public record ApiResponse<T>(
        @JsonProperty("result") T result,
        @JsonProperty("error") @Schema(hidden = true) ApiError error
) {

    public ApiResponse {
        if ((result == null && error == null) || (result != null && error != null)) {
            throw new IllegalArgumentException("ApiResponse must contain either result or error");
        }
    }

    @JsonIgnore
    public boolean isSuccess() {
        return error == null;
    }

    @JsonIgnore
    public boolean hasError() {
        return error != null;
    }

    public T orElse(T other) {
        return result != null ? result : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return result != null ? result : supplier.get();
    }

    public <R> ApiResponse<R> map(Function<? super T, ? extends R> mapper) {
        return isSuccess() ? ApiResponse.success(mapper.apply(result)) : ApiResponse.error(error);
    }

    public <R> ApiResponse<R> flatMap(Function<? super T, ApiResponse<R>> mapper) {
        return isSuccess() ? mapper.apply(result) : ApiResponse.error(error);
    }

    public static <R> ApiResponse<R> success(R result) {
        return new ApiResponse<>(result, null);
    }

    public static <R> ApiResponse<R> error(ApiError error) {
        return new ApiResponse<>(null, error);
    }

    public static <R> CompletableFuture<ApiResponse<R>> success(CompletableFuture<R> resultFuture) {
        return resultFuture.thenApply(ApiResponse::success);
    }
}
