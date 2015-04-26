package de.itm.stage.abilityprofiling.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public final String TAG = this.getClass().getSimpleName();

	public static final String TABLE_ABILITY = "ability";
	public static final String TABLE_PROFILE = "profile";
	public static final String TABLE_PERMISSION = "permission";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_ICF_CODE = "icfcode";
	public static final String COLUMN_APP_NAME = "appname";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PERMISSION_STATE = "permission";

	private static final String TEXT_TYPE = " TEXT,";
	private static final String TEXT_TYPE_END = " TEXT";

	public static final String DATABASE_NAME = "abilityprofilingtoolkit.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_ABILITY = "create table "
			+ TABLE_ABILITY + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ICF_CODE
			+ TEXT_TYPE + COLUMN_TITLE + TEXT_TYPE + COLUMN_DESCRIPTION
			+ TEXT_TYPE_END + ")";

	private static final String DATABASE_CREATE_PROFILE = "create table "
			+ TABLE_PROFILE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ICF_CODE
			+ TEXT_TYPE + COLUMN_APP_NAME + TEXT_TYPE + COLUMN_RATING
			+ TEXT_TYPE + COLUMN_TIMESTAMP + TEXT_TYPE_END + ")";

	private static final String DATABASE_CREATE_PERMISSION = "create table "
			+ TABLE_PERMISSION + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_APP_NAME
			+ TEXT_TYPE + COLUMN_PERMISSION_STATE + TEXT_TYPE_END + ")";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	public void createDatabases(android.database.sqlite.SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE_ABILITY);
		database.execSQL(DATABASE_CREATE_PROFILE);
		database.execSQL(DATABASE_CREATE_PERMISSION);
	}


    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {

        createDatabases(db);
        Log.i(TAG, "dbs created");
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABILITY);

        Log.i(TAG, "upgrade");
        onCreate(db);
    }
}