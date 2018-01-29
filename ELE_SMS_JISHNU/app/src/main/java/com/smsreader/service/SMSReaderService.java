package com.smsreader.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.annotation.Nullable;

import com.smsreader.constants.Constants;


public class SMSReaderService extends IntentService {

    public static final String TAG = SMSReaderService.class.getSimpleName();

    public SMSReaderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String [] projection = new String[]{Constants.SMSListColumns.COLUMN_ID, Telephony.TextBasedSmsColumns.ADDRESS, Telephony.TextBasedSmsColumns.BODY, Telephony.TextBasedSmsColumns.DATE};
        Cursor cursor = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                projection,
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ContentValues contentValues = new ContentValues();
            long id = cursor.getLong(cursor.getColumnIndex(Constants.SMSListColumns.COLUMN_ID));
            contentValues.put(Constants.SMSListColumns.COLUMN_SMS_ID, id);
            contentValues.put(Constants.SMSListColumns.COLUMN_ADDRESS, cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS)));
            contentValues.put(Constants.SMSListColumns.COLUMN_BODY, cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));
            contentValues.put(Constants.SMSListColumns.COLUMN_RECIEVED_DATE, cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE)));
            int insertCount = getContentResolver().update(Constants.CONTENT_URI_SMS_LIST, contentValues, Constants.SMSListColumns.COLUMN_SMS_ID + " = ? ", new String[]{id+""});
            if(insertCount <= 0){
                getContentResolver().insert(Constants.CONTENT_URI_SMS_LIST, contentValues);
            }
            cursor.moveToNext();
        }

    }
}
