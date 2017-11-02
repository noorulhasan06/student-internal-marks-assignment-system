package com.mcs.du.noorul.smas;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class add_sub_teacher extends AppCompatActivity {

    String[] department_ids, department_names;
    String[] course_ids, course_names;
    String[] subject_ids, subject_names;
    Toolbar toolbar;
    Spinner sp_course, sp_department, sp_subject;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayAdapter<String> adapter_department,adapter_course, adapter_subject;
    Button btn_submit;
    Bundle bundle;
    String result, course_id, course_name, department_id, department_name, subject_id, subject_name, teacher_id, hash;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_teacher);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        CharSequence title = "Add Subjects";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        result = bundle.getString("result");
        teacher_id = bundle.getString("teacher_id");
        hash = bundle.getString("hash");
        Log.d("error",result);
        adapter_department.add("....Select Department....");
        addDataIntoDepartmentAdapter(result);
        sp_department.setAdapter(adapter_department);
        sp_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedDepartment(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedCourse(parent,view,position,id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClickListener(v);
            }
        });
    }
    public void initialize(){
        sp_course = (Spinner) findViewById(R.id.sp_course_register);
        sp_department = (Spinner) findViewById(R.id.sp_department_register);
        sp_subject = (Spinner) findViewById(R.id.sp_subject_register);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        department_ids = new String[20];
        course_ids = new String[20];
        department_names = new String[20];
        course_names = new String[20];
        subject_names = new String[20];
        subject_ids = new String[20];
        adapter_department = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_course = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_subject = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    public void submitButtonClickListener(View v){
        getValues();
        if(subject_id.isEmpty() || department_id.isEmpty() || course_id.isEmpty()){
            Toast.makeText(this, "All Fields are required.", Toast.LENGTH_SHORT).show();
        }else{
            if(InternetConnection.isConnected(this,v)) {
                BackgroundWorker bgWorker = new BackgroundWorker(this);
                bgWorker.execute("addSubject", "teacher", department_id, course_id, subject_id, teacher_id, hash);
                adapter_department.clear();
                adapter_course.clear();
                adapter_subject.clear();
            } else{
                Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitButtonClickListener(v);
                    }
                }).show();
            }
        }
    }

    public void getValues(){
        if(sp_subject.getSelectedItemPosition()>0){
            subject_id = subject_ids[sp_subject.getSelectedItemPosition()-1];
        } else{
            subject_id = null;
        }
        if(sp_department.getSelectedItemPosition()>0){
            department_id = department_ids[sp_department.getSelectedItemPosition()-1];
        } else{
            department_id = null;
        }
        if(sp_course.getSelectedItemPosition()>0){
            course_id = course_ids[sp_course.getSelectedItemPosition()-1];
        } else{
            course_id = null;
        }
    }
    public void onItemSelectedCourse(AdapterView<?> parent, View view, int position, long id) {
        adapter_subject.clear();
        adapter_subject.add("....Select Subject....");
        if(position>0){
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            try {
                if(InternetConnection.isConnected(this,view)) {
                    String result = backgroundWorker.execute("getSubject", "teacherUpdate", teacher_id, hash, course_ids[position - 1]).get();
                    Log.d("error11", result);
                    addDataIntoSubjectAdapter(result);
                    sp_subject.setAdapter(adapter_subject);
                } else{
                    Snackbar.make(view,"Internet Required",Snackbar.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    public void onItemSelectedDepartment(AdapterView<?> parent, View v, int position, long id) {
        adapter_course.clear();
        adapter_subject.clear();
        adapter_course.add("....Select Course....");
        if(position>0){
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            try {
                if(InternetConnection.isConnected(this, v)) {
                    String result = backgroundWorker.execute("getCourse", "student", department_ids[position - 1]).get();  // remains student
                    Log.d("error11", result);
                    addDataIntoCourseAdapter(result);
                    sp_course.setAdapter(adapter_course);
                } else{
                    Snackbar.make(v,"Internet Required",Snackbar.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    public void addDataIntoCourseAdapter(String result){
        index = 0;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    for(int i=2; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        course_ids[index] = jsonObject.getString("course_id");
                        course_names[index] = jsonObject.getString("course_name");
                        //semesters[index] = jsonObject.getInt("semesters");
                        if(!course_names[index].isEmpty()){
                            adapter_course.add(course_names[index]);
                        }
                        index++;
                    }
                }
                else{
                    builder.setTitle("Error2").setMessage(jsonObject.getString("message2")).show();
                }
            }
            else{
                builder.setTitle("Error1").setMessage(jsonObject.getString("message1")).show();
            }
        } catch (JSONException e) {
            Log.d("Error", e.toString());
        }
    }
    public void addDataIntoSubjectAdapter(String result){
        index = 0;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    for(int i=2; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        subject_ids[index] = jsonObject.getString("subject_id");
                        subject_names[index] = jsonObject.getString("subject_name");
                        //semesters[index] = jsonObject.getInt("semesters");
                        if(!subject_names[index].isEmpty()){
                            adapter_subject.add(subject_names[index]);
                        }
                        index++;
                    }
                }
                else{
                    builder.setTitle("Error2").setMessage(jsonObject.getString("message2")).show();
                }
            }
            else{
                builder.setTitle("Error1").setMessage(jsonObject.getString("message1")).show();
            }
        } catch (JSONException e) {
            Log.d("Error", e.toString());
        }
    }


    public void addDataIntoDepartmentAdapter(String result){
        index = 0;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    for(int i=2; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        department_ids[index] = jsonObject.getString("department_id");
                        department_names[index] = jsonObject.getString("department_name");
                        if(!department_names[index].isEmpty()){
                            adapter_department.add(department_names[index]);
                        }
                        index++;
                    }
                }
                else{
                    builder.setTitle("Error2").setMessage(jsonObject.getString("message2")).show();
                }
            }
            else{
                builder.setTitle("Error1").setMessage(jsonObject.getString("message1")).show();
            }
        } catch (JSONException e) {
            Log.d("Error", e.toString());
        }
    }
}
