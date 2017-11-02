package com.mcs.du.noorul.smas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_id;
    EditText et_password;
    Button btn_login,btn_register;
    String result, id,password;
    FloatingActionButton fab_login,fab_reg;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_login= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab_reg= (FloatingActionButton) findViewById(R.id.floatingActionButton2);
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
            if(InternetConnection.isConnected(this, v)) {
                BackgroundWorker bgworker = new BackgroundWorker(this);
                bgworker.execute("login", "student", id, password);
            } else{
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
        if(InternetConnection.isConnected(this, v)) {
            BackgroundWorker bgworker = new BackgroundWorker(this);
            bgworker.execute("getDepartment", "student");
        } else{
            Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRegisterButtonClickListener(v);
                }
            }).show();
        }
    }
}
