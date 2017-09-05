package id.masoft.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.SpassFingerprint;

import id.masoft.lockedrecorder.activities.MainActivity;
import id.masoft.lockedrecorder.R;


public class LoginActivity extends Activity implements MyPassCallback {

    MyPass myPass;
    Button btnSetting,btnSecret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myPass = new MyPass(this);
        myPass.setCallback(this);
        try {
            myPass.initialize();
        }catch (SsdkUnsupportedException e){
            showErrorDialog("SDK PASS",e.getMessage());
            return;
        }catch (UnsupportedOperationException e){
            showErrorDialog("OPERATION PASS",e.getMessage());
            return;
        }
        if (myPass.hasRegisteredFinger){
            myPass.setActive(true);
            if (myPass.getActive()){
                myPass.handleIdentifyWithDialog(true);
            }else{
                showErrorDialog("Fingerprint Activated","You need to activate the fingerprint security..");
            }

            Log.d("TAG", "onCheckedChanged: switch already registered");
        }else {
            //Change to handler
            myPass.handleRegisterFinger();
        }

        btnSecret = (Button)findViewById(R.id.btn_secret);
        btnSetting = (Button)findViewById(R.id.btn_setting_login);

        btnSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPass.getActive()){
                    myPass.handleIdentifyWithDialog(true);
                }else{
                    showErrorDialog("Fingerprint Activated","You need to activate the fingerprint security..");
                }
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPass.handleRegisterFinger();
//                Intent setting = new Intent(LoginActivity.this,SettingPassActivity.class);
//                LoginActivity.this.startActivity(setting);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }


    void showErrorDialog(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public void onFinishedIdentify(int eventStatus) {
        if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
            Intent secret = new Intent(LoginActivity.this, MainActivity.class);
            this.startActivity(secret);
        }
    }
    @Override
    public void onFinishedRegister(boolean register) {
    }
}
