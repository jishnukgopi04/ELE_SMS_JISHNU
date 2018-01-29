package com.smsreader.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smsreader.constants.Constants;



public class SMSDatabase extends SQLiteOpenHelper {

    public static final String TAG = SMSDatabase.class.getSimpleName();
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "sms_data";

    private Context mContext;

    public SMSDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.QUERY_CREATE_TABLE_SMS_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.QUERY_DROP_TABLE_SMS_LIST);
        onCreate(db);
    }
}
