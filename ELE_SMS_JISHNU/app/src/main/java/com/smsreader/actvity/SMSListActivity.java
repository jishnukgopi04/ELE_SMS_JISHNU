package com.smsreader.actvity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.smsreader.R;
import com.smsreader.fragment.SMSListFragment;
import com.smsreader.service.SMSReaderService;

public class SMSListActivity extends AppCompatActivity {

    public static final String TAG = SMSListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SMS_READ = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smslist);
        initToolbar();
        if (hasSMSReadPermission()) {
            startSMSCopying();
        }
        if(savedInstanceState == null){
            addSMSListFragment();
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_sms_list));
    }

    private void addSMSListFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, SMSListFragment.newInstance(), SMSListFragment.TAG).commit();
    }


    private boolean hasSMSReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Enable Permission from Settings", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        REQUEST_CODE_SMS_READ);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SMS_READ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startSMSCopying();
                } else {
                    Toast.makeText(this, "Enable Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void startSMSCopying() {
        Intent intent = new Intent(this, SMSReaderService.class);
        startService(intent);
    }


}
