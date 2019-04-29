package com.example.university.astroweathertwo.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.university.astroweathertwo.utilities.database.entities.City;

import java.util.LinkedList;
import java.util.List;

//TODO: limit somehow number of records
// https://medium.com/@ssaurel/learn-to-save-data-with-sqlite-on-android-b11a8f7718d3
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AstroWeatherTwoDB";
    private static final String TABLE_NAME = "Cities";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_LOCATION_STRING = "location_string";
    private static final String KEY_WOEID = "woeid";
    private static final String[] COLUMNS = { KEY_ID, KEY_NAME, KEY_COUNTRY_CODE, KEY_WOEID, KEY_LOCATION_STRING };

    private static SQLiteDatabaseHelper instance = null;


    private SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDatabaseHelper getInstance(Context context) {
        if(instance == null)
            instance = new SQLiteDatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Cities ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
                + "country_code TEXT, " + "woeid TEXT, location_string TEXT )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }


    public void deleteOne(City city) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(city.getId()) });
        db.close();
    }

    public City getCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        City city = new City();
        city.setId(Integer.parseInt(cursor.getString(0)));
        city.setName(cursor.getString(1));
        city.setCountryCode(cursor.getString(2));
        city.setWoeid(cursor.getString(3));
        city.setLocationString(cursor.getString(4));

        return city;
    }

    public List<City> allCities() {

        List<City> players = new LinkedList<City>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        City city = null;

        if (cursor.moveToFirst()) {
            do {
                city = new City();
                city.setId(Integer.parseInt(cursor.getString(0)));
                city.setName(cursor.getString(1));
                city.setCountryCode(cursor.getString(2));
                city.setWoeid(cursor.getString(3));
                city.setLocationString(cursor.getString(4));
                players.add(city);

            } while (cursor.moveToNext());
        }

        return players;
    }

    public void addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, city.getName());
        values.put(KEY_COUNTRY_CODE, city.getCountryCode());
        values.put(KEY_WOEID, city.getWoeid());
        values.put(KEY_LOCATION_STRING, city.getLocationString());
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public int updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, city.getName());
        values.put(KEY_COUNTRY_CODE, city.getCountryCode());
        values.put(KEY_WOEID, city.getWoeid());
        values.put(KEY_LOCATION_STRING, city.getLocationString());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[] { String.valueOf(city.getId()) });

        db.close();

        return i;
    }
}
