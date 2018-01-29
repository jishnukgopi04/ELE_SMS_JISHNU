package com.smsreader.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smsreader.constants.Constants;
import com.smsreader.sqlite.SMSDatabase;

import java.util.ArrayList;


public class SMSProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(Constants.AUTHORITY, Constants.PATH_SMS_LIST, Constants.CODE_SMS_LIST);
    }

    private SMSDatabase mSmsDatabase;

    @Override
    public boolean onCreate() {
        mSmsDatabase = new SMSDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Constants.TABLE_SMS_LIST);
        int matchCode = URI_MATCHER.match(uri);
        Cursor cursor = null;
        switch (matchCode){
            case Constants.CODE_SMS_LIST:
                cursor = queryBuilder.query(mSmsDatabase.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
        }

        return cursor;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return super.applyBatch(operations);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqLiteDatabase = mSmsDatabase.getWritableDatabase();
        int matchCode = URI_MATCHER.match(uri);
        switch (matchCode){
            case Constants.CODE_SMS_LIST:
                long result = sqLiteDatabase.insert(Constants.TABLE_SMS_LIST, null, values);
                if(result != -1){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mSmsDatabase.getWritableDatabase();
        int updateCount = 0;
        int matchCode = URI_MATCHER.match(uri);
        switch (matchCode){
            case Constants.CODE_SMS_LIST:
                updateCount = sqLiteDatabase.update(Constants.TABLE_SMS_LIST, values, selection, selectionArgs);
                break;
        }
        if(updateCount > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

}
