/*
 * This file is generated by jOOQ.
 */
package uk.hotten.staffog.data.jooq.tables;


import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
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
import uk.hotten.staffog.data.jooq.tables.records.StaffogAuditRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffogAudit extends TableImpl<StaffogAuditRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog.staffog_audit</code>
     */
    public static final StaffogAudit STAFFOG_AUDIT = new StaffogAudit();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StaffogAuditRecord> getRecordType() {
        return StaffogAuditRecord.class;
    }

    /**
     * The column <code>staffog.staffog_audit.id</code>.
     */
    public final TableField<StaffogAuditRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>staffog.staffog_audit.type</code>.
     */
    public final TableField<StaffogAuditRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_audit.data</code>.
     */
    public final TableField<StaffogAuditRecord, String> DATA = createField(DSL.name("data"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_audit.time</code>.
     */
    public final TableField<StaffogAuditRecord, Long> TIME = createField(DSL.name("time"), SQLDataType.BIGINT.nullable(false), this, "");

    private StaffogAudit(Name alias, Table<StaffogAuditRecord> aliased) {
        this(alias, aliased, null);
    }

    private StaffogAudit(Name alias, Table<StaffogAuditRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>staffog.staffog_audit</code> table reference
     */
    public StaffogAudit(String alias) {
        this(DSL.name(alias), STAFFOG_AUDIT);
    }

    /**
     * Create an aliased <code>staffog.staffog_audit</code> table reference
     */
    public StaffogAudit(Name alias) {
        this(alias, STAFFOG_AUDIT);
    }

    /**
     * Create a <code>staffog.staffog_audit</code> table reference
     */
    public StaffogAudit() {
        this(DSL.name("staffog_audit"), null);
    }

    public <O extends Record> StaffogAudit(Table<O> child, ForeignKey<O, StaffogAuditRecord> key) {
        super(child, key, STAFFOG_AUDIT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Staffog.STAFFOG;
    }

    @Override
    public Identity<StaffogAuditRecord, Integer> getIdentity() {
        return (Identity<StaffogAuditRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<StaffogAuditRecord> getPrimaryKey() {
        return Keys.KEY_STAFFOG_AUDIT_PRIMARY;
    }

    @Override
    public StaffogAudit as(String alias) {
        return new StaffogAudit(DSL.name(alias), this);
    }

    @Override
    public StaffogAudit as(Name alias) {
        return new StaffogAudit(alias, this);
    }

    @Override
    public StaffogAudit as(Table<?> alias) {
        return new StaffogAudit(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogAudit rename(String name) {
        return new StaffogAudit(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogAudit rename(Name name) {
        return new StaffogAudit(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogAudit rename(Table<?> name) {
        return new StaffogAudit(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Integer, ? super String, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Integer, ? super String, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
