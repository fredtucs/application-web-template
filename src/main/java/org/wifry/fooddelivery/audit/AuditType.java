package org.wifry.fooddelivery.audit;

public enum AuditType {
	/**
	 * 
	 * Indicates that an audit record is documenting an update to an existing
	 * record.
	 */
	UPDATE,
 
	/**
	 * 
	 * Indicates that an audit record is documenting an insert of a new record.
	 */
	INSERT,

	/**
	 * 
	 * Indicates that an audit record is documenting a delete of an existing
	 * record.
	 */
	DELETE;
}
