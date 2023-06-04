/*
 * This file is generated by jOOQ.
 */
package uk.hotten.staffog.data.jooq.tables;


import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import uk.hotten.staffog.data.jooq.Keys;
import uk.hotten.staffog.data.jooq.Staffog;
import uk.hotten.staffog.data.jooq.tables.records.StaffogTaskRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffogTask extends TableImpl<StaffogTaskRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog.staffog_task</code>
     */
    public static final StaffogTask STAFFOG_TASK = new StaffogTask();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StaffogTaskRecord> getRecordType() {
        return StaffogTaskRecord.class;
    }

    /**
     * The column <code>staffog.staffog_task.id</code>.
     */
    public final TableField<StaffogTaskRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>staffog.staffog_task.task</code>.
     */
    public final TableField<StaffogTaskRecord, String> TASK = createField(DSL.name("task"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_task.data</code>.
     */
    public final TableField<StaffogTaskRecord, String> DATA = createField(DSL.name("data"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    private StaffogTask(Name alias, Table<StaffogTaskRecord> aliased) {
        this(alias, aliased, null);
    }

    private StaffogTask(Name alias, Table<StaffogTaskRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>staffog.staffog_task</code> table reference
     */
    public StaffogTask(String alias) {
        this(DSL.name(alias), STAFFOG_TASK);
    }

    /**
     * Create an aliased <code>staffog.staffog_task</code> table reference
     */
    public StaffogTask(Name alias) {
        this(alias, STAFFOG_TASK);
    }

    /**
     * Create a <code>staffog.staffog_task</code> table reference
     */
    public StaffogTask() {
        this(DSL.name("staffog_task"), null);
    }

    public <O extends Record> StaffogTask(Table<O> child, ForeignKey<O, StaffogTaskRecord> key) {
        super(child, key, STAFFOG_TASK);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Staffog.STAFFOG;
    }

    @Override
    public Identity<StaffogTaskRecord, Integer> getIdentity() {
        return (Identity<StaffogTaskRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<StaffogTaskRecord> getPrimaryKey() {
        return Keys.KEY_STAFFOG_TASK_PRIMARY;
    }

    @Override
    public StaffogTask as(String alias) {
        return new StaffogTask(DSL.name(alias), this);
    }

    @Override
    public StaffogTask as(Name alias) {
        return new StaffogTask(alias, this);
    }

    @Override
    public StaffogTask as(Table<?> alias) {
        return new StaffogTask(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogTask rename(String name) {
        return new StaffogTask(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogTask rename(Name name) {
        return new StaffogTask(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogTask rename(Table<?> name) {
        return new StaffogTask(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Integer, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Integer, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
