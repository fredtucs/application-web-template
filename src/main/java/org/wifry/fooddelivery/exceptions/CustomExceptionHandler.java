package org.wifry.fooddelivery.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.wifry.fooddelivery.util.FacesUtils;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    private ResourceBundle msg = FacesUtils.getBundle();

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

    final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }


    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();
            Throwable rootCause = ExceptionUtils.getRootCause(exception);
            try {
                if (exception instanceof ViewExpiredException) {
                    String errorPageLocation = "/errorpages/expired.html";
                    facesContext.setViewRoot(facesContext.getApplication().getViewHandler().createView(facesContext, errorPageLocation));
                    facesContext.getPartialViewContext().setRenderAll(true);
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else if (rootCause instanceof NullPeriodoException) {
                    FacesUtils.addErrorMessage(msg.getString("errPeriodoActual"));
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else if (rootCause instanceof ChangeStatusException) {
                    FacesUtils.addErrorMessage(msg.getString("errCambiarEstado"));
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else if (rootCause instanceof SaveEntityException) {
                    FacesUtils.addErrorMessage(msg.getString("errGuardar"));
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else if (rootCause instanceof DeleteEntityException) {
                    FacesUtils.addErrorMessage(msg.getString("errEliminar"));
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else if (rootCause instanceof DataIntegrityViolationException) {
                    if (exception.getCause() instanceof ConstraintViolationException) {
                        requestMap.put("exceptionMessage", exception.getMessage());
                        FacesUtils.addErrorMessage(msg.getString("errIntegridadDb"));
                    } else {
                        FacesUtils.addErrorMessage(exception.getLocalizedMessage());
                    }
                    exception.printStackTrace();
                    facesContext.renderResponse();
                } else {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    exception.printStackTrace(printWriter);
                    exception.printStackTrace();

                    requestMap.put("exceptionMessage", exception.getMessage());

                    FacesUtils.addErrorMessage(msg.getString("errSistema"));

                    facesContext.renderResponse();
                }
            } finally {
                // Remove a exeção da fila
                iterator.remove();
            }
        }
        // Manipula o erro
        getWrapped().handle();
    }
}
