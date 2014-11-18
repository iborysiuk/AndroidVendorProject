//Create Local database on device
package com.yborisjuk.vendor.libs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBVendorHelper {

	// Table "vendor" Columns names

	public static final String KEY_ROWID = "id";
	public static final int COL_ROWID = 0;
	
	public static final String KEY_UID = "uid";
	public static final int COL_UID = 1;
	
	public static final String KEY_NAME = "name";
	public static final int COL_NAME = 2;

	public static final String KEY_EMAIL = "email";
	public static final int COL_EMAIL = 3;

	public static final String KEY_PHONE = "phonenumber";
	public static final int COL_PHONE = 4;

	public static final String KEY_COUNTRY = "country";
	public static final int COL_COUNTRY = 5;

	public static final String KEY_CITY = "city";
	public static final int COL_CITY = 6;

	public static final String KEY_ADDRESS = "address";
	public static final int COL_ADDRESS = 7;

	public static final String KEY_POSTALCODE = "postalcode";
	public static final int COL_POSTALCODE = 8;

	public static final String KEY_IMGLINK = "imgLink";
	public static final int COL_IMGLINK = 9;

	public static final String KEY_WORKTIME = "workTime";
	public static final int COL_WORKTIME = 10;

	public static final String KEY_CREATED = "created_at";
	public static final int COL_CREATED = 11;

	private static final String DATABASE_NAME = "u733452522_vndr";
	private static final String TABLE_VENDORS = "vendors";

	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_FAVORITE_VENDOR = "CREATE TABLE "
			+ TABLE_VENDORS + "(" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_UID + " text not null, " + KEY_NAME
			+ " text not null, " + KEY_EMAIL + " text not null, " + KEY_PHONE
			+ " text not null, " + KEY_COUNTRY + " text not null, " + KEY_CITY
			+ " text not null, " + KEY_ADDRESS + " text not null, " + KEY_POSTALCODE
			+ " text not null, " + KEY_IMGLINK + " text not null, " + KEY_WORKTIME + " text not null, " + KEY_CREATED
			+ " text not null" + ");";

	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public DBVendorHelper(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public DBVendorHelper open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * All CRUD
	 */

	public long addFavoriteVendor(String uid, String name, String email, String tel,
			String country, String city, String address, String postalCode,
			String imgLink, String workTime) {
		ContentValues values = new ContentValues();
		values.put(KEY_UID, uid);
		values.put(KEY_NAME, name);
		values.put(KEY_EMAIL, email);
		values.put(KEY_PHONE, tel);
		values.put(KEY_COUNTRY, country);
		values.put(KEY_CITY, city);
		values.put(KEY_ADDRESS, address);
		values.put(KEY_POSTALCODE, postalCode);
		values.put(KEY_IMGLINK, imgLink);
		values.put(KEY_WORKTIME, workTime);
		values.put(KEY_CREATED, getDateTime());
		return db.insert(TABLE_VENDORS, null, values);
	}

	public Cursor getAllFavoriteVendors() {
		Cursor cursor = db.query(TABLE_VENDORS, new String[] {KEY_ROWID, KEY_UID,
				KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_COUNTRY, KEY_CITY,
				KEY_ADDRESS, KEY_POSTALCODE, KEY_IMGLINK, KEY_WORKTIME, KEY_CREATED }, null,
				null, null, null, null);
		if (cursor !=null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor checkFavoriteVendor(String uid) {
			String where = KEY_UID + "='" + uid +"'";
			Cursor cursor = db.query(TABLE_VENDORS, new String[] {KEY_ROWID, KEY_UID,
					KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_COUNTRY, KEY_CITY,
					KEY_ADDRESS, KEY_POSTALCODE, KEY_IMGLINK, KEY_WORKTIME, KEY_CREATED}, 
					where, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			return cursor;
	}
	
	public long deleteFavorite(String uid) {
		String where = KEY_UID + "='" + uid +"'";
		return db.delete(TABLE_VENDORS, where, null);
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("db", "db: " + CREATE_FAVORITE_VENDOR);
			db.execSQL(CREATE_FAVORITE_VENDOR);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
			onCreate(db);

		}

	}
}
