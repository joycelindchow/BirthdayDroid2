package com.tmendes.birthdaydroid;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManualBirthday implements Serializable {
    String name;
    String date;
    String sign;
    String phoneNo;
    String chineseSign;

    private int day;
    private int month;
    private int year;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final String TAG = "ManualBirthday";

    public ManualBirthday(String name, String date, String sign, String ChineseSign, String phoneNo, int day) {
        this.name = name;
        this.date = date;
        this.sign = sign;
        this.chineseSign = ChineseSign;
        this.day = day;
    }

    public ManualBirthday(String name, String date, String phoneNo) {
        this.name = name;
        this.date = date;
        this.phoneNo = phoneNo;
    }

    public ManualBirthday() {

    }

    public void setSign() {
        //extract month from string date
        try {
            Date container = simpleDateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(container);

            month = calendar.get(Calendar.MONTH);
        } catch (ParseException e) {
            Log.d(TAG, "parse Error");
        }

        //create a switch case to determine the sign
        switch (month) {
            case 0: // Jan
                if ((day >= 21) && (day <= 31)) {
                    sign = "Aquarius";
                } else {
                    sign = "Capricorn";
                }
                break;
            case 1: // Feb
                if ((day >= 20) && (day <= 29)) {
                    sign = "Pisces";
                } else {
                    sign = "Aquarius";
                }
                break;
            case 2: // Mar
                if ((day >= 21) && (day <= 31)) {
                    sign = "Aries";
                } else {
                    sign = "Pisces";
                }
                break;
            case 3: // Apr
                if ((day >= 20) && (day <= 30)) {
                    sign = "Taurus";
                } else {
                    sign = "Aries";
                }
                break;
            case 4: //May
                if ((day >= 20) && (day <= 31)) {
                    sign = "Gemini";
                } else {
                    sign = "Taurus";
                }
                break;
            case 5: // Jun
                if ((day >= 21) && (day <= 30)) {
                    sign = "Cancer";
                } else {
                    sign = "Gemini";
                }
                break;
            case 6: // Jul
                if ((day >= 23) && (day <= 31)) {
                    sign = "Leo";
                } else {
                    sign = "Cancer";
                }
                break;
            case 7: // Aug
                if ((day >= 22) && (day <= 31)) {
                    sign = "Virgo";
                } else {
                    sign = "Leo";
                }
                break;
            case 8: // Sep
                if ((day >= 23) && (day <= 30)) {
                    sign = "Libra";
                } else {
                    sign = "Virgo";
                }
                break;
            case 9: // Oct
                if ((day >= 23) && (day <= 31)) {
                    sign = "Scorpio";
                } else {
                    sign = "Libra";
                }
                break;
            case 10: // Nov
                if ((day >= 22) && (day <= 30)) {
                    sign = "Sagittarius";
                } else {
                    sign = "Scorpio";
                }
                break;
            case 11:
                if ((day >= 22) && (day <= 31)) {
                    sign = "Capricorn";
                } else {
                    sign = "Sagittarius";
                }
                break;
        }

        //set sign
        this.sign = sign;
    }

    public void setChineseSign() {
        //extract year from string date
        try {
            Date container = simpleDateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(container);

            year = calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            Log.d(TAG, "parse Error");
        }

        //create a switch case to determine the sign
        switch (year % 12) {
            case 0:
                chineseSign = "Monkey";
                break;

            case 1:
                chineseSign = "Rooster";
                break;

            case 2:
                chineseSign = "Dog";
                break;

            case 3:
                chineseSign = "Pig";
                break;

            case 4:
                chineseSign = "Rat";
                break;

            case 5:
                chineseSign = "Ox";
                break;

            case 6:
                chineseSign = "Tiger";
                break;

            case 7:
                chineseSign = "Rabbit";
                break;

            case 8:
                chineseSign = "Dragon";
                break;

            case 9:
                chineseSign = "Snake";
                break;

            case 10:
                chineseSign = "Horse";
                break;

            case 11:
                chineseSign = "Goat";
                break;

        }
        // set chinese sign
        this.chineseSign = chineseSign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSign() {
        return sign;
    }

    public String getChineseSign() {
        return chineseSign;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}