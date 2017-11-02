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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherHome extends AppCompatActivity {
    Button btn_profile, btn_marks;
    Toolbar toolbar;
    String hash, teacher_id, teacher_name, email, department_id, department_name, course_id, course_name;
    Bundle bundle;
    String result;
    JSONArray jsonArray;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        CharSequence title = "Information";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        initialize();
        bundle = getIntent().getExtras();
        result = bundle.getString("result");
        storeTeacherInfo(result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            ///Toast.makeText(this, "Hey you just hit", Toast.LENGTH_SHORT).show();
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


    public void storeTeacherInfo(String result) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    jsonObject = jsonArray.getJSONObject(2);
                    if (jsonObject.getString("error3").equals("no")) {
                        jsonObject = jsonArray.getJSONObject(3);
                        if (jsonObject.getString("error4").equals("no")) {
                            teacher_id = jsonObject.getString("teacher_id");
                            teacher_name = jsonObject.getString("teacher_name");
                            hash = jsonObject.getString("hash");
                            email = jsonObject.getString("email");
                        } else {
                            builder.setTitle("Error4").setMessage(jsonObject.getString("message4")).show();
                        }
                    } else {
                        builder.setTitle("Error3").setMessage(jsonObject.getString("message3")).show();
                    }
                } else {
                    builder.setTitle("Error2").setMessage(jsonObject.getString("message2")).show();
                }
            } else {
                builder.setTitle("Error1").setMessage(jsonObject.getString("message1")).show();
            }
        } catch (JSONException e) {
            Log.d("error", e.toString());
        }
    }

    public void initialize(){
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_marks = (Button) findViewById(R.id.btn_marks);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileButtonClickListener(v);
            }
        });
        btn_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marksButtonClickListener(v);
            }
        });
    }
    public void profileButtonClickListener(View v){
        Intent intent = new Intent(this,Display_teacher.class);
        Bundle bundle = new Bundle();
        bundle = storeInBundle(bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public Bundle storeInBundle(Bundle bundle){
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("email",email);
        bundle.putString("hash", hash);
        return bundle;
    }
    public void marksButtonClickListener(View v){
        if(InternetConnection.isConnected(this,v)) {
            BackgroundWorker bgworker = new BackgroundWorker(this);
            bgworker.execute("getCourse", "teacher", teacher_id, hash);
        } else{
            Snackbar.make(v,"Internet Required",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    marksButtonClickListener(v);
                }
            }).show();
        }
    }
}
