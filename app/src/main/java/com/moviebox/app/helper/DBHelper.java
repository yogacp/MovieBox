package com.moviebox.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moviebox.app.vo.data.Favorite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.moviebox.app.helper.DBHelperContract.FavoriteEntry.*;

/**
 * Created by user on 01/03/2018.
 */

@Singleton
public class DBHelper extends SQLiteOpenHelper {

    @Inject
    public DBHelper(Context context,
                    String dbName,
                    Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        Log.d(getClass().getName(), "tableCreateStatements");
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + DBHelperContract.FavoriteEntry.TABLE_FAVORITE + "("
                            + FAVORITE_COLUMN_MOVIES_ID + " VARCHAR PRIMARY KEY, "
                            + FAVORITE_COLUMN_MOVIES_TITLE + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_BACKDROP_PATH + " TEXT NOT NULL, "
                            + FAVORITE_COLUMN_MOVIES_ORIGINAL_TITLE + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_VOTE_COUNT + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_VOTE_AVERAGE + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_POPULARITY + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_ORIGINAL_LANGUAGE + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_ADULT + " INTEGER DEFAULT 0, "
                            + FAVORITE_COLUMN_MOVIES_OVERVIEW + " TEXT, "
                            + FAVORITE_COLUMN_MOVIES_RELEASE_DATE + " VARCHAR(100), "
                            + FAVORITE_COLUMN_MOVIES_POSTER_PATH + " TEXT NOT NULL "+ ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }


    /**
     * Get Current Time Stamp when insert Data
     * @Param none
     * */
    private String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    /**
     * Get Favorite List
     *@Param none
     * */
    protected List<Favorite> getFavorite() throws Resources.NotFoundException, NullPointerException {
        List<Favorite> favoriteList = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_FAVORITE;
            cursor = db.rawQuery(query,null);

            if (cursor.getCount() > 0) {
                for(int i=0;i<cursor.getCount();i++){
                    cursor.move(i);
                    Favorite favorite = new Favorite();
                    favorite.setId(cursor.getLong(cursor.getColumnIndex(FAVORITE_COLUMN_MOVIES_ID)));
                    favorite.setTitle(cursor.getString(cursor.getColumnIndex(FAVORITE_COLUMN_MOVIES_TITLE)));
                    favoriteList.add(favorite);
                }
                return favoriteList;
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * Insert Favorite data
     *@Param Favorite model
     * */
    protected Long insertFavorite(Favorite favorite) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVORITE_COLUMN_MOVIES_ID, favorite.getId());
            contentValues.put(FAVORITE_COLUMN_MOVIES_TITLE, favorite.getTitle());
            return db.insert(TABLE_FAVORITE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
