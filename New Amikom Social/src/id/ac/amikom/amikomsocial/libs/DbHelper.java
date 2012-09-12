package id.ac.amikom.amikomsocial.libs;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "db_adem";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, 40);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists shout "
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, public_id INTEGER, "
				+ "nid TEXT, " + "name TEXT, alias TEXT, " + "msg TEXT, "
				+ "foto TEXT, " + "sts TEXT, "
				+ "time TIMESTAMP NOT NULL DEFAULT current_timestamp, "
				+ "via TEXT);");
		
		db.execSQL("CREATE TABLE if not exists calendar "
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, "
				+ "start TIMESTAMP NOT NULL DEFAULT current_timestamp, "
				+ "end TIMESTAMP NOT NULL DEFAULT current_timestamp, "
				+ "location TEXT, " + "detail TEXT, " + "status INTEGER);");

		db.execSQL("CREATE TABLE if not exists login "
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "usr TEXT, "
				+ "is_mhs INTEGER, " + "name TEXT, " + "logdate DATE,"
				+ "calendar INTEGER);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS shout");
		db.execSQL("DROP TABLE IF EXISTS login");
		db.execSQL("DROP TABLE IF EXISTS materi");
		db.execSQL("DROP TABLE IF EXISTS calendar");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS info");
		
		onCreate(db);
	}

	public void insertShout(Shout shout) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("public_id", shout.get_public_id());
		values.put("nid", shout.get_nid());
		values.put("name", shout.get_name());
		values.put("alias", shout.get_alias());
		values.put("msg", shout.get_msg());
		values.put("foto", shout.get_foto());
		values.put("sts", shout.get_sts());
		values.put("time", shout.get_time());
		values.put("via", shout.get_via());

		db.insert("shout", null, values);
		db.close();

	}

	public List<Shout> getShout() {
		List<Shout> shoutList = new ArrayList<Shout>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery(
				"Select _id,nid,name,alias,msg,foto,sts,time,via "
						+ "From shout Order By time Desc Limit 100", null);

		if (cur.moveToFirst()) {
			do {
				Shout shout = new Shout();
				shout.set_id(Integer.parseInt(cur.getString(cur.getColumnIndex("_id"))));
				shout.set_nid(cur.getString(cur.getColumnIndex("nid")));
				shout.set_name(cur.getString(cur.getColumnIndex("name")));
				shout.set_alias(cur.getString(cur.getColumnIndex("alias")));
				shout.set_msg(cur.getString(cur.getColumnIndex("msg")));
				shout.set_foto(cur.getString(cur.getColumnIndex("foto")));
				shout.set_sts(cur.getString(cur.getColumnIndex("sts")));
				shout.set_time(cur.getString(cur.getColumnIndex("time")));
				shout.set_via(cur.getString(cur.getColumnIndex("via")));
				shoutList.add(shout);
			} while (cur.moveToNext());
		}
		
		cur.close();
		db.close();
		
		return shoutList;
	}
	
	public int getLastShoutId() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor pub_id = db.rawQuery(
				"Select _id,public_id From shout Order By time Desc Limit 1",
				new String[] {});				

		int lastid = 0;
		if (pub_id.moveToFirst()) {
			do {
				lastid = Integer.parseInt(pub_id.getString(1).toString());
			} while (pub_id.moveToNext());
		}

		pub_id.close();
		db.close();
				
		return lastid;
	}

}
