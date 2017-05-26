package org.wifry.fooddelivery.exceptions;

/**
 * Created by wtuco on 14/07/2016.
 *
 * ChangeStatusException
 */
public class SaveEntityException extends Exception {

    public SaveEntityException(String message) {
        super(message);
    }

    public SaveEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
