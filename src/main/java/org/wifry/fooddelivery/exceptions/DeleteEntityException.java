package org.wifry.fooddelivery.exceptions;

/**
 * Created by wtuco on 14/07/2016.
 *
 * ChangeStatusException
 */
public class DeleteEntityException extends Exception {

    public DeleteEntityException(String message) {
        super(message);
    }

    public DeleteEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
