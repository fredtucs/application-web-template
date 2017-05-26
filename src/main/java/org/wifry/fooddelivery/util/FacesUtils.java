package org.wifry.fooddelivery.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

import javax.el.MethodExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.ResourceBundle;

public class FacesUtils {

    private static final String HIBERNATE_SESSION = "hibernate_session";

    public static void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg);
    }

    public static void addErrorMessage(String clientId, String msg) {
        addMessage(clientId, FacesMessage.SEVERITY_ERROR, msg);
    }

    public static void addWarnigMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_WARN, msg);
    }

    public static void addWarnigMessage(String clientId, String msg) {
        addMessage(clientId, FacesMessage.SEVERITY_WARN, msg);
    }

    private static void addMessage(FacesMessage.Severity severity, String msg) {
        addMessage(null, severity, msg);
    }

    private static void addMessage(String clientId, FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, msg);
    }

    public static String getBundleKey(String bundleName, String key) {
        return FacesContext.getCurrentInstance().getApplication()
                .getResourceBundle(FacesContext.getCurrentInstance(), bundleName).getString(key);
    }

    public static void addMessage(String msg) {
        FacesMessage message = new FacesMessage(msg);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }

    public static void addMessage(String clientId, FacesMessage msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(clientId, msg);
    }

    public static void addMessage(UIComponent component, String msg) {
        FacesMessage message = new FacesMessage(msg);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(component.getClientId(context), message);

    }

    public static void addMessage(UIComponent component, String msg, Severity severity) {
        FacesMessage message = new FacesMessage(severity, msg, null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(component.getClientId(context), message);
    }

    public static void addMessage(String msg, Severity severity) {
        FacesMessage message = new FacesMessage(severity, msg, null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }

    public static ResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, "msg");
    }

    public static void reset(String formId) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset(formId);
    }

    public static void update(String uiComponent) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update(uiComponent);
    }

    public static void update(Collection<String> uiComponent) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update(uiComponent);
    }

    public static void setRequestSession(Session session) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(HIBERNATE_SESSION, session);
    }

    public static Session getRequestSession() {
        return (Session) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(HIBERNATE_SESSION);
    }

    public static String getCompId(UIComponent ui) {
        if (ui == null)
            return null;

        return ui.getId();
    }

    public static boolean isCompId(UIComponent ui, String idComp) {
        if(ui == null)
            return false;
        return StringUtils.containsIgnoreCase(ui.getClientId(),idComp);
    }

    /**
     *
     * @param methodExpression
     * @return
     */
    public static MethodExpression createMethodExpression(String methodExpression, Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory()
                .createMethodExpression(facesContext.getELContext(), methodExpression, valueType, new Class<?>[0]);
    }

    /**
     * <p>
     * Get a value by evaluating an expression.
     * </p>
     *
     * @param expression
     *            #{formatoDocBean}
     * @param expectedType
     *            Bean <code>.class</code>
     * @return Object Bean
     */
    public static <T> T getExpressionBean(String expression, Class<? extends T> expectedType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().evaluateExpressionGet(facesContext, expression, expectedType);

    }

    public static String getStringI18n(String key) {
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        String bundleName = factory.getApplication().getMessageBundle();
        ResourceBundle messages = ResourceBundle.getBundle(bundleName, FacesContext.getCurrentInstance().getViewRoot()
                .getLocale());

        return messages.getString(key);
    }

    public static String getParameterisedStringI18n(String key, String args[]) {
        String msg = getLocalizedMessage(key, args);
        return msg;
    }

    public static String getLocalizedMessage(String key, String[] args) {
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        String bundleName = factory.getApplication().getMessageBundle();
        ResourceBundle messages = ResourceBundle.getBundle(bundleName, FacesContext.getCurrentInstance().getViewRoot()
                .getLocale());
        // it's even more work to format a message with args
        MessageFormat form = new MessageFormat(messages.getString(key));
        return form.format(args, new StringBuffer(), null).toString();
    }

}