package org.wifry.fooddelivery.validators;

import org.wifry.fooddelivery.util.FacesUtils;
import org.wifry.fooddelivery.util.Utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

@FacesValidator("montoDsctoValidator")
public class MontoDsctoValidator implements Validator {
    private ResourceBundle msg = FacesUtils.getBundle();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        BigDecimal montoDscto = new BigDecimal(value.toString());

        BigDecimal importTotal = ((BigDecimal) component.getAttributes().get("importTotal"));

        if (importTotal == null) {
            return;
        }

        if (Utils.valueScale(montoDscto).compareTo(Utils.valueScale(importTotal)) == 1) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("errMontoDscto"), null));
        }

    }
}
