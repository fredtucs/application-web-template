package org.wifry.fooddelivery.util.view;
import java.io.Serializable;
/**
 * Created by wtuco on 05/06/2015.
 */
public class FilterOption implements Serializable {

    private String field;

    private Object value;

    public FilterOption() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
