package com.readboy.learnwordg.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mar on 13-9-30.
 */
public class DbHelper extends SQLiteOpenHelper {

    static final String dbName = "LearnWord.db";// 数据库名
    static final String name = "";
    static final String tableName = "wrongword";// 数据表名称
    SQLiteDatabase db;
    static int db_version = 1;

    public DbHelper(Context context) {
        super(context, dbName, null, db_version);
        db = this.getWritableDatabase();

        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + tableName
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,word_name text,word_expl text, word_index int,word_count int)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
//        System.out.println("DbOnupgrade");
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                + tableName
//                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,word_name text, word_index int,word_count)");
    }

    /**
     * 添加操作
     *
     * @param
     */
    public void insert(ContentValues cv) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, cv);
    }

    public void into(Word word) {
        Cursor cur = db.rawQuery("select * from " + tableName + " where word_index=" + word.index, null);
        ContentValues cv = new ContentValues();
        cv.put("word_name", word.word);
        cv.put("word_index", word.index);
        cv.put("word_expl", word.expl);

        if (cur.getCount() == 0) {
            cv.put("word_count", 1);
            insert(cv);
        } else {
            cur.moveToFirst();
            int x = cur.getInt(3) + 1;
            cv.put("word_count", x);
            update(cv, cur.getInt(0) + "");
        }
        cur.close();

    }

    /**
     * 得到记录总条数
     *
     * @return
     */
    public int getCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * from " + tableName, null);
        int x = cur.getCount();
        cur.close();
        return x;
    }

    /**
     * 得到所有记录的List数组 返回Cursor数据源
     *
     * @return
     */
    public Cursor query() {
        return db.query(tableName, null, null, null, null, null, null, null);
    }

    /**
     * 根据id得到记录
     *
     * @return Cursor
     */
//    public Cursor queryByid(String id) {
//        return db.query(tableName, null, "id=?", new String[] { id }, null,
//                null, null, null);
//    }
    public Cursor queryword(int word_index) {
        // TODO Auto-generated method stub
        String sql = "select id from " + tableName + " where word_index like '" + word_index + "'";
        return db.rawQuery(sql, null);
    }


    /**
     * 删除操作
     *
     * @param
     * @return
     */
    public int delete(int word_index) {
        return db.delete(tableName, "word_index=?", new String[]{word_index + ""});
    }

    /**
     * 根据id修改操作
     */
    public void update(ContentValues cv, String id) {
        db.update(tableName, cv, "id=?", new String[]{id});
    }
}
