package org.wifry.fooddelivery.model;

import org.wifry.fooddelivery.enumeration.GenericEnumUserType;
import org.wifry.fooddelivery.enumeration.PersistentEnum;
import org.wifry.fooddelivery.util.Utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public enum Estado implements PersistentEnum {

    NUEVO(Integer.valueOf(99)), INACTIVO(Integer.valueOf(0)), ACTIVO(Integer.valueOf(1)), ELIMINADO(Integer.valueOf(-1)), ANULADO(Integer.valueOf(-2));

    private Integer value;
    private ResourceBundle msg = Utils.RESOURCEBUNDLE;
    private static Map<Integer, Estado> map = new HashMap<>();

    static {
        for (Estado estado : Estado.values()) {
            map.put(estado.value, estado);
        }
    }

    public static Estado valueOf(Object estado) {
        return map.get(Integer.valueOf(estado.toString()));
    }


    private Estado(Integer value) {
        this.value = value;
    }

    public static Estado[] valuesForm() {
        return (Estado[]) EnumSet.of(ACTIVO, INACTIVO).toArray(new Estado[0]);
    }

    public static Estado[] valuesFormDocument() {
        return (Estado[]) EnumSet.of(ACTIVO, ANULADO).toArray(new Estado[0]);
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean isActivo() {
        return this == ACTIVO || this == NUEVO ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean isAnulado() {
        return this == ANULADO ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean isEliminado() {
        return this == ELIMINADO ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean isNuevo() {
        return this == NUEVO ? Boolean.TRUE : Boolean.FALSE;
    }

    public Estado isEActivo() {
        return this == ACTIVO ? INACTIVO : ACTIVO;
    }

    public String toString() {
        return this.msg.getString(name());
    }

    public static class EstadoConverter extends GenericEnumUserType<Estado> {
        public static final String NAME = "org.wifry.fooddelivery.model.Estado$EstadoConverter";
    }
}
