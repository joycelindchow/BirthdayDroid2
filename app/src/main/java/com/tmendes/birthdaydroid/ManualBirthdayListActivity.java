package com.tmendes.birthdaydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tmendes.birthdaydroid.adapters.ManualAdapter;

import java.util.ArrayList;

public class ManualBirthdayListActivity extends AppCompatActivity {

    TextView textView_TAG;
    TextView textView_phoneNo;
    ListView listView_manualBirthdayList;
    ArrayList<ManualBirthday> manualBirthdayArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_birthday_list);

        textView_TAG = findViewById(R.id.textView_TAG);
        listView_manualBirthdayList = findViewById(R.id.listView_manualList);

        //populate list
        getList(new ListCallback() {
            @Override
            public void onCallback(ArrayList<ManualBirthday> list) {
                ManualAdapter adapter = new ManualAdapter(ManualBirthdayListActivity.this, list);
                listView_manualBirthdayList.setAdapter(adapter);
            }
        });

        listView_manualBirthdayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View parentView = (View) view.getParent();
                String phoneNo = ((TextView) parentView.findViewById(R.id.textView_phoneNo)).getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNo, null));
                intent.putExtra("sms_body", "Happy Birthday!");
                startActivity(intent);
            }
        });
    }

    private void getList(final ListCallback listCallback){
        FirebaseFirestore.getInstance().collection("manualBirthday")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<ManualBirthday> list = new ArrayList<>();
                        for (ManualBirthday manualBirthday : task.getResult().toObjects(ManualBirthday.class)) {
                            list.add(manualBirthday);
                        }
                        listCallback.onCallback(list);
                    }
                });
    }

    private interface ListCallback{
        void onCallback(ArrayList<ManualBirthday> list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
