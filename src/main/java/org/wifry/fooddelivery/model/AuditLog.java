package org.wifry.fooddelivery.model;

import org.wifry.fooddelivery.audit.AuditType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_T_AUDITLOG")
public class AuditLog implements Serializable {

	private static final long serialVersionUID = 3066084197268914315L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_AUDIT")
	@SequenceGenerator(name = "SQ_AUDIT", sequenceName = "SQ_AUDIT_ID", allocationSize = 1, initialValue = 0)
	private Long idAudit;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AuditType operationType;

	@Column(length = 50, nullable = false, updatable = false)
	private Long entityId;

	@Column(length = 100, nullable = false, updatable = false)
	private String entityName;

	@Column(length = 100, nullable = false, updatable = false) 
	private String entityTable;

	@Column(nullable = false, updatable = false)
	private Integer entityHasCode;

	@Column(nullable = false, updatable = false)
	private Long userId;

	@Column(length = 100, nullable = false, updatable = false)
	private String userName;

	@Column(length = 50)
	private String ipAddress;

	@Lob
	@Column(nullable = false, updatable = false)
	private String detailLog;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date createdDate;

	public AuditLog() {
	}

	public AuditLog(Long entityId, String entityName, String entityTable, AuditType operationType) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.entityTable = entityTable;
		this.operationType = operationType;
	}

	public AuditLog(Long entityId, String entityName, String entityTable, Integer entityHasCode, String userName,
			Date createdDate, AuditType operationType) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.entityTable = entityTable;
		this.entityHasCode = entityHasCode;
		this.userName = userName;
		this.createdDate = createdDate;
		this.operationType = operationType;
	}

	public AuditLog(Long entityId, String entityName, String entityTable, Integer entityHasCode, Long userId,
			String userName, Date createdDate, AuditType operationType) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.entityTable = entityTable;
		this.entityHasCode = entityHasCode;
		this.userId = userId;
		this.userName = userName;
		this.createdDate = createdDate;
		this.operationType = operationType;
	}
	
	public AuditLog(Long entityId, String entityName, String entityTable, Integer entityHasCode, Long userId,
			String userName, Date createdDate, AuditType operationType, String ipAddress) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.entityTable = entityTable;
		this.entityHasCode = entityHasCode;
		this.userId = userId;
		this.userName = userName;
		this.createdDate = createdDate;
		this.operationType = operationType;
		this.ipAddress = ipAddress;
	}

	public Long getIdAudit() {
		return idAudit;
	}

	public void setIdAudit(Long idAudit) {
		this.idAudit = idAudit;
	}

	public AuditType getOperationType() {
		return operationType;
	}

	public void setOperationType(AuditType operationType) {
		this.operationType = operationType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityTable() {
		return entityTable;
	}

	public void setEntityTable(String entityTable) {
		this.entityTable = entityTable;
	}

	public Integer getEntityHasCode() {
		return entityHasCode;
	}

	public void setEntityHasCode(Integer entityHasCode) {
		this.entityHasCode = entityHasCode;
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

	public String getDetailLog() {
		return detailLog;
	}

	public void setDetailLog(String detailLog) {
		this.detailLog = detailLog;
	}

}
