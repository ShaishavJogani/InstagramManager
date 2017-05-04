package database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dataItems.Profile;

import utils.C;
import utils.CommanData;
import utils.Parameters;

import android.app.DownloadManager.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Users {

	private static final String KEY_ROWID = "_id";
	private static final String KEY_UID = "_user_id";
	private static final String KEY_NAME = "full_name";
	private static final String KEY_UNAME = "user_name";
	private static final String KEY_PIC = "profile_picture";
	private static final String KEY_RELATION = "relationship";
	private static final String KEY_BIO = "bio";
	private static final String KEY_MEDIA_COUNT = "media_count";
	private static final String KEY_FOLLOWING = "following";
	private static final String KEY_FOLLOWERS = "followers";

	private static final String DATABASE_NAME = "Unfollower";

	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	public Users(Context c) {
		ourContext = c;
	}

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				db.execSQL(CreateNewTableQuery(C.TABLE_FOLLOWER));
				db.execSQL(CreateNewTableQuery(C.TABLE_FOLLOWING));
				db.execSQL(CreateNewTableQuery(C.TABLE_UNFOLLOWER));
				db.execSQL(CreateNewTableQuery(C.TABLE_TEMP_UPDATE));
			} catch (SQLException e) {
				Log.e("Users", "error creating table");
				e.printStackTrace();
			}
			// db.execSQL("CREATE TABLE " + C.TABLE_FOLLOWER + " (" + KEY_ROWID
			// + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_UID
			// + " TEXT NOT NULL UNIQUE, " + KEY_NAME + " TEXT, "
			// + KEY_UNAME + " TEXT NOT NULL, " + KEY_PIC
			// + " TEXT NOT NULL, " + KEY_BIO + " TEXT, "
			// + KEY_MEDIA_COUNT + " TEXT NOT NULL, " + KEY_FOLLOWING
			// + " TEXT NOT NULL, " + KEY_FOLLOWERS + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + C.TABLE_FOLLOWER);
			db.execSQL("DROP TABLE IF EXISTS " + C.TABLE_FOLLOWING);
			db.execSQL("DROP TABLE IF EXISTS " + C.TABLE_UNFOLLOWER);
			db.execSQL("DROP TABLE IF EXISTS " + C.TABLE_TEMP_UPDATE);
			onCreate(db);
		}

	}

	public void createDatabaseTableEntry(JSONArray result, String tableName)
			throws JSONException {
		// TODO Auto-generated method stub
		if (tableName.equals(C.TABLE_FOLLOWER)) {
			Backup_old_data();
		}
		TruncateTable(ourDatabase, tableName);
		for (int i = 0; i < result.length(); i++) {
			JSONObject temp = result.getJSONObject(i);
			// System.out.println("" + temp);
			String id = temp.getString(Parameters.id);
			String name = temp.getString(Parameters.name);
			String user_name = temp.getString(Parameters.uname);
			String profile = temp.getString(Parameters.profilepic_url);
			String relationStatus = Parameters.noneRelation;
			if (tableName.equals(C.TABLE_FOLLOWING)) {
				relationStatus = Parameters.followRelation;
			} else if (tableName.equals(C.TABLE_FOLLOWER)) {
				relationStatus = checkRelationInFollowingtable(id);
			}

			String bio = "saisu", media = "0", following = "0", followers = "0";
			// if (temp.getString(Parameters.bio)!=null)

			// bio = temp.getString(Parameters.bio);
			if (temp.has(Parameters.counts)) {
				media = temp.getJSONObject(Parameters.counts).getString(
						Parameters.userMedia);
				following = temp.getJSONObject(Parameters.counts).getString(
						Parameters.following);
				followers = temp.getJSONObject(Parameters.counts).getString(
						Parameters.followers);
			}
			// if (name.equals(""))
			// name = "hiii";
			createEntry(tableName, id, name, user_name, profile,
					relationStatus, bio, media, following, followers);
		}
	}

	private String checkRelationInFollowingtable(String id) {
		// TODO Auto-generated method stub
		String query = "Select * from " + C.TABLE_FOLLOWING + " where "
				+ KEY_UID + " = " + id;
		Cursor cursor = ourDatabase.rawQuery(query, null);
		if (cursor.getCount() <= 0) {
			cursor.close();
			return Parameters.noneRelation;
		}
		cursor.close();
		return Parameters.followRelation;
	}

	private void Backup_old_data() {
		// TODO Auto-generated method stub
		TruncateTable(ourDatabase, C.TABLE_TEMP_UPDATE);

		String backupOldData = "INSERT INTO " + C.TABLE_TEMP_UPDATE
				+ " SELECT * FROM " + C.TABLE_FOLLOWER + ";";
		ourDatabase.execSQL(backupOldData);
	}

	private void TruncateTable(SQLiteDatabase db, String table_name) {
		// TODO Auto-generated method stub
		StringBuilder truncateQuery = new StringBuilder();
		truncateQuery.append("TRUNCATE ");
		truncateQuery.append("TABLE ");
		truncateQuery.append(table_name);
		truncateQuery.append(";");
		// ourDatabase.execSQL(truncateQuery.toString());
		db.delete(table_name, null, null);
	}

	public Users Open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	/**
	 * This code can be improved. It does update every time value, instead we
	 * can make it like that if only record is changed only then rows has to
	 * update
	 * 
	 * @param DATABASE_TABLE
	 * @param _UID
	 * @param name
	 * @param user_name
	 * @param profile_pic
	 * @param bio
	 * @param media
	 * @param following
	 * @param followers
	 */
	public void createEntry(String DATABASE_TABLE, String _UID, String name,
			String user_name, String profile_pic, String relationStatus,
			String bio, String media, String following, String followers) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_UID, _UID);
		cv.put(KEY_NAME, name);
		cv.put(KEY_UNAME, user_name);
		cv.put(KEY_PIC, profile_pic);
		cv.put(KEY_RELATION, relationStatus);
		cv.put(KEY_BIO, bio);
		cv.put(KEY_MEDIA_COUNT, media);
		cv.put(KEY_FOLLOWING, following);
		cv.put(KEY_FOLLOWERS, followers);

		if (!CheckIfDataAlreadyInDBorNot(DATABASE_TABLE, KEY_UID, _UID)) {
			ourDatabase.insert(DATABASE_TABLE, null, cv);
		} else {
			ourDatabase.update(DATABASE_TABLE, cv, KEY_UID + "=" + _UID, null);
		}
	}

	public boolean CheckIfDataAlreadyInDBorNot(String TableName,
			String dbfield, String newfield) {

		String Query = "Select * from " + TableName + " where " + dbfield
				+ " = " + newfield;
		Cursor cursor = ourDatabase.rawQuery(Query, null);
		if (cursor.getCount() <= 0) {
			return false;
		} else if (cursor.getCount() == 1) {
		}
		return true;
	}

	public List<Profile> getData(String DATABASE_TABLE) throws JSONException {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_UID, KEY_NAME, KEY_UNAME, KEY_PIC, KEY_RELATION };
		Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		if (cursor == null)
			return null;
		int iRow = cursor.getColumnIndex(KEY_UID);
		int iName = cursor.getColumnIndex(KEY_NAME);
		int iUName = cursor.getColumnIndex(KEY_UNAME);
		int iPic = cursor.getColumnIndex(KEY_PIC);
		int iRelation = cursor.getColumnIndex(KEY_RELATION);

		List<Profile> result = new ArrayList<Profile>();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Profile temp = new Profile();
			temp.setId(cursor.getString(iRow));
			temp.setName(cursor.getString(iName));
			temp.setUName(cursor.getString(iUName));
			temp.setProfilePicURL(cursor.getString(iPic));
			temp.setOutRelationship(cursor.getString(iRelation));
			result.add(temp);
		}
		cursor.close();
		return result;
	}

	public List<Profile> getUnfollower() throws JSONException {
		// TODO Auto-generated method stub
		// select * from A where id not in (select id from B);
		String query = " SELECT * FROM " + C.TABLE_TEMP_UPDATE + " where "
				+ KEY_UID + " NOT IN (SELECT " + KEY_UID + " FROM "
				+ C.TABLE_FOLLOWER + ");";

		StringBuilder updateunfollowtable = new StringBuilder();
		updateunfollowtable.append("INSERT INTO ");
		updateunfollowtable.append(C.TABLE_UNFOLLOWER);
		updateunfollowtable.append(query);
		ourDatabase.execSQL(updateunfollowtable.toString());
		return getData(C.TABLE_UNFOLLOWER);
	}

	private static String CreateNewTableQuery(String TableName) {
		// TODO Auto-generated method stub

		StringBuilder query = new StringBuilder();
		query.append("CREATE TABLE ");
		query.append(TableName);
		query.append(" (");
		query.append(KEY_ROWID);
		query.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		query.append(KEY_UID);
		if (TableName.equals(C.TABLE_UNFOLLOWER))
			query.append(" TEXT NOT NULL, ");
		else
			query.append(" TEXT NOT NULL UNIQUE, ");
		query.append(KEY_NAME);
		query.append(" TEXT, ");
		query.append(KEY_UNAME);
		query.append(" TEXT NOT NULL, ");
		query.append(KEY_PIC);
		query.append(" TEXT NOT NULL, ");
		query.append(KEY_RELATION);
		query.append(" TEXT NOT NULL, ");
		query.append(KEY_BIO);
		query.append(" TEXT, ");
		query.append(KEY_MEDIA_COUNT);
		query.append(" TEXT NOT NULL, ");
		query.append(KEY_FOLLOWING);
		query.append(" TEXT NOT NULL, ");
		query.append(KEY_FOLLOWERS);
		query.append(" TEXT NOT NULL);");
		return query.toString();
	}

	private void deleteEntry(String DATABASE_TABLE, String user_id)
			throws SQLException {
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE, KEY_UID + "=" + user_id, null);
	}

	public void close() {
		ourHelper.close();
	}
}
