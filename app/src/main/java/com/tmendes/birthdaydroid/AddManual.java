package com.tmendes.birthdaydroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tmendes.birthdaydroid.fragments.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddManual extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "AddManualActivity";

    private EditText editText_Name, editText_phoneNo;
    private TextView textView_birthDate;
    private Button button_Save;
    private Button button_Cancel;
    private Button button_viewList;
    private Button button_Calendar;
    private String whyError = "";
    private Date date;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    static final String ADD = "Add";

    protected void onStart() {
        super.onStart();
    }

    protected void establish() {
        editText_Name = findViewById(R.id.editText_AddManual_Name);
        editText_phoneNo = findViewById(R.id.editText_AddManual_phoneNo);
        textView_birthDate = findViewById(R.id.textView_AddManual_BirthDate);
        button_Save = findViewById(R.id.button_AddManual_Save);
        button_Cancel = findViewById(R.id.button_AddManual_Cancel);
        button_Calendar = findViewById(R.id.button_AddManual_Calendar);
        button_viewList = findViewById(R.id.button_AddManual_viewList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_manual);
        establish();

        button_Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve information
                String name = editText_Name.getText().toString();
                String dateString = textView_birthDate.getText().toString();
                String phoneNo = editText_phoneNo.getText().toString();
                Intent values = new Intent(AddManual.this, ManualBirthdayListActivity.class);
                values.putExtra("PhoneNo", phoneNo);
                startActivity(values);

                try {

                    date = simpleDateFormat.parse(dateString);
                }
                catch (ParseException e){
                    Log.d(TAG, "Parse error " + e.getStackTrace().toString());
                }
                //validate input
                if(validate(name,dateString)) {
                    ManualBirthday manualBirthday = new ManualBirthday(name, dateString, phoneNo);
                    manualBirthday.setSign();
                    manualBirthday.setChineseSign();
                    String MBID = UUID.randomUUID().toString();
                    FirebaseFirestore.getInstance().collection("manualBirthday")
                            .document(MBID)
                            .set(manualBirthday)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(getApplicationContext(), ManualBirthdayListActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "failure: " + e.getStackTrace().toString());
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), whyError,Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        button_viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManualBirthdayListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        date = calendar.getTime();
        String dateContainer = simpleDateFormat.format(date);

        textView_birthDate.setText(dateContainer);
    }

    private boolean validate(String name, String dateString) {
        if (checkIfDataNotBlank(name, dateString)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkIfDataNotBlank(String name, String dateString) {
        boolean result = true;
        if(name.equals("") || dateString.equals("")) {
            whyError = "Please fill all the fields";
            result = false;
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), ManualBirthdayListActivity.class);
        startActivity(intent);
    }
}

