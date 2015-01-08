package com.jiam.touchriding;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSpotHelper extends SQLiteOpenHelper {
	
	//DB version
	private static final int DATABASE_VERSION = 2;
	
	//DB name
	private static final String DATABASE_NAME = "spotManager";
	
	//spot table name
	private static final String TABLE_SPOT = "spot";
	
	//spot table columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_NFC_ID = "nfc_id";
	

	public DBSpotHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SPOT_TABLE = "CREATE TABLE " + TABLE_SPOT + "(" 
								   + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
								   + KEY_NFC_ID + " TEXT" + ")";
		db.execSQL(CREATE_SPOT_TABLE);
		
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(1, 'Spot #1', '04A9D892A42880');");
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(2, 'Spot #2', '047DD692A42880');");
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(3, 'Spot #3', '049D180ABE2380');");
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(4, 'Spot #4', '0463DF92A42880');");
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(5, 'Spot #5', '047ED692A42880');");
		db.execSQL("INSERT INTO " + TABLE_SPOT + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_NFC_ID + 
				   ") VALUES(6, 'Spot #6', '042CDB92A42880');");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPOT);
		
		onCreate(db);		
	}
	
	/*
	 * CRUD functions
	 */
	
	//add a new Spot to db
	public void addSpot(Spot spot) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, spot.getName());
		values.put(KEY_NFC_ID, spot.getNfcId());
		
		db.insert(TABLE_SPOT, null, values);
		db.close();
	}
	
	//get a spot from db
	public Spot getSpot(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_SPOT, new String[] { KEY_ID,  KEY_NAME,  KEY_NFC_ID }, 
								 KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		
		Spot spot = new Spot(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		
		return spot;
	}
	
	//get all spots from db
	public List<Spot> getAllSpot() {
		List<Spot> spotList = new ArrayList<Spot>();
		
		String selectQuery = "SELECT * FROM " + TABLE_SPOT;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()) {
			do {
				Spot spot = new Spot();
				spot.setId(Integer.parseInt(cursor.getString(0)));
				spot.setName(cursor.getString(1));
				spot.setNfcId(cursor.getString(2));
				spotList.add(spot);
			} while (cursor.moveToNext());
		}
		
		return spotList;
	}

	//update spot
	public int updateSpot(Spot spot) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, spot.getName());
		values.put(KEY_NFC_ID, spot.getNfcId());
		
		return db.update(TABLE_SPOT, values, KEY_ID + " =?", new String[] { String.valueOf(spot.getId()) });
	}
	
	//delete spot
	public void deleteSpot(Spot spot) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SPOT, KEY_ID + " =?", new String[] { String.valueOf(spot.getId()) });
		db.close();
	}
	
	//get number of spot
	public int getSpotsCount() {
		String countQuery = "SELECT * FROM " + TABLE_SPOT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
}
