/*
 * This file is generated by jOOQ.
 */
package uk.hotten.staffog.data.jooq.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
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
import uk.hotten.staffog.data.jooq.tables.records.StaffogWebRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffogWeb extends TableImpl<StaffogWebRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog.staffog_web</code>
     */
    public static final StaffogWeb STAFFOG_WEB = new StaffogWeb();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StaffogWebRecord> getRecordType() {
        return StaffogWebRecord.class;
    }

    /**
     * The column <code>staffog.staffog_web.id</code>.
     */
    public final TableField<StaffogWebRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_web.username</code>.
     */
    public final TableField<StaffogWebRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(128).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_web.uuid</code>.
     */
    public final TableField<StaffogWebRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_web.password</code>.
     */
    public final TableField<StaffogWebRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_web.active</code>.
     */
    public final TableField<StaffogWebRecord, Boolean> ACTIVE = createField(DSL.name("active"), SQLDataType.BIT.nullable(false).defaultValue(DSL.inline("b'1'", SQLDataType.BIT)), this, "");

    /**
     * The column <code>staffog.staffog_web.admin</code>.
     */
    public final TableField<StaffogWebRecord, Boolean> ADMIN = createField(DSL.name("admin"), SQLDataType.BIT.nullable(false), this, "");

    private StaffogWeb(Name alias, Table<StaffogWebRecord> aliased) {
        this(alias, aliased, null);
    }

    private StaffogWeb(Name alias, Table<StaffogWebRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>staffog.staffog_web</code> table reference
     */
    public StaffogWeb(String alias) {
        this(DSL.name(alias), STAFFOG_WEB);
    }

    /**
     * Create an aliased <code>staffog.staffog_web</code> table reference
     */
    public StaffogWeb(Name alias) {
        this(alias, STAFFOG_WEB);
    }

    /**
     * Create a <code>staffog.staffog_web</code> table reference
     */
    public StaffogWeb() {
        this(DSL.name("staffog_web"), null);
    }

    public <O extends Record> StaffogWeb(Table<O> child, ForeignKey<O, StaffogWebRecord> key) {
        super(child, key, STAFFOG_WEB);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Staffog.STAFFOG;
    }

    @Override
    public UniqueKey<StaffogWebRecord> getPrimaryKey() {
        return Keys.KEY_STAFFOG_WEB_PRIMARY;
    }

    @Override
    public List<UniqueKey<StaffogWebRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_STAFFOG_WEB_USERNAME);
    }

    @Override
    public StaffogWeb as(String alias) {
        return new StaffogWeb(DSL.name(alias), this);
    }

    @Override
    public StaffogWeb as(Name alias) {
        return new StaffogWeb(alias, this);
    }

    @Override
    public StaffogWeb as(Table<?> alias) {
        return new StaffogWeb(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogWeb rename(String name) {
        return new StaffogWeb(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogWeb rename(Name name) {
        return new StaffogWeb(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogWeb rename(Table<?> name) {
        return new StaffogWeb(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, String, String, String, Boolean, Boolean> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super Integer, ? super String, ? super String, ? super String, ? super Boolean, ? super Boolean, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super Integer, ? super String, ? super String, ? super String, ? super Boolean, ? super Boolean, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}