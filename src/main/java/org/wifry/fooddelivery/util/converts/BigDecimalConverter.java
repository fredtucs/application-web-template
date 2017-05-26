package org.wifry.fooddelivery.util.converts;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wifry.fooddelivery.util.Utils;

/**
 * Extends BigDecimalConveter to support specific numbers that correspond to
 * monetary types which are represented by BigDecimal
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */

@FacesConverter(value = "bigDecimalConverter", forClass = BigDecimal.class)
public class BigDecimalConverter extends javax.faces.convert.BigDecimalConverter {

	private static final Logger log = LoggerFactory.getLogger(BigDecimalConverter.class);

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
		if (facesContext == null)
			throw new NullPointerException("facesContext");

		if (uiComponent == null)
			throw new NullPointerException("uiComponent");

		if (StringUtils.isNotBlank(value)) {
			value = value.trim();

			try {
				Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
				DecimalFormat decimalFormat = Utils.createDecimalFormat(locale);
				/**
				 * ^ ( 0 //First is zero | (\d+) // or any number of digits with
				 * no period separator | (\d{1,3}) // or any up to digits
				 * (\\.\d{3})*) //followed by period separator which must be
				 * followed by 3 digits, one or more times (,\d{2})? //Followed
				 * by comma separator and two fraction digits $
				 */
				// String groupingSeparator = new String(new char[] {
				// decimalFormat.getDecimalFormatSymbols().getGroupingSeparator()
				// });
				// String decimalSeparator = new String(new char[] {
				// decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()
				// });
				//
				// if (".".equals(groupingSeparator))
				// groupingSeparator = "\\,";
				// if (".".equals(decimalSeparator))
				// decimalSeparator = "\\.";
				//
				// if (value.matches("^(0|(\\d+)|(\\d{1,3})(" +
				// groupingSeparator + "\\d{3})*)(" + decimalSeparator +
				// "\\d{2})?$")) {

				log.debug(decimalFormat.toPattern());

				Number number = decimalFormat.parse(value);

				if (number instanceof BigDecimal)
					return number;

				return new BigDecimal(number.doubleValue());
				// } else
				// throw new
				// Exception("Value does not match with regular expression");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;

	}

	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		if (facesContext == null)
			throw new NullPointerException("facesContext");
		if (uiComponent == null)
			throw new NullPointerException("uiComponent");

		if (value == null) {
			return "";
		}

		if (value instanceof String) {
			return (String) value;
		}
		if (uiComponent instanceof HtmlInputText && value instanceof BigDecimal) {
			return ((BigDecimal) value).toString();
		}

		try {
			DecimalFormat decimalFormat = Utils.createDecimalFormat(FacesContext.getCurrentInstance().getViewRoot()
					.getLocale());
			return decimalFormat.format(((BigDecimal) value));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}