package org.wifry.fooddelivery.web.admin;

import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wifry.fooddelivery.util.FacesUtils;
import org.wifry.fooddelivery.exceptions.ChangeStatusException;
import org.wifry.fooddelivery.model.Banco;
import org.wifry.fooddelivery.model.Estado;
import org.wifry.fooddelivery.services.admin.BancoService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wtuco on 06/05/2016.
 *
 * @see Serializable
 */
@Component
@Scope("view")
public class BancoBean implements Serializable {

    private static final long serialVersionUID = 3037619632050646575L;

    private List<Banco> bancoList;
    private Banco banco;
    private String valorBuscar;

    @Autowired
    private BancoService bancoService;

    private ResourceBundle msg = FacesUtils.getBundle();

    @PostConstruct
    public void init() {
        valorBuscar = null;
        banco = new Banco();
    }

    public void listar() {
        setValorBuscar(null);
        bancoList = bancoService.listAll();
    }

    public void nuevo() {
        banco = new Banco();
        banco.setEstado(Estado.ACTIVO);
        FacesUtils.reset("fmEditBanco");
    }

    public void editar() {
        if (banco != null && banco.getIdBanco() != 0L) {
            banco = bancoService.getByID(banco.getIdBanco());
            FacesUtils.reset("fmEditBanco");
        } else {
            FacesUtils.addWarnigMessage(msg.getString("errEmptyObj"));
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void buscar() {
        bancoList = bancoService.find(getValorBuscar());
    }

    public void eliminar() {
        try {
            String id = Faces.getRequestParameter("index");
            setBanco(bancoService.getByID(Long.valueOf(id)));
            bancoService.delete(banco);
            listar();
            FacesUtils.addSuccessMessage(msg.getString("elimExito"));
            Ajax.update("fmBancos");
            setBanco(null);
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addErrorMessage(msg.getString("errEliminar"));
            Ajax.update("fmBancos:messages");
        }
    }

    public void guardar() {
        try {
            bancoService.save(banco);
            FacesUtils.addSuccessMessage(msg.getString("guardExito"));
            listar();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addErrorMessage(msg.getString("errGuardar"));
            Ajax.update("fmBancos:messages");
        }
    }

    public void editaEstado() throws ChangeStatusException {
        if (banco != null && banco.getIdBanco() != 0L) {
            Estado status = banco.getEstado().isEActivo();
            banco.setEstado(status);
            bancoService.updateState(banco);
            listar();
        }
    }

    public Estado[] getEstados() {
        return Estado.valuesForm();
    }

	/*
     * Gets and Sets
	 */

    public List<Banco> getBancoList() {
        return bancoList;
    }

    public void setBancoList(List<Banco> tipoCuentaList) {
        this.bancoList = tipoCuentaList;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco finalidad) {
        this.banco = finalidad;
    }

    public String getValorBuscar() {
        return valorBuscar;
    }

    public void setValorBuscar(String valorBuscar) {
        this.valorBuscar = valorBuscar;
    }


}
