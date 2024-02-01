package uk.hotten.staffog.data.jooq.tables.records;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import uk.hotten.staffog.data.jooq.tables.StaffogHistory;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffogHistoryRecord extends UpdatableRecordImpl<StaffogHistoryRecord> implements Record4<Integer, LocalDateTime, String, String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Setter for <code>staffog.staffog_history.id</code>.
	 */
	public void setId(Integer value) {
		set(0, value);
	}

	/**
	 * Getter for <code>staffog.staffog_history.id</code>.
	 */
	public Integer getId() {
		return (Integer) get(0);
	}

	/**
	 * Setter for <code>staffog.staffog_history.date</code>.
	 */
	public void setDate(LocalDateTime value) {
		set(1, value);
	}

	/**
	 * Getter for <code>staffog.staffog_history.date</code>.
	 */
	public LocalDateTime getDate() {
		return (LocalDateTime) get(1);
	}

	/**
	 * Setter for <code>staffog.staffog_history.name</code>.
	 */
	public void setName(String value) {
		set(2, value);
	}

	/**
	 * Getter for <code>staffog.staffog_history.name</code>.
	 */
	public String getName() {
		return (String) get(2);
	}

	/**
	 * Setter for <code>staffog.staffog_history.uuid</code>.
	 */
	public void setUuid(String value) {
		set(3, value);
	}

	/**
	 * Getter for <code>staffog.staffog_history.uuid</code>.
	 */
	public String getUuid() {
		return (String) get(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	@Override
	public Row4<Integer, LocalDateTime, String, String> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	@Override
	public Row4<Integer, LocalDateTime, String, String> valuesRow() {
		return (Row4) super.valuesRow();
	}

	@Override
	public Field<Integer> field1() {
		return StaffogHistory.STAFFOG_HISTORY.ID;
	}

	@Override
	public Field<LocalDateTime> field2() {
		return StaffogHistory.STAFFOG_HISTORY.DATE;
	}

	@Override
	public Field<String> field3() {
		return StaffogHistory.STAFFOG_HISTORY.NAME;
	}

	@Override
	public Field<String> field4() {
		return StaffogHistory.STAFFOG_HISTORY.UUID;
	}

	@Override
	public Integer component1() {
		return getId();
	}

	@Override
	public LocalDateTime component2() {
		return getDate();
	}

	@Override
	public String component3() {
		return getName();
	}

	@Override
	public String component4() {
		return getUuid();
	}

	@Override
	public Integer value1() {
		return getId();
	}

	@Override
	public LocalDateTime value2() {
		return getDate();
	}

	@Override
	public String value3() {
		return getName();
	}

	@Override
	public String value4() {
		return getUuid();
	}

	@Override
	public StaffogHistoryRecord value1(Integer value) {
		setId(value);
		return this;
	}

	@Override
	public StaffogHistoryRecord value2(LocalDateTime value) {
		setDate(value);
		return this;
	}

	@Override
	public StaffogHistoryRecord value3(String value) {
		setName(value);
		return this;
	}

	@Override
	public StaffogHistoryRecord value4(String value) {
		setUuid(value);
		return this;
	}

	@Override
	public StaffogHistoryRecord values(Integer value1, LocalDateTime value2, String value3, String value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached StaffogHistoryRecord
	 */
	public StaffogHistoryRecord() {
		super(StaffogHistory.STAFFOG_HISTORY);
	}

	/**
	 * Create a detached, initialized StaffogHistoryRecord
	 */
	public StaffogHistoryRecord(Integer id, LocalDateTime date, String name, String uuid) {

		super(StaffogHistory.STAFFOG_HISTORY);

		setId(id);
		setDate(date);
		setName(name);
		setUuid(uuid);
		resetChangedOnNotNull();

	}

}