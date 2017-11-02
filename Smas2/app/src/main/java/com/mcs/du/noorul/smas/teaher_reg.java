package com.mcs.du.noorul.smas;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class teaher_reg extends AppCompatActivity {

    EditText et_id,et_password,et_repassword,et_name,et_email;
    Button btn_register;
    Toolbar toolbar;
    String teacher_id,password,repassword,teacher_name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaher_reg);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        toolbar.setElevation(100);
        CharSequence title = "Registration";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClickListener(v);
            }
        });
    }

    public void initialize(){
        et_id=(EditText)findViewById(R.id.et_id_register);
        et_password=(EditText)findViewById(R.id.et_password_register);
        et_repassword = (EditText) findViewById(R.id.et_repassword_register);
        et_name=(EditText)findViewById(R.id.et_name_register);
        et_email=(EditText)findViewById(R.id.et_email_register);
        btn_register=(Button)findViewById(R.id.btn_register);
    }

    public void getValues(){
        teacher_id=et_id.getText().toString();
        password=et_password.getText().toString();
        repassword = et_repassword.getText().toString();
        teacher_name=et_name.getText().toString();
        email=et_email.getText().toString();
    }

    public void registerButtonClickListener(View v){
        getValues();
        if(teacher_id.isEmpty() || teacher_name.isEmpty() || password.isEmpty() || email.isEmpty() || repassword.isEmpty()){
            Toast.makeText(this, "All Fields are required.", Toast.LENGTH_SHORT).show();
        }else{
            if(password.equals(repassword)){
                if(isConnected(v)) {
                    BackgroundWorker bgWorker = new BackgroundWorker(this);
                    bgWorker.execute("register", "teacher", teacher_id, password, teacher_name, email);
                } else{
                    Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            registerButtonClickListener(v);
                        }
                    }).show();
                }
            } else {
                Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        }
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