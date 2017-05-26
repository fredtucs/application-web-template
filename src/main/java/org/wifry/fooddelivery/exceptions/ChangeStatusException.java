package org.wifry.fooddelivery.exceptions;

/**
 * Created by wtuco on 14/07/2016.
 *
 * ChangeStatusException
 */
public class ChangeStatusException extends Exception {

    public ChangeStatusException(String message) {
        super(message);
    }

    public ChangeStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
