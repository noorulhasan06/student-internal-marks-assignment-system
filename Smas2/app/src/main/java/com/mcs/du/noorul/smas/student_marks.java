package com.mcs.du.noorul.smas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class student_marks extends AppCompatActivity {
    //String[] dept={"cs","math"};
    ListView listView;
    Toolbar toolbar;
    JSONArray jsonArray;
    JSONObject jsonObject;
    int index = 0;
    //String[] subject_id, subject_name, marks_minor;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<MarksDataModel> data;
    static View.OnClickListener myOnClickListener;
    String[] nameArray;
    String[] sidArray, marksArray;
    AlertDialog.Builder builder;
    Bundle bundle;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks);
        initialize();
        toolbar= (Toolbar) findViewById(R.id.ab);
        setSupportActionBar(toolbar);
        toolbar.setElevation(100);
        toolbar.setElevation(100);
        CharSequence title = "Marks";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(250,250,250)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        bundle = getIntent().getExtras();
        storeData(bundle);
        /*listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView dept=(TextView)view;
                BackgroundWorker bgworker = new BackgroundWorker(student_marks.this);
                bgworker.execute("getStudentData",((TextView) view).getText().toString());
            }
        });*/

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<MarksDataModel>();
        for (int i = 0; i < nameArray.length; i++) {
            data.add(new MarksDataModel(
                    nameArray[i],
                    sidArray[i],
                    marksArray[i]
            ));
        }
        adapter = new MarksAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
           // Toast.makeText(this, "Hey you just hit", Toast.LENGTH_SHORT).show();
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


    public void initialize(){
        listView=(ListView)findViewById(R.id.listview);
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            getItem(v);
        }

        private void getItem(View v)
        {
            /*int itemS=recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder =recyclerView.findViewHolderForAdapterPosition(itemS);
            TextView Name = (TextView) viewHolder.itemView.findViewById(R.id.Name);
            String SelectedName=(String) Name.getText();

            Toast.makeText(student_marks.this, SelectedName, Toast.LENGTH_SHORT).show();*/
        }

    }

    public void storeData(Bundle bundle){
        index =0;
        String result = bundle.getString("result");
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    nameArray = new String[jsonArray.length()-2];
                    sidArray = new String[jsonArray.length()-2];
                    marksArray = new String[jsonArray.length()-2];
                    for(int i=2, j=0; i<jsonArray.length(); i++, j++){
                            jsonObject = jsonArray.getJSONObject(i);
                            nameArray[j] = jsonObject.getString("subject_name");
                            sidArray[j] = jsonObject.getString("subject_id");
                            marksArray[j] = jsonObject.getString("marks_minor");
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
