package com.example.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Events";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "Events";

	private static final String EVENTS_TABLE_CREATE = "create table Events (Event_id integer primary key autoincrement, "
			+ "Event_Type int check (Event_Type in (1,2,3,4)),"
			+ "Rate int not null check (Rate in (1,2,3,4,5)),"
			+ "Start_Date date,Start_HH int,Start_mm int,"
			+ "Title string,Description string,Reminder int not null,"
			+ "Recurrence int,"
			+ "State int not null check (State in (0,1,2)),"
			+ "constraint F1 foreign key(Event_Type) references Event_types);";

	private static final String EVENT_TYPES_TABLE_CREATE = "create table Event_Types (Type_id integer primary key autoincrement check (Type_id in (1,2,3,4)), "
			+ "Type_name string not null,"
			+ "Color string not null check (Color in ('Orange','Green','Red','Blue')));";
	// create table event_types (Type_id integer primary key autoincrement check
	// (Type_id in (1,2,3,4)),Type_name string not null,Color string not null
	// check (Color in ('Orange','Green','Red','Blue')));

	private static final String SETTINGS_TABLE_CREATE = "create table settings('开机启动' boolean primary key default TRUE, "
			+ "'主题皮肤' int not null default 0,"
			+ "'提醒方式' int not null check ('提醒方式' in (0,1,2)),"
			+ "'铃声' int default 0,"
			+ "'音量' int check ('音量' in (1,2,3,4,5,6,7)));";
	// create table settings("开机启动" boolean primary key default TRUE,"主题皮肤" int
	// not null default 0,"提醒方式" int not null check ("提醒方式" in (0,1,2)),"铃声" int
	// default,"音量" int check ("音量" in (1,2,3,4,5,6,7)));
	private static final String BACKGROUND_CREATE = "create table background(image_id integer primary key autoincrement , "
			+ "image_link string not null);";

	// create table background(image_id integer primary key autoincrement ,
	// image_link string not null);

	public DatabaseUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EVENTS_TABLE_CREATE);
		db.execSQL(EVENT_TYPES_TABLE_CREATE);
		db.execSQL(SETTINGS_TABLE_CREATE);
		db.execSQL(BACKGROUND_CREATE);

		// add 4 background images in Background table to choose
		db.execSQL("insert into Background values('/res/drawable/a1');");
		db.execSQL("insert into Background values('/res/drawable/a2');");
		db.execSQL("insert into Background values('/res/drawable/a3');");
		db.execSQL("insert into Background values('/res/drawable/a4');");
		// add 4 records to Event_types
		db.execSQL("insert into Event_Types values(1,'工作类','Orange');");
		db.execSQL("insert into Event_Types values(2,'学习类','Green');");
		db.execSQL("insert into Event_Types values(3,'生活类','Red');");
		db.execSQL("insert into Event_Types values(4,'其他','Blue');");
		// insert into Event_Types values(1,'工作类','Orange');
		// add 1 test record to Events
		db.execSQL("insert into Events values(null,1,1,'2013-06-01',21,38,'安卓编程讨论','经院7楼',1,1,0);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	public Cursor Event_Select() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Events;", null);
		return cursor;
	}

	public Cursor Event_Select(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Events WHERE Event_id=" + id
				+ ";", null);
		return cursor;
	}

	public Cursor Event_Select(int year, int month, int day) {
		SQLiteDatabase db = this.getWritableDatabase();
		Calendar selectedDay = Calendar.getInstance();
		selectedDay.set(year, month, day);
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		String formatedDate = dateFormater.format(selectedDay.getTime());
		Cursor cursor = db.rawQuery("SELECT * FROM Events Where Start_Date='"
				+ formatedDate + "';", null);
		return cursor;
	}

	public void Events_Add(int type, int rate, int year, int month, int day,
			int sHH, int smm, String title, String description, int reminder,
			int recurrence, int state) {
		SQLiteDatabase db = this.getWritableDatabase();
		Calendar selectedDay = Calendar.getInstance();
		selectedDay.set(year, month, day);
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		String dateFormated=dateFormater.format(selectedDay.getTime());
		db.execSQL("insert into Events values(null," + type + "," + rate + ",'"
				+ dateFormated + "'," + sHH + "," + smm
				+ ",'" + title + "','" + description + "'," + reminder + ","
				+ recurrence + "," + state + ");");
	}

	public void Events_Delete(SQLiteDatabase MyHelper, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from Events where Event_id=" + id + ";");
	}

	// id是你要修改的id号
	public void update(SQLiteDatabase MyHelper, String id, int type, int rate,
			Date sdate, int sHH, int smm, String title, String description,
			int reminder, int recurrence, int state) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from Events where Event_id=" + id + ";");
		db.execSQL("insert into Events values(id,type,rate,sdate,stime,fdate,ftime,title,description,reminder,recurrence,state);");
	}
}