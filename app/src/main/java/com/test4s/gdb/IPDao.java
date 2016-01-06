package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.IP;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table IP.
*/
public class IPDao extends AbstractDao<IP, Long> {

    public static final String TABLENAME = "IP";

    /**
     * Properties of entity IP.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IpId = new Property(1, String.class, "ipId", false, "IP_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Type = new Property(3, String.class, "type", false, "TYPE");
        public final static Property Style = new Property(4, String.class, "style", false, "STYLE");
        public final static Property Range = new Property(5, String.class, "range", false, "RANGE");
        public final static Property Introuduction = new Property(6, String.class, "introuduction", false, "INTROUDUCTION");
        public final static Property Location = new Property(7, String.class, "location", false, "LOCATION");
        public final static Property Scale = new Property(8, String.class, "scale", false, "SCALE");
        public final static Property WebSite = new Property(9, String.class, "webSite", false, "WEB_SITE");
        public final static Property TelePhone = new Property(10, String.class, "telePhone", false, "TELE_PHONE");
        public final static Property Address = new Property(11, String.class, "address", false, "ADDRESS");
        public final static Property OtherIp = new Property(12, String.class, "otherIp", false, "OTHER_IP");
    };


    public IPDao(DaoConfig config) {
        super(config);
    }
    
    public IPDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'IP' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'IP_ID' TEXT," + // 1: ipId
                "'NAME' TEXT," + // 2: name
                "'TYPE' TEXT," + // search: type
                "'STYLE' TEXT," + // 4: style
                "'RANGE' TEXT," + // 5: range
                "'INTROUDUCTION' TEXT," + // 6: introuduction
                "'LOCATION' TEXT," + // 7: location
                "'SCALE' TEXT," + // 8: scale
                "'WEB_SITE' TEXT," + // 9: webSite
                "'TELE_PHONE' TEXT," + // 10: telePhone
                "'ADDRESS' TEXT," + // 11: address
                "'OTHER_IP' TEXT);"); // 12: otherIp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'IP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IP entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String ipId = entity.getIpId();
        if (ipId != null) {
            stmt.bindString(2, ipId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
 
        String style = entity.getStyle();
        if (style != null) {
            stmt.bindString(5, style);
        }
 
        String range = entity.getRange();
        if (range != null) {
            stmt.bindString(6, range);
        }
 
        String introuduction = entity.getIntrouduction();
        if (introuduction != null) {
            stmt.bindString(7, introuduction);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(8, location);
        }
 
        String scale = entity.getScale();
        if (scale != null) {
            stmt.bindString(9, scale);
        }
 
        String webSite = entity.getWebSite();
        if (webSite != null) {
            stmt.bindString(10, webSite);
        }
 
        String telePhone = entity.getTelePhone();
        if (telePhone != null) {
            stmt.bindString(11, telePhone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(12, address);
        }
 
        String otherIp = entity.getOtherIp();
        if (otherIp != null) {
            stmt.bindString(13, otherIp);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public IP readEntity(Cursor cursor, int offset) {
        IP entity = new IP( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ipId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // style
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // range
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // introuduction
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // location
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // scale
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // webSite
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // telePhone
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // address
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // otherIp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IP entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIpId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStyle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRange(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIntrouduction(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLocation(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setScale(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setWebSite(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTelePhone(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAddress(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setOtherIp(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(IP entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(IP entity) {
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