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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class teacher_student extends AppCompatActivity {

    ListView listView;
    Bundle bundle;
    Toolbar toolbar;
    int index =0 ;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String teacher_id, course_id, subject_id, hash;
    String[] courseArray, subjectArray, courseName, subjectName , studentArray, studentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student);
        listView =(ListView)findViewById(R.id.listview);
        bundle = getIntent().getExtras();
        storeData(bundle);
        toolbar= (Toolbar) findViewById(R.id.ab);
        toolbar.setElevation(100);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        CharSequence title = "Students";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,studentName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCourseClilckListener(adapterView, view, i, l);
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

    public void onCourseClilckListener(AdapterView<?> adapterView, View view, int i, long l){
        Log.d("error501",((TextView) view).getText().toString());
        if(InternetConnection.isConnected(this,view)) {
            BackgroundWorker bgworker = new BackgroundWorker(teacher_student.this);
            bgworker.execute("getStudentData", "teacher", teacher_id, hash, course_id, subject_id, studentArray[i], studentName[i]);
        } else {
            Snackbar.make(view,"Internet Required",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void storeData(Bundle bundle){
        teacher_id = bundle.getString("teacher_id");
        hash = bundle.getString("hash");
        course_id = bundle.getString("course_id");
        subject_id = bundle.getString("subject_id");
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
                    studentArray = new String[jsonArray.length()-2];
                    studentName = new String[jsonArray.length()-2];
                    for(int i=2, j=0; i<jsonArray.length(); i++, j++){
                        jsonObject = jsonArray.getJSONObject(i);
                        studentArray[j] = jsonObject.getString("student_id");
                        studentName[j] = jsonObject.getString("student_name");
                    }
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
