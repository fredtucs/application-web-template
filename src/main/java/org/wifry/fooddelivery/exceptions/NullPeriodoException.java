package org.wifry.fooddelivery.exceptions;

/**
 * Created by wtuco on 14/07/2016.
 * <p>
 * ChangeStatusException
 */
public class NullPeriodoException extends Exception {

    public NullPeriodoException() {
    }

    public NullPeriodoException(String message) {
        super(message);
    }

    public NullPeriodoException(String message, Throwable cause) {
        super(message, cause);
    }
}
