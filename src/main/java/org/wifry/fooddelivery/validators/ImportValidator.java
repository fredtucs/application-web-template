package org.wifry.fooddelivery.validators;

import org.wifry.fooddelivery.util.FacesUtils;
import org.wifry.fooddelivery.util.Utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.util.ResourceBundle;


@FacesValidator("importValidator")
public class ImportValidator implements Validator {

    private ResourceBundle msg = FacesUtils.getBundle();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        BigDecimal montoPago = new BigDecimal(value.toString());

        if (Utils.valueScale(montoPago).compareTo(BigDecimal.ZERO) == 0 || Utils.valueScale(montoPago).compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("errImportTotalZero"), null));
        }

        BigDecimal importSumaTotal = Utils.valueScale(component.getAttributes().get("importSumaTotal"));
        BigDecimal importTotal = Utils.valueScale(component.getAttributes().get("importTotal"));

        Object oldValue = ((UIInput) component).getValue();

        Object newValue = ((UIInput) component).getSubmittedValue();

        if (oldValue == null && newValue != null) {
            importSumaTotal = importSumaTotal.add(montoPago);
        }

        if (oldValue != null && newValue != null && Utils.valueScale(newValue).compareTo(Utils.valueScale(oldValue)) != 0) {
            importSumaTotal = importSumaTotal.subtract(new BigDecimal(oldValue.toString()));
            importSumaTotal = importSumaTotal.add(montoPago);
        }

        if (Utils.valueScale(importSumaTotal).compareTo(Utils.valueScale(importTotal)) == 1) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("errImportDetalle"), null));
        }


    }

}