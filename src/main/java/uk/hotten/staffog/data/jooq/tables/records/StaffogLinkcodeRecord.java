package uk.hotten.staffog.data.jooq.tables.records;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;

import uk.hotten.staffog.data.jooq.tables.StaffogLinkcode;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffogLinkcodeRecord extends TableRecordImpl<StaffogLinkcodeRecord> implements Record3<String, String, Boolean> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>staffog.staffog_linkcode.uuid</code>.
	 */
	public void setUuid(String value) {
		set(0, value);
	}

	/**
	 * Getter for <code>staffog.staffog_linkcode.uuid</code>.
	 */
	public String getUuid() {
		return (String) get(0);
	}

	/**
	 * Setter for <code>staffog.staffog_linkcode.code</code>.
	 */
	public void setCode(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>staffog.staffog_linkcode.code</code>.
	 */
	public String getCode() {
		return (String) get(1);
	}

	/**
	 * Setter for <code>staffog.staffog_linkcode.admin</code>.
	 */
	public void setAdmin(Boolean value) {
		set(2, value);
	}

	/**
	 * Getter for <code>staffog.staffog_linkcode.admin</code>.
	 */
	public Boolean getAdmin() {
		return (Boolean) get(2);
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	@Override
	public Row3<String, String, Boolean> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	@Override
	public Row3<String, String, Boolean> valuesRow() {
		return (Row3) super.valuesRow();
	}

	@Override
	public Field<String> field1() {
		return StaffogLinkcode.STAFFOG_LINKCODE.UUID;
	}

	@Override
	public Field<String> field2() {
		return StaffogLinkcode.STAFFOG_LINKCODE.CODE;
	}

	@Override
	public Field<Boolean> field3() {
		return StaffogLinkcode.STAFFOG_LINKCODE.ADMIN;
	}

	@Override
	public String component1() {
		return getUuid();
	}

	@Override
	public String component2() {
		return getCode();
	}

	@Override
	public Boolean component3() {
		return getAdmin();
	}

	@Override
	public String value1() {
		return getUuid();
	}

	@Override
	public String value2() {
		return getCode();
	}

	@Override
	public Boolean value3() {
		return getAdmin();
	}

	@Override
	public StaffogLinkcodeRecord value1(String value) {
		setUuid(value);
		return this;
	}

	@Override
	public StaffogLinkcodeRecord value2(String value) {
		setCode(value);
		return this;
	}

	@Override
	public StaffogLinkcodeRecord value3(Boolean value) {
		setAdmin(value);
		return this;
	}

	@Override
	public StaffogLinkcodeRecord values(String value1, String value2, Boolean value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached StaffogLinkcodeRecord
	 */
	public StaffogLinkcodeRecord() {
		super(StaffogLinkcode.STAFFOG_LINKCODE);
	}

	/**
	 * Create a detached, initialized StaffogLinkcodeRecord
	 */
	public StaffogLinkcodeRecord(String uuid, String code, Boolean admin) {

		super(StaffogLinkcode.STAFFOG_LINKCODE);

		setUuid(uuid);
		setCode(code);
		setAdmin(admin);
		resetChangedOnNotNull();

	}

}