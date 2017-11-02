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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity {
    String[] department_ids, department_names;
    String[] course_ids, course_names;
    Integer[] semesters;
    String semester;
    EditText et_id,et_password,et_repassword,et_semester,et_name,et_email;
    Button btn_register;
    Bundle bundle;
    String result;
    Toolbar toolbar;
    Spinner sp_course, sp_department, sp_semester;
    String student_id,password,repassword,student_name,email,course_id,course_name,department_id,department_name;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayAdapter<String> adapter_department,adapter_course, adapter_semester;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        toolbar.setElevation(100);
        CharSequence title = "Register";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        toolbar.setElevation(100);
        department_ids = new String[20];
        course_ids = new String[20];
        department_names = new String[20];
        course_names = new String[20];
        semesters = new Integer[20];
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        result = bundle.getString("result");
        Log.d("error",result);
        adapter_department = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_course = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_semester = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClickListener(v);
            }
        });
    }

    public void onItemSelectedCourse(AdapterView<?> parent, View view, int position, long id) {
        adapter_semester.clear();
        adapter_semester.add("....Select Semester....");
        if(position>0){
            Log.d("error301",semesters[position-1]+"some text");
            int sems = semesters[position-1];
            for(Integer i=1;i<=sems; i++){
                adapter_semester.add(i.toString());
            }
            sp_semester.setAdapter(adapter_semester);
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
                        semesters[index] = jsonObject.getInt("semesters");
                        if(!course_names[index].isEmpty()){
                            adapter_course.add(course_names[index]);
                        }
                        index++;
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

    public void onItemSelectedDepartment(AdapterView<?> parent, View v, final int position, long id) {
        adapter_course.clear();
        adapter_semester.clear();
        adapter_course.add("....Select Course....");
        if(position>0){
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            try {
                if(InternetConnection.isConnected(this,v)) {
                    String result = backgroundWorker.execute("getCourse","student",department_ids[position-1]).get();
                    Log.d("error11",result);
                    addDataIntoCourseAdapter(result);
                    sp_course.setAdapter(adapter_course);
                } else {
                    Snackbar.make(v,"Internet Required",Snackbar.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(){
        et_id=(EditText)findViewById(R.id.et_id_register);
        et_password=(EditText)findViewById(R.id.et_password_register);
        et_repassword = (EditText) findViewById(R.id.et_repassword_register);
        sp_semester = (Spinner) findViewById(R.id.sp_semester_register);
        et_name=(EditText)findViewById(R.id.et_name_register);
        et_email=(EditText)findViewById(R.id.et_email_register);
        btn_register=(Button)findViewById(R.id.btn_register);
        sp_course = (Spinner) findViewById(R.id.sp_course_register);
        sp_department = (Spinner) findViewById(R.id.sp_department_register);
        sp_semester = (Spinner) findViewById(R.id.sp_semester_register);
    }

    public void getValues(){
        student_id=et_id.getText().toString();
        password=et_password.getText().toString();
        repassword = et_repassword.getText().toString();
        student_name=et_name.getText().toString();
        email=et_email.getText().toString();
        if(sp_semester.getSelectedItemPosition()>0){
            semester = sp_semester.getSelectedItem().toString();
        } else{
            semester = null;
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

    public void registerButtonClickListener(View v){
        getValues();
        if(student_id.isEmpty() || student_name.isEmpty() || password.isEmpty() || email.isEmpty() || repassword.isEmpty() || semester.isEmpty() || department_id.isEmpty() || course_id.isEmpty()){
            Toast.makeText(this, "All Fields are required.", Toast.LENGTH_SHORT).show();
        }else{
            if(password.equals(repassword)){
                if(InternetConnection.isConnected(this,v)) {
                    BackgroundWorker bgWorker = new BackgroundWorker(this);
                    bgWorker.execute("register", "student", student_id, password, student_name, email, department_id, course_id, semester);
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

}