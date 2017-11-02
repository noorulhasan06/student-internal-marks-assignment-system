package com.mcs.du.noorul.smas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Display_teacher extends AppCompatActivity {

    TextView tv_teacher_id, tv_teacher_name,tv_email;
    Toolbar toolbar;
    String teacher_id, teacher_name, email, hash;
    Button btn_addsubject;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_teacher);
        initialize();
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        CharSequence title = "Teacher Profile";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        retriveFromBundle(bundle);
        show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,teacher_in.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    public void show(){
        tv_teacher_id.setText(teacher_id);
        tv_teacher_name.setText(teacher_name);
        tv_email.setText(email);
    }
    public void retriveFromBundle(Bundle bundle){
        teacher_id = bundle.getString("teacher_id",teacher_id);
        teacher_name = bundle.getString("teacher_name",teacher_name);
        email = bundle.getString("email",email);
        hash = bundle.getString("hash", null);
    }
    public void addSubject(View view)
    {
        if(view.getId()==R.id.btn_addsubject) {
            if(InternetConnection.isConnected(this,view)) {
                BackgroundWorker bgworker = new BackgroundWorker(this);
                bgworker.execute("getDepartment", "teacher", teacher_id, hash);
            } else{
                Snackbar.make(view,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSubject(v);
                    }
                }).show();
            }
        }
    }
    public void initialize(){
        tv_teacher_id = (TextView) findViewById(R.id.teacher_id);
        tv_teacher_name = (TextView) findViewById(R.id.teacher_name);
        tv_email = (TextView) findViewById(R.id.email);
        toolbar= (Toolbar) findViewById(R.id.ab);
        btn_addsubject= (Button) findViewById(R.id.btn_addsubject);
    }

}
