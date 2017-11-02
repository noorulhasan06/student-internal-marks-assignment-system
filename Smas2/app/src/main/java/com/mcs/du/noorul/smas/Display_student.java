package com.mcs.du.noorul.smas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Display_student extends AppCompatActivity
{
    TextView tv_student_id, tv_student_name,tv_email,tv_course_id,tv_department_id,tv_semester;
    String student_id, student_name, email,course_id, department_id, semester, hash;
    Toolbar toolbar;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        toolbar.setElevation(100);
        CharSequence title = "Student Profile";
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

        if (id == R.id.action_settings)
        {
            //Toast.makeText(this, "Hey you just hit", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,MainActivity.class);
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
        tv_student_id.setText(student_id);
        tv_student_name.setText(student_name);
        tv_email.setText(email);
        tv_semester.setText(semester);
        tv_department_id.setText(department_id);
        tv_course_id.setText(course_id);
    }
    public void retriveFromBundle(Bundle bundle){
        student_id = bundle.getString("student_id",student_id);
        student_name = bundle.getString("student_name",student_name);
        email = bundle.getString("email",email);
        department_id = bundle.getString("department_id",department_id);
        course_id = bundle.getString("course_id",course_id);
        semester = bundle.getString("semester",semester);
    }

     public void initialize(){
         tv_student_id = (TextView) findViewById(R.id.student_id);
         tv_student_name = (TextView) findViewById(R.id.student_name);
         tv_email = (TextView) findViewById(R.id.email);
         tv_semester = (TextView) findViewById(R.id.semester);
         tv_department_id = (TextView) findViewById(R.id.department_id);
         tv_course_id = (TextView) findViewById(R.id.course_id);
     }
}