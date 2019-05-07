package com.myapplicationdev.android.p03_classjournal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Page2 extends AppCompatActivity {
    int requestCodeForAdd = 1;
    ListView lvGA;
    Button btnInfo, btnAdd, btnEmail;
    ArrayAdapter dg;
    ArrayList<DG> dailyCA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        lvGA = findViewById(R.id.listViewGA);
        btnAdd = findViewById(R.id.buttonAdd);
        btnInfo = findViewById(R.id.buttonInfo);
        btnEmail = findViewById(R.id.buttonEmail);

        dailyCA = new ArrayList<DG>();

        Intent i = getIntent();
        final String type = i.getStringExtra("type");
        this.setTitle("Info for " + type);

        if(type.equals("C347")) {
            dailyCA.add(new DG("B", "C347", 1));
            dailyCA.add(new DG("C", "C347", 2));
        } else {
            dailyCA.add(new DG("C", "C302", 1));
            dailyCA.add(new DG("A", "C302", 2));
            dailyCA.add(new DG("A", "C302", 3));
        }
        dg = new DGadapter(this, R.layout.row, dailyCA);
        lvGA.setAdapter(dg);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Intent to display data
                Intent rpIntent = new Intent(Intent.ACTION_VIEW);
                // Set the URL to be used.
                if(type.equals("C347")) {
                    rpIntent.setData(Uri.parse("https://www.rp.edu.sg/schools-courses/courses/full-time-diplomas/full-time-courses/modules/index/C347"));
                } else {
                    rpIntent.setData(Uri.parse("https://www.rp.edu.sg/schools-courses/courses/full-time-diplomas/full-time-courses/modules/index/C302"));
                }

                startActivity(rpIntent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Page2.this, add.class);
                i.putExtra("week", lvGA.getAdapter().getCount()+1);
                startActivityForResult(i, requestCodeForAdd);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                StringBuilder sb = new StringBuilder();
                for (DG string : dailyCA) {
                    sb.append("Week " + string.getWeek() + ": DG: " + string.getDgGrade());
                    sb.append("\n");
                }
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"jason_lim@rp.edu.sg"});
                email.putExtra(Intent.EXTRA_SUBJECT,
                        "");
                email.putExtra(Intent.EXTRA_TEXT,
                        "Hi Faci \n I am ... \n Please see my remarks so far, thank you! \n " + sb.toString());
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,
                        "Choose an Email client :"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (data != null) {
                String newGrade = data.getStringExtra("grade");

                dailyCA.add(new DG(newGrade, "C302", lvGA.getAdapter().getCount()+1));
                dg.notifyDataSetChanged();
            }
        }
    }

}
