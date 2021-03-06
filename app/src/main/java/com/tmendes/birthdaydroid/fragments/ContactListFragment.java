/*
 * Copyright (C) 2015-2016 The Food Restriction Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tmendes.birthdaydroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tmendes.birthdaydroid.BirthDay;
import com.tmendes.birthdaydroid.Contact;
import com.tmendes.birthdaydroid.MainActivity;
import com.tmendes.birthdaydroid.R;
import com.tmendes.birthdaydroid.adapters.BirthDayArrayAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ContactListFragment extends Fragment {
    // Search EditText
    private EditText inputSearch;

    // Adapter
    private BirthDayArrayAdapter adapter;

    // Context
    private Context ctx;

    private SwipeRefreshLayout refreshLayout;
    private boolean swipeDown;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contact_list,
                container, false);

        ctx = container.getContext();

        PreferenceManager.setDefaultValues(ctx, R.xml.preferences, false);

        adapter = new BirthDayArrayAdapter(ctx,
                ((MainActivity) Objects.requireNonNull(getActivity())).getBirthday().getBirthDayList());

        ListView listView = v.findViewById(R.id.lvContacts);
        listView.setTextFilterEnabled(true);
        listView.setDividerHeight(0);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        inputSearch = v.findViewById(R.id.inputSearch);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = inputSearch.getText().toString();
                adapter.getFilter().filter(text);
            }
        });

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputManager).hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
                }
            }
        });

        refreshLayout = v.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        isTodayADayToCelebrate();
                    }
                }
        );

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        swipeDown = prefs.getBoolean("swipe_down_to_notify", false);
        refreshLayout.setEnabled( swipeDown );
        ((MainActivity) Objects.requireNonNull(getActivity())).getBirthday().refresh();
        updateSortSettings();
    }

    private void updateSortSettings() {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        int sortInput = Integer.valueOf(s.getString("sort_input", "0"));
        int sortMethod = Integer.valueOf(s.getString("sort_method", "0"));
        adapter.sort(sortInput, sortMethod);
    }

    private void isTodayADayToCelebrate() {
        if (swipeDown) {
            BirthDay birthDays = ((MainActivity) Objects.requireNonNull(getActivity())).getBirthday();
            ArrayList<Contact> notifications = birthDays.shallWeCelebrate();
            if (notifications.size() == 0) {
                Toast.makeText(ctx, getResources().getString(R.string.birthday_scan_not_found),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ctx, getResources().getString(R.string.birthday_scan_found),
                        Toast.LENGTH_LONG).show();
                for (Contact contact : notifications) {
                    birthDays.postNotification(contact);
                }
            }
        }
        refreshLayout.setRefreshing(false);
    }

}
