package laiwei.mydagger2.bean;

import android.support.annotation.Nullable;

import laiwei.mydagger2.C;

/**
 * Created by laiwei on 2018/3/27 0027.
 */
public class ErrorEnvelope {
    public final int code;
    @Nullable
    public final String message;
    @Nullable
    private final Throwable throwable;

    public ErrorEnvelope(@Nullable String message) {
        this(C.ErrorCode.UNKNOWN, message);
    }

    public ErrorEnvelope(int code, @Nullable String message) {
        this(code, message, null);
    }

    public ErrorEnvelope(int code, @Nullable String message, @Nullable Throwable throwable) {
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }
}
