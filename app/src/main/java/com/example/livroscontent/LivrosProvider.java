package com.example.livroscontent;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class LivrosProvider extends ContentProvider {
    LivrosDbHelper dbHelper;

    //Conforme consta no AndroidManisfest.xml
    public static final String AUTHORITY = "com.example.livros";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String ID_PATH = "id/*";

    public static final int BOOKS = 1;
    public static final int BY_ID = 2;

    static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        matcher.addURI(AUTHORITY, null, BOOKS);
        matcher.addURI(AUTHORITY, ID_PATH, BY_ID);
        matcher.addURI(AUTHORITY,"#", BOOKS);
    }

    public LivrosProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);

        if(match != BOOKS){
            return 0;
        }else{
            return db.delete(LivrosDbHelper.TABLE, selection, selectionArgs);
        }

    }

    @Override
    public String getType(Uri uri) {
        int match = matcher.match(uri);

        if(match==BOOKS){
            return "vnd.android.cursor.dir/vnd.example.livros";
        }else{
            return "vnd.android.cursor.item/vnd.example.livro";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        long newId = 0;

        if(match != BOOKS){
            throw new UnsupportedOperationException("Not yet implemented");
        }

        if(values != null){
            newId = db.insert(LivrosDbHelper.TABLE, null, values);
            return Uri.withAppendedPath(uri, String.valueOf(newId));
        }else{
            return null;
        }

    }

    @Override
    public boolean onCreate() {
        dbHelper = new LivrosDbHelper(getContext());
        if(dbHelper==null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String order = null;
        Cursor result = null;

        if(sortOrder!=null){
            order=sortOrder;
        }

        int match = matcher.match(uri);

        try{
            switch (match){
                case BOOKS:
                    result = db.query(LivrosDbHelper.TABLE, projection, selection,selectionArgs,null, null, order);
                case BY_ID:
                    result = db.query(LivrosDbHelper.TABLE, projection, LivrosDbHelper.C_ID+ "=?", new String[]{uri.getLastPathSegment()},null,null,order);
                    break;
                default: throw new UnsupportedOperationException("Not yet implemented");
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        int rows = 0;

        if(match != BOOKS){
            throw new UnsupportedOperationException("Not yet implemented");
        }

        if(values != null){
            rows = db.update(LivrosDbHelper.TABLE, values, selection, selectionArgs);
        }

        return rows;
    }
}
