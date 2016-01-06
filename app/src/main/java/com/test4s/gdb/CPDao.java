package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.CP;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CP.
*/
public class CPDao extends AbstractDao<CP, Long> {

    public static final String TABLENAME = "CP";

    /**
     * Properties of entity CP.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Introuduction = new Property(2, String.class, "introuduction", false, "INTROUDUCTION");
        public final static Property Location = new Property(3, String.class, "location", false, "LOCATION");
        public final static Property Scale = new Property(4, String.class, "scale", false, "SCALE");
        public final static Property WebSite = new Property(5, String.class, "webSite", false, "WEB_SITE");
        public final static Property TelePhone = new Property(6, String.class, "telePhone", false, "TELE_PHONE");
        public final static Property Address = new Property(7, String.class, "address", false, "ADDRESS");
    };

    private DaoSession daoSession;


    public CPDao(DaoConfig config) {
        super(config);
    }
    
    public CPDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CP' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'INTROUDUCTION' TEXT," + // 2: introuduction
                "'LOCATION' TEXT," + // search: location
                "'SCALE' TEXT," + // 4: scale
                "'WEB_SITE' TEXT," + // 5: webSite
                "'TELE_PHONE' TEXT," + // 6: telePhone
                "'ADDRESS' TEXT);"); // 7: address
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CP entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String introuduction = entity.getIntrouduction();
        if (introuduction != null) {
            stmt.bindString(3, introuduction);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(4, location);
        }
 
        String scale = entity.getScale();
        if (scale != null) {
            stmt.bindString(5, scale);
        }
 
        String webSite = entity.getWebSite();
        if (webSite != null) {
            stmt.bindString(6, webSite);
        }
 
        String telePhone = entity.getTelePhone();
        if (telePhone != null) {
            stmt.bindString(7, telePhone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(8, address);
        }
    }

    @Override
    protected void attachEntity(CP entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CP readEntity(Cursor cursor, int offset) {
        CP entity = new CP( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // introuduction
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // location
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // scale
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // webSite
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // telePhone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // address
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CP entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIntrouduction(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLocation(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setScale(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWebSite(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTelePhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAddress(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CP entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CP entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
