package org.wifry.fooddelivery.model;


import org.wifry.fooddelivery.enumeration.SesionAccionEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_LOGINLOG")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 3066084197268914315L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_LOGIN")
    @SequenceGenerator(name = "SQ_LOGIN", sequenceName = "SQ_LOGIN_ID", allocationSize = 1, initialValue = 1)
    private Long idLogin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SesionAccionEnum sesionAccion;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(length = 100, nullable = false, updatable = false)
    private String userName;

    @Column(length = 50)
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    public LoginLog() {
    }

    public LoginLog(Long userId, String userName, String ipAddress, Date createdDate, SesionAccionEnum sesionAccion) {
        this.userId = userId;
        this.userName = userName;
        this.ipAddress = ipAddress;
        this.createdDate = createdDate;
        this.sesionAccion = sesionAccion;
    }

    public Long getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(Long idLogin) {
        this.idLogin = idLogin;
    }

    public SesionAccionEnum getSesionAccion() {
        return sesionAccion;
    }

    public void setSesionAccion(SesionAccionEnum sesionAccion) {
        this.sesionAccion = sesionAccion;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
