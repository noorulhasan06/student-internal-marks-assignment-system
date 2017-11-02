package com.mcs.du.noorul.smas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class BackgroundWorker extends AsyncTask<String,String,String> {

    ProgressDialog progressDialog;
    AppCompatActivity context;
    String id,password,name,email,course_id,department_id,semester, teacher_id, subject_id, marks_minor;
    AlertDialog.Builder builder;
    String title,message, hash, student_id, student_name;
    Bundle bundle;
    String call_type,type;
    String result = null;
    boolean error = false;
    ConnectivityManager connectivityManager;
    BackgroundWorker(AppCompatActivity activity)
    {
        context = activity;
    }

    @Override
    protected String doInBackground(String... params) {
           type = params[0];
            if (type.equals("login")) {
                id = params[2];
                password = params[3];
                call_type = params[1];
                result = send("login_learning.php", "type", call_type, "id", id, "password", password);
                check4(result);
            } else if (params[0].equals("register")) {
                call_type = params[1];
                if (call_type.equals("student")) {
                    id = params[2];
                    password = params[3];
                    name = params[4];
                    email = params[5];
                    course_id = params[7];
                    semester = params[8];
                    department_id = params[6];
                    result = send("register_learning.php", "type", call_type, "id", id, "password", password, "name", name,
                            "email", email, "course_id", course_id, "semester", semester, "department_id", department_id);
                    check4(result);
                } else if (call_type.equals("teacher")) {
                    id = params[2];
                    password = params[3];
                    name = params[4];
                    email = params[5];
                    result = send("register_learning.php", "type", call_type, "id", id, "password", password, "name", name, "email", email);
                    check4(result);
                }
                check4(result);
            } else if (params[0].equals("getStudentData")) {
                call_type = params[1];
                if (call_type.equals("teacher")) {
                    teacher_id = params[2];
                    hash = params[3];
                    course_id = params[4];
                    subject_id = params[5];
                    student_id = params[6];
                    student_name = params[7];
                    result = send("getStudentData.php", "type", call_type, "teacher_id", teacher_id, "hash", hash, "subject_id", subject_id,
                            "student_id", student_id);
                    //Log.d("error909","this"+result);
                    check2(result);
                }
            } else if (type.equals("getDepartment")) {
                call_type = params[1];

                if (call_type.equals("teacher") || call_type.equals("teacherUpdate")) {
                    teacher_id = params[2];
                    hash = params[3];
                    result = send("getDepartment.php", "type", call_type, "teacher_id", teacher_id, "hash", hash);
                } else if (call_type.equals("student")) {
                    result = send("getDepartment.php", "type", call_type);
                }
                //Log.d("error",result);
                check2(result);
            } else if (type.equals("getCourse")) {
                call_type = params[1];
                if (call_type.equals("student")) {
                    department_id = params[2];
                    result = send("getCourse.php", "type", call_type, "department_id", department_id);
                    //Log.d("error",result);
                    check2(result);
                } else if (call_type.equals("teacher")) {
                    teacher_id = params[2];
                    hash = params[3];
                    result = send("getCourse.php", "type", call_type, "teacher_id", teacher_id, "hash", hash);
                    check2(result);
                    //Log.d("error",result);
                    check2(result);
                }
            } else if (type.equals("student_marks")) {
                call_type = params[1];
                student_id = params[2];
                hash = params[3];
                result = send("student_marks.php", "student_id", student_id, "hash", hash);
                check2(result);
                //Log.d("error-222",result);
                //check2(result);
            } else if (type.equals("getSubject")) {
                call_type = params[1];
                teacher_id = params[2];
                hash = params[3];
                course_id = params[4];
            /*Log.d("error305","getSubject.php"+ "type"+call_type+"teacher_id"+teacher_id+ "hash"+hash+ "course_id"+course_id);*/
                result = send("getSubject.php", "type",call_type,"teacher_id",teacher_id, "hash", hash, "course_id",course_id);
                check2(result);
                //Log.d("error-222",result);
                return result;
            } else if (type.equals("getStudent")) {
                call_type = params[1];
                teacher_id = params[2];
                hash = params[3];
                course_id = params[4];
                subject_id = params[5];
                result = send("getStudent.php", "type", type, "teacher_id", teacher_id, "hash", hash, "course_id", course_id, "subject_id", subject_id);
                check2(result);
                //Log.d("error-222",result);
            } else if (type.equals("UpdateMarks")) {
                call_type = params[1];
                teacher_id = params[2];
                hash = params[3];
                course_id = params[4];
                subject_id = params[5];
                student_id = params[6];
                marks_minor = params[7];
                //Log.d("error-299",type+teacher_id+hash+student_id+subject_id);
                result = send("UpdateMarks.php", "type", type, "teacher_id", teacher_id, "hash", hash, "course_id", course_id, "subject_id",
                        subject_id, "student_id", student_id, "marks_minor", marks_minor);
                check2(result);
                //Log.d("error-222",result);
            } else if (type.equals("addSubject")) {
                call_type = params[1];
                department_id = params[2];
                course_id = params[3];
                subject_id = params[4];
                teacher_id = params[5];
                hash = params[6];
            /*Log.d("error405","addSubject.php"+"type"+type+ "teacher_id"+teacher_id+ "department_id"+ department_id+"course_id"+course_id+
                    "subject_id"+ subject_id+ "hash"+ hash);*/
                result = send("addSubject.php", "type", type, "teacher_id", teacher_id, "department_id", department_id, "course_id", course_id,
                        "subject_id", subject_id, "hash", hash);
                check2(result);
            }
        return result;
    }

    public String send(String... params){

        String login_url = "http://wwwshahincom.000webhostapp.com/smas/php/"+params[0];
        //String login_url = "http://192.168.0.105/smas/php/"+params[0];
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data="";
            for(int i=1;i<params.length;i++) {
                post_data += URLEncoder.encode(params[i], "UTF-8") + "=" + URLEncoder.encode(params[++i], "UTF-8");
                if(i<params.length-1){
                    post_data+="&";
                }
            }
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            result = stringBuilder.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Error", "failed Malformed.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "failed IO.");
        }

        return null;
    }

    public void check4(String result){
        Log.d("error",result);
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(result);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")){
                jsonObject=jsonArray.getJSONObject(1);
                if(jsonObject.getString("error2").equals("no")){
                    jsonObject=jsonArray.getJSONObject(2);
                    if(jsonObject.getString("error3").equals("no")){
                        jsonObject=jsonArray.getJSONObject(3);
                        if(jsonObject.getString("error4").equals("no")){
                            title="Message";
                            message=jsonObject.getString("message4");
                            Log.d("error32", message);
                        }
                        else{
                            error = true;
                            title="Error4";
                            message=jsonObject.getString("message4");
                        }
                    }
                    else{
                        error = true;
                        title="Error3";
                        message=jsonObject.getString("message3");
                    }
                }
                else{
                    error = true;
                    title="Error2";
                    message=jsonObject.getString("message2");
                }
            }
            else{
                error = true;
                title="Error1";
                message=jsonObject.getString("message1");
            }
        } catch (JSONException e) {
            error = true;
            title="Json Error";
            message = e.toString();
        }
    }
    
    public void check2(String result){
        Log.d("error333",result);
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(result);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")){
                jsonObject=jsonArray.getJSONObject(1);
                if(jsonObject.getString("error2").equals("no")){
                            title="Message";
                            message=jsonObject.getString("message2");
                            Log.d("error32", message);
                } else{
                    error = true;
                    title="Error2";
                    message=jsonObject.getString("message2");
                }
            } else{
                error = true;
                title="Error1";
                message=jsonObject.getString("message1");
            }
        } catch (JSONException e) {
            error = true;
            title="Json Error";
            message = e.toString();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog=ProgressDialog.show(context,"Progress Dialog","Wait");

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if(!error) {
            if (type.equals("login")) {
                showToast(message);
                    //Log.d("error103", result);
                    bundle = new Bundle();
                    bundle.putString("result", result);
                    Intent intent = null;
                    if (call_type.equals("student")) {
                        intent = new Intent(context, StudentHome.class);
                    } else if (call_type.equals("teacher")) {
                        intent = new Intent(context, TeacherHome.class);
                    }
                    intent.putExtras(bundle);
                    context.startActivity(intent);
            } else if (type.equals("addSubject")) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            } else if (type.equals("register")) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                if (call_type.equals("student") && !error) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else if (call_type.equals("teacher") && !error) {
                    Intent intent = new Intent(context, teacher_in.class);
                    context.startActivity(intent);
                }
            } else if (type.equals("getStudentData")) {
                //Log.d("error401", result);
                if (!error) {
                    bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("teacher_id", teacher_id);
                    bundle.putString("subject_id", subject_id);
                    bundle.putString("student_id", student_id);
                    bundle.putString("student_name", student_name);
                    bundle.putString("hash", hash);
                    bundle.putString("course_id", course_id);
                    Intent intent = new Intent(context, UpdateMarks.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            } else if (type.equals("getDepartment") && call_type.equals("student")) {
                bundle = new Bundle();
                bundle.putString("result", result);
                //Log.d("error", result);
                Intent intent = new Intent(context, Register.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            } else if (type.equals("getDepartment") && call_type.equals("teacher")) {
                bundle = new Bundle();
                bundle.putString("teacher_id", teacher_id);
                bundle.putString("result", result);
                bundle.putString("hash", hash);
                //Log.d("error", result);
                Intent intent = new Intent(context, add_sub_teacher.class);   //make change
                intent.putExtras(bundle);
                context.startActivity(intent);
            } else if (type.equals("getCourse") && call_type.equals("student")) {
                //Log.d("error404", result);
            } else if (type.equals("getCourse") && call_type.equals("teacher")) {
                //Log.d("error404", result);
                if (!error) {
                    bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("teacher_id", teacher_id);
                    bundle.putString("hash", hash);
                    Intent intent = new Intent(context, teacher_dept.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            } else if (type.equals("student_marks") && call_type.equals("student")) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                if (!error) {
                    bundle = new Bundle();
                    bundle.putString("result", result);
                  //  Log.d("error", result);
                    Intent intent = new Intent(context, student_marks.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            } else if (type.equals("getSubject") && call_type.equals("teacher")) {
                //Log.d("error404", result);
                if (!error) {
                    bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("teacher_id", teacher_id);
                    bundle.putString("hash", hash);
                    bundle.putString("course_id", course_id);
                    Intent intent = new Intent(context, teacher_subject.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            } else if (type.equals("getStudent") && call_type.equals("teacher")) {
                //Log.d("error404", result);
                if (!error) {
                    bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("teacher_id", teacher_id);
                    bundle.putString("hash", hash);
                    bundle.putString("course_id", course_id);
                    bundle.putString("subject_id", subject_id);
                    Intent intent = new Intent(context, teacher_student.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            } else if (type.equals("UpdateMarks") && call_type.equals("teacher")) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.d(title,message);
        }
    }

    public void showToast(String message){
        Toast t = Toast.makeText(context,message,Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);                     // use publishProgress() to call this function
    }
}