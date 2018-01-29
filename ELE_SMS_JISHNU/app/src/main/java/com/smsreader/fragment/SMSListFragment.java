package com.smsreader.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.smsreader.R;
import com.smsreader.adapter.SMSGridAdapter;
import com.smsreader.constants.Constants;



public class SMSListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = SMSListFragment.class.getSimpleName();

    private static final int ID_SMS_LIST_LOADER = 111;
    private GridView mGridView;
    private SMSGridAdapter mSmsGridAdapter;

    public static SMSListFragment newInstance() {
        SMSListFragment fragment = new SMSListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSmsGridAdapter = new SMSGridAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sms_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        mGridView = view.findViewById(R.id.gridview);
        mGridView.setAdapter(mSmsGridAdapter);
        initLoader();
    }

    private void initLoader(){
        getLoaderManager().initLoader(ID_SMS_LIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id){
            case ID_SMS_LIST_LOADER:
                String where_condition = "(" + Constants.SMSListColumns.COLUMN_BODY + " LIKE '%" +Constants.QUERY_PARAM + "%' COLLATE NOCASE)";
                cursorLoader = new CursorLoader(getActivity(), Constants.CONTENT_URI_SMS_LIST, null, where_condition, null, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSmsGridAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSmsGridAdapter.swapCursor(null);
    }
}
