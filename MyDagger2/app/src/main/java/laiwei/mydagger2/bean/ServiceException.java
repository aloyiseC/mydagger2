package laiwei.mydagger2.bean;

/**
 * Created by laiwei on 2018/3/27 0027.
 */
public class ServiceException extends Exception {
    public final ErrorEnvelope error;

    public ServiceException(String message) {
        super(message);

        error = new ErrorEnvelope(message);
    }
}
