package org.wifry.fooddelivery.exceptions;

/**
 * Created by wtuco on 14/07/2016.
 *
 * ChangeStatusException
 */
public class NullReportException extends Exception {

    public NullReportException(String message) {
        super(message);
    }

    public NullReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
