package com.tmendes.birthdaydroid.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tmendes.birthdaydroid.ManualBirthday;
import com.tmendes.birthdaydroid.R;

import java.util.ArrayList;

public class ManualAdapter extends ArrayAdapter {
    private ArrayList<ManualBirthday> manualBirthdayArrayList;
    private Activity context;
    private static final String TAG = "ManualAdapter";

    public ManualAdapter(Activity context, ArrayList<ManualBirthday> manualBirthdayArrayList) {
        super(context, R.layout.manual_list_adapter, manualBirthdayArrayList);
        this.context = context;
        this.manualBirthdayArrayList = manualBirthdayArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.manual_list_adapter, null, true);
        //initialise widgets

        TextView textView_name = listViewItem.findViewById(R.id.textView_Name);
        TextView textView_birthDate = listViewItem.findViewById(R.id.textView_DOB);
        TextView textView_sign = listViewItem.findViewById(R.id.textView_Sign);
        TextView textView_chineseSign = listViewItem.findViewById(R.id.textView_chineseSign);
        TextView textView_phoneNo = listViewItem.findViewById(R.id.textView_phoneNo);

        ManualBirthday manualBirthday = manualBirthdayArrayList.get(position);
        textView_name.setText(manualBirthday.getName());
        textView_birthDate.setText(manualBirthday.getDate());
        textView_sign.setText(manualBirthday.getSign());
        textView_chineseSign.setText(manualBirthday.getChineseSign());
        textView_phoneNo.setText(manualBirthday.getPhoneNo());

        return listViewItem;
    }
}
