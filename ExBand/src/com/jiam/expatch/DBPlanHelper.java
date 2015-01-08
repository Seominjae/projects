package com.jiam.expatch;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBPlanHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "PlanManager";
	private static final String TABLE_PLAN = "plan";
	
	private static final String KEY_ID = "id";
	private static final String USER_NAME = "user";
	private static final String PLAN_NAME = "plan";
	private static final String PLAN_TIMES = "times";	
	
	public DBPlanHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLAN_TABLE = "CREATE TABLE " + TABLE_PLAN + "("
								   + KEY_ID + " INTEGER PRIMARY KEY," + USER_NAME + " TEXT,"
								   + PLAN_NAME + " TEXT," + PLAN_TIMES + " INTEGER)";
		db.execSQL(CREATE_PLAN_TABLE);
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN);
		onCreate(db);
	}
	
	/*
	 * 2014-07-25
	 * CRUD functions
	 */
	
	//add a new plan to db
	public void addPlan(Plan plan) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(USER_NAME, plan.getUser());
		values.put(PLAN_NAME, plan.getName());
		values.put(PLAN_TIMES, plan.getTimes());
		
		db.insert(TABLE_PLAN, null, values);
		db.close();
	}
	
	//get a plan from db
	public Plan getPlan(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PLAN, new String[] { KEY_ID, USER_NAME, PLAN_NAME, PLAN_TIMES },
								 KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		
		if(cursor!=null)
			cursor.moveToFirst();
		
		Plan plan = new Plan(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
		
		return plan;
	}
	
	//get user's plans from db
	public List<Plan> getUserPlans(String username) {
		List<Plan> planList = new ArrayList<Plan>();
		
		String selectQuery = "SELECT * FROM " + TABLE_PLAN + " WHERE " + USER_NAME + " = '" + username + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()) {
			do {
				Plan plan = new Plan(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
				planList.add(plan);
			} while(cursor.moveToNext());
		}
		
		return planList;
	}
	
	//update plan
	public int updatePlan(Plan plan) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_NAME, plan.getUser());
		values.put(PLAN_NAME, plan.getName());
		values.put(PLAN_TIMES, plan.getTimes());
		
		return db.update(TABLE_PLAN, values, KEY_ID + " =?", new String[] { String.valueOf(plan.getId()) });
	}
	
	//delete plan
	public void deletePlan(Plan plan) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLAN, KEY_ID + " =?", new String[] { String.valueOf(plan.getId()) });
		db.close();
	}
	
	//delete user's plans
	public void deleteUserPlans(String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLAN, USER_NAME + " =?", new String[] { username });
		db.close();
	}

}
