package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.NewsInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NEWS_INFO.
*/
public class NewsInfoDao extends AbstractDao<NewsInfo, Long> {

    public static final String TABLENAME = "NEWS_INFO";

    /**
     * Properties of entity NewsInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Ueser_id = new Property(1, String.class, "ueser_id", false, "UESER_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Views = new Property(3, String.class, "views", false, "VIEWS");
        public final static Property Comments = new Property(4, String.class, "comments", false, "COMMENTS");
        public final static Property Cover_img = new Property(5, String.class, "cover_img", false, "COVER_IMG");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property Url = new Property(7, String.class, "url", false, "URL");
    };


    public NewsInfoDao(DaoConfig config) {
        super(config);
    }
    
    public NewsInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NEWS_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UESER_ID' TEXT," + // 1: ueser_id
                "'TITLE' TEXT," + // 2: title
                "'VIEWS' TEXT," + // 3: views
                "'COMMENTS' TEXT," + // 4: comments
                "'COVER_IMG' TEXT," + // 5: cover_img
                "'TIME' TEXT," + // 6: time
                "'URL' TEXT);"); // 7: url
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NEWS_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, NewsInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String ueser_id = entity.getUeser_id();
        if (ueser_id != null) {
            stmt.bindString(2, ueser_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String views = entity.getViews();
        if (views != null) {
            stmt.bindString(4, views);
        }
 
        String comments = entity.getComments();
        if (comments != null) {
            stmt.bindString(5, comments);
        }
 
        String cover_img = entity.getCover_img();
        if (cover_img != null) {
            stmt.bindString(6, cover_img);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(8, url);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public NewsInfo readEntity(Cursor cursor, int offset) {
        NewsInfo entity = new NewsInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ueser_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // views
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // comments
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // cover_img
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // time
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // url
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, NewsInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUeser_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setViews(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setComments(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCover_img(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(NewsInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(NewsInfo entity) {
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
