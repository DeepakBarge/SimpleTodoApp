package com.example.deepak.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Item implements Serializable{

    public int id;
    public String label;
    public String notes;
    public String priorityLevel;
    public String dueDate;
    public String status;

    public Item(){}

    public Item(String label, String notes, String priorityLevel, String status){
        this.notes = notes;
        this.label = label;
        this.priorityLevel = priorityLevel;
        this.status = status;
        //this.dueDate = dueDate;
    }
}

public class ItemsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "ItemsDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_LABEL = "label";
    private static final String KEY_ITEM_NOTES = "notes";
    private static final String KEY_ITEM_PLEVEL = "priorityLevel";
    private static final String KEY_ITEM_DUEDATE = "dueDate";
    private static final String KEY_ITEM_STATUS = "status";

    private static ItemsDatabaseHelper sInstance;

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_LABEL + " TEXT," +
                KEY_ITEM_NOTES + " TEXT," +
                KEY_ITEM_PLEVEL + " TEXT," +
                KEY_ITEM_DUEDATE + " DATE," +
                KEY_ITEM_STATUS + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Insert an item into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_LABEL, item.label);
            values.put(KEY_ITEM_STATUS, item.status);
            values.put(KEY_ITEM_PLEVEL, item.priorityLevel);
            values.put(KEY_ITEM_NOTES, item.notes);

            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteItem(String itemLabel) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_LABEL + " = ?", new String[] { String.valueOf(itemLabel) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    // Get all items in the database
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.id = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    newItem.label = cursor.getString(cursor.getColumnIndex(KEY_ITEM_LABEL));
                    newItem.status = cursor.getString(cursor.getColumnIndex(KEY_ITEM_STATUS));
                    newItem.priorityLevel = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PLEVEL));
                    newItem.notes = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NOTES));
                    //newItem.dueDate = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUEDATE));
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    // Update the item
    public int updateItem(String oldLabel, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_LABEL, item.label);
        values.put(KEY_ITEM_NOTES, item.notes);
        values.put(KEY_ITEM_PLEVEL, item.priorityLevel);
        values.put(KEY_ITEM_STATUS, item.status);
        //values.put(KEY_ITEM_DUEDATE, item.dueDate);

        // Updating note of an item with a given id
        return db.update(TABLE_ITEMS, values, KEY_ITEM_LABEL + " = ?",
                new String[] { String.valueOf(oldLabel) });
    }
}