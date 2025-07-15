package com.sertifikasi.bukuku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sertifikasi.bukuku.model.Book;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bukuku.db";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_BOOKS = "books";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_AUTHOR = "author";
    private static final String COL_YEAR = "year_published";
    private static final String COL_GENRE = "genre";
    private static final String COL_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_AUTHOR + " TEXT NOT NULL, " +
                COL_YEAR + " INTEGER NOT NULL, " +
                COL_GENRE + " TEXT NOT NULL, " +
                COL_STATUS + " TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_YEAR, book.getYearPublished());
        values.put(COL_GENRE, book.getGenre());
        values.put(COL_STATUS, book.getStatus());
        
        long result = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return result;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS + " ORDER BY " + COL_TITLE;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS))
                );
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return bookList;
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COL_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);
        
        Book book = null;
        if (cursor.moveToFirst()) {
            book = new Book(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS))
            );
        }
        
        cursor.close();
        db.close();
        return book;
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_YEAR, book.getYearPublished());
        values.put(COL_GENRE, book.getGenre());
        values.put(COL_STATUS, book.getStatus());
        
        int result = db.update(TABLE_BOOKS, values, COL_ID + "=?", 
                new String[]{String.valueOf(book.getId())});
        db.close();
        return result;
    }

    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
