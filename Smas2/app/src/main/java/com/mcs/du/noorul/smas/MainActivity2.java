package com.mcs.du.noorul.smas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    String[] nameArray;
    String[] sidArray;
    AlertDialog.Builder builder;
    Bundle bundle;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        storeData();
        data = new ArrayList<DataModel>();
        for (int i = 0; i < nameArray.length; i++) {
            data.add(new DataModel(
                    nameArray[i],
                    sidArray[i]
            ));
        }
        adapter = new Adapter(data);
        recyclerView.setAdapter(adapter);
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
            int itemS=recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder =recyclerView.findViewHolderForAdapterPosition(itemS);
           TextView Name = (TextView) viewHolder.itemView.findViewById(R.id.Name);
           String SelectedName=(String) Name.getText();

            Toast.makeText(MainActivity2.this, SelectedName, Toast.LENGTH_SHORT).show();
        }

    }

    public void storeData(){
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        result = bundle.getString("result");
        builder = new AlertDialog.Builder(this);
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getString("error1").equals("no")) {
                jsonObject = jsonArray.getJSONObject(1);
                if (jsonObject.getString("error2").equals("no")) {
                    nameArray = new String[jsonArray.length()-2];
                    sidArray = new String[jsonArray.length()-2];
                            for(int i=2, j=0; i<jsonArray.length(); i++, j++){
                                jsonObject = jsonArray.getJSONObject(i);
                                nameArray[j] = jsonObject.getString("name");
                                sidArray[j] = jsonObject.getString("sid");
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
