package com.mcs.du.noorul.smas;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.mcs.du.noorul.smas.R.id.textView;

public class choose extends AppCompatActivity implements View.OnClickListener{

    TextView tx;
    Button stu,teach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        tx=(TextView)findViewById(textView);
        Typeface font= Typeface.createFromAsset(getAssets(),"BRLNSR.TTF");
        tx.setTypeface(font);
        stu=(Button)findViewById(R.id.student);
        teach=(Button)findViewById(R.id.teachers);
        stu.setOnClickListener(this);
        teach.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view.getId()==R.id.student)
        {
            i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else
        if(view.getId()==R.id.teachers) {
            i = new Intent(this, teacher_in.class);
            startActivity(i);
        }
    }
}
