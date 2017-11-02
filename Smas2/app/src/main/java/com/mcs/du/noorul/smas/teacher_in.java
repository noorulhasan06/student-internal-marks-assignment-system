package com.mcs.du.noorul.smas;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class teacher_in extends AppCompatActivity {

    EditText et_id;
    EditText et_password;
    FloatingActionButton fab_reg,fab_login;
    String result, id,password;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_in);

        fab_reg= (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab_login= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);
        fab_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClickListener(v);
            }
        });
        fab_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onLoginButtonClickListener(v);
            }
        });
    }

    public void onLoginButtonClickListener(View v){
        id = et_id.getText().toString();
        password = et_password.getText().toString();
        if(id.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"All Fields are required.",Toast.LENGTH_SHORT).show();
        } else {
            if(isConnected(v)) {
                BackgroundWorker bgworker = new BackgroundWorker(this);
                bgworker.execute("login", "teacher", id, password);
            } else {
                Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoginButtonClickListener(v);
                    }
                }).show();
            }
        }
    }

    public void onRegisterButtonClickListener(View v){
        Intent intent=new Intent(this,teaher_reg.class);
        startActivity(intent);
    }
    public boolean isConnected(View v){
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            return true;
        } else{
            return false;
        }
    }
}