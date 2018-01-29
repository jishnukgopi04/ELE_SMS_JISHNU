package com.smsreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.smsreader.R;
import com.smsreader.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SMSGridAdapter extends CursorAdapter {

    public static final String TAG = SMSGridAdapter.class.getSimpleName();

    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    public SMSGridAdapter(Context context) {
        super(context, null, false);
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY HH:mm aa");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String messageBody=cursor.getString(cursor.getColumnIndex(Constants.SMSListColumns.COLUMN_BODY));
        Log.d("messageBody", "bindView: "+messageBody);
        TextView senderName = view.findViewById(R.id.txt_sender_id);
        TextView recievedTime = view.findViewById(R.id.txt_receved_time);
        TextView amount = view.findViewById(R.id.txt_amount);
        TextView cardNumber = view.findViewById(R.id.txt_credit_card_no);
        TextView transactonDate = view.findViewById(R.id.txt_transaction_date);
        senderName.setText(cursor.getString(cursor.getColumnIndex(Constants.SMSListColumns.COLUMN_ADDRESS)));
        recievedTime.setText(getDate(cursor.getLong(cursor.getColumnIndex(Constants.SMSListColumns.COLUMN_RECIEVED_DATE))));
       try {
           amount.setText(getBalance(messageBody).get(1));
           String cardNumberString=getCardNo(messageBody).get(0);
           cardNumber.setText(cardNumberString.substring(cardNumberString.length()-4));
           transactonDate.setText(getTransactionDate(messageBody));
       }
       catch(Exception e)
       {
           Log.e(TAG, "bindView: "+e );
       }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = mSimpleDateFormat.format(cal.getTime()).toString();
        return date;
    }
    List<String>  getBalance(String Message)
    {
        List<String>allMatches=new ArrayList<String>();
        Pattern balncePattern=Pattern.compile(Constants.BALNCE_REGEX);
        Matcher m= balncePattern.matcher(Message);
        while(m.find())
        {
            allMatches.add(m.group());
        }
        Log.d("Balance", "getBalance: "+allMatches);
        return allMatches;
    }
    List<String>  getCardNo(String Message)
    {
        List<String>allMatches=new ArrayList<String>();
        Pattern cardNumberPattern=Pattern.compile(Constants.CARDNUM_REGEX);
        Matcher m= cardNumberPattern.matcher(Message);
        while(m.find())
        {
            allMatches.add(m.group());
        }
        Log.d("Cardnumber", "getCardnumber: "+allMatches);
        return allMatches;
    }
   String getTransactionDate(String Message)
    {
        String transactionDate=null;
        List<String>allMatches=new ArrayList<String>();
        Pattern datePattern=Pattern.compile(Constants.TRANSACDATE_REGIX);
        Matcher m= datePattern.matcher(Message);
        if(m.find())
        {
            //allMatches.add(m.group());
            transactionDate=m.group(m.groupCount());
        }
        Log.d("transactiondate", "getTransactionDate: "+allMatches);
        return transactionDate;
    }

}
