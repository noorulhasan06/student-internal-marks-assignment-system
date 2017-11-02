package com.mcs.du.noorul.smas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateMarks extends AppCompatActivity {

    EditText et_marks;
    TextView tv_student_id, tv_student_name;
    Button submit;
    Toolbar toolbar;
    String student_id, student_name, marks_minor, teacher_id, hash, course_id, subject_id;
    String marks;
    Bundle bundle;
    JSONObject jsonObject;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_marks);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        toolbar.setElevation(100);
        CharSequence title = "Update";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        bundle = getIntent().getExtras();
        storeData(bundle);
        et_marks.setText(marks_minor);
        tv_student_id.setText(student_id);
        tv_student_name.setText(student_name);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClickListener(v);
            }
        });
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

    public void submitButtonClickListener(View v){
        marks = et_marks.getText().toString();
        if(InternetConnection.isConnected(this,v)) {
            BackgroundWorker bgworker = new BackgroundWorker(UpdateMarks.this);
            bgworker.execute("UpdateMarks", "teacher", teacher_id, hash, course_id, subject_id, student_id, marks);
        } else {
            Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitButtonClickListener(v);
                }
            }).show();
        }
    }
    public void initialize(){
        et_marks = (EditText) findViewById(R.id.et_marks);
        tv_student_id = (TextView) findViewById(R.id.tv_student_id);
        tv_student_name = (TextView) findViewById(R.id.tv_student_name);
        submit = (Button) findViewById(R.id.btn_sumbit);
    }
    public void storeData(Bundle bundle){
        teacher_id = bundle.getString("teacher_id");
        hash = bundle.getString("hash");
        course_id = bundle.getString("course_id");
        subject_id = bundle.getString("subject_id");
        student_id = bundle.getString("student_id");
        student_name = bundle.getString("student_name");
        String result = bundle.getString("result");
        Log.d("error202",teacher_id+hash+result);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    jsonObject = jsonArray.getJSONObject(2);
                    marks_minor = jsonObject.getString("marks_minor");
                } else{
                    builder.setTitle("Error2").setMessage(jsonObject.getString("message2")).show();
                }
            } else{
                builder.setTitle("Error1").setMessage(jsonObject.getString("message1")).show();
            }
        } catch (JSONException e) {
            Log.d("Error", e.toString());
        }
    }
}
