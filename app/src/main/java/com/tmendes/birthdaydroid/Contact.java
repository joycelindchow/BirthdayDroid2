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

package com.tmendes.birthdaydroid;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Contact {

    private final Context ctx;

    private final int eventType;

    private final String name;
    private String sign;
    private String signElement;
    private final String key;
    private final String photoURI;
    private final String date;
    private String ChineseSign;

    private int day;
    private int month;
    private int year;
    private int age;
    private int daysAge;

    private final String failMsg;

    private boolean containsYearInfo = false;
    private boolean failOnParseDateString = true;
    private boolean letsCelebrate = false;

    private Calendar bornOn;
    private Calendar nextBirthday;

    private static final long DAY = 60 * 60 * 1000 * 24;

    private final List<String> knownPatterns = Arrays.asList(
            "yyyy-M-dd",
            "yyyy-M-dd hh:mm:ss.SSS",
            "dd-M-y",
            "dd-M-y hh:mm:ss.SSS",
            "--M-dd",
            "--M-dd hh:mm:ss.SSS",
            "dd-M-- hh:mm:ss.SSS",
            "dd-M--"
    );

    public Contact(Context ctx, String key, String name, String date, String photoURI, int eventType) {
        this.ctx = ctx;
        this.key = key;
        this.name = name;
        this.date = date;
        this.photoURI = photoURI;
        this.eventType = eventType;

        failMsg = "";

        parseContactBirthdayField(date);

        if (!failOnParseDateString) {
            setContactBirthDayInfo();
            setContactSign();
            setChineseSign();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void parseContactBirthdayField(String dateString) {
        for (String pattern : knownPatterns) {
            try {
                bornOn = new GregorianCalendar();
                bornOn.setTime(new SimpleDateFormat(pattern).parse(dateString));
                failOnParseDateString = false;
                containsYearInfo = pattern.contains("y");
                break;
            } catch (ParseException ignored) {
                containsYearInfo = false;
                failOnParseDateString = true;
            }
        }

        if (failOnParseDateString) {
            String dialogData = " " + name +
                    "\n" +
                    ctx.getResources().getString(R.string.log_cant_parse, dateString) +
                    "\n";
            failMsg.concat(dialogData);
        }
    }

    private void setContactBirthDayInfo() {
        Calendar actualCal = Calendar.getInstance();
        final int actualYear = actualCal.get(Calendar.YEAR);

        final boolean isLeapYear = ((actualYear % 4 == 0)
                && (actualYear % 100 != 0) || (actualYear % 400 == 0));

        /* Set The Next Birthday Info */
        nextBirthday = (Calendar) bornOn.clone();
        nextBirthday.set(Calendar.YEAR, actualYear);

        boolean birthDayAlreadyPassed = nextBirthday.getTimeInMillis() < actualCal.getTimeInMillis();

        if (birthDayAlreadyPassed) {
            nextBirthday.add(Calendar.YEAR, 1);
        }

        day = bornOn.get(Calendar.DAY_OF_MONTH);
        month = bornOn.get(Calendar.MONTH);
        year = bornOn.get(Calendar.YEAR);

        /* Age */
        if (containsYearInfo) {
            if (year > actualYear) {
                daysAge = 0;
                age = 0;
            } else {
                age = actualYear - year;

                if (!birthDayAlreadyPassed) {
                    --age;
                }

                if (age == 0) {
                    /* For those who are just born */
                    daysAge = (int) ((actualCal.getTimeInMillis() - bornOn.getTimeInMillis()) / DAY);
                }
            }
        } else {
            bornOn.set(Calendar.YEAR, actualYear);
            age = -1;
        }

        /* Party Today \o/ */
        if (isLeapYear) {
            if ((actualCal.get(Calendar.DAY_OF_MONTH) == 1)
                    && (bornOn.get(Calendar.DAY_OF_MONTH) == 29)
                    && (actualCal.get(Calendar.MONTH) == Calendar.MARCH)) {
                letsCelebrate = true;
            } else if (actualCal.get(Calendar.DAY_OF_MONTH) == bornOn.get(Calendar.DAY_OF_MONTH)
                    && actualCal.get(Calendar.MONTH) == bornOn.get(Calendar.MONTH)) {
                letsCelebrate = true;
            }
        } else {
            if (actualCal.get(Calendar.DAY_OF_MONTH) == bornOn.get(Calendar.DAY_OF_MONTH)
                    && actualCal.get(Calendar.MONTH) == bornOn.get(Calendar.MONTH)) {
                letsCelebrate = true;
            }
        }
    }

    private void setContactSign() {
        if (ctx == null) {
            return;
        }

        switch (month) {
            case 0: // Jan
                if ((day >= 21) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_aquarius);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_capricorn);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                }
                break;
            case 1: // Feb
                if ((day >= 20) && (day <= 29)) {
                    sign = ctx.getResources().getString(R.string.sign_pisces);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_aquarius);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                }
                break;
            case 2: // Mar
                if ((day >= 21) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_aries);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_pisces);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                }
                break;
            case 3: // Apr
                if ((day >= 20) && (day <= 30)) {
                    sign = ctx.getResources().getString(R.string.sign_taurus);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_aries);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                }
                break;
            case 4: //May
                if ((day >= 20) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_gemini);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_taurus);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                }
                break;
            case 5: // Jun
                if ((day >= 21) && (day <= 30)) {
                    sign = ctx.getResources().getString(R.string.sign_cancer);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_gemini);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                }
                break;
            case 6: // Jul
                if ((day >= 23) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_leo);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_cancer);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                }
                break;
            case 7: // Aug
                if ((day >= 22) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_virgo);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_leo);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                }
                break;
            case 8: // Sep
                if ((day >= 23) && (day <= 30)) {
                    sign = ctx.getResources().getString(R.string.sign_libra);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_virgo);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                }
                break;
            case 9: // Oct
                if ((day >= 23) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_scorpio);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_libra);
                    signElement = ctx.getResources().getString(R.string.sign_element_air);
                }
                break;
            case 10: // Nov
                if ((day >= 22) && (day <= 30)) {
                    sign = ctx.getResources().getString(R.string.sign_sagittarius);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_scorpio);
                    signElement = ctx.getResources().getString(R.string.sign_element_water);
                }
                break;
            case 11:
                if ((day >= 22) && (day <= 31)) {
                    sign = ctx.getResources().getString(R.string.sign_capricorn);
                    signElement = ctx.getResources().getString(R.string.sign_element_earth);
                } else {
                    sign = ctx.getResources().getString(R.string.sign_sagittarius);
                    signElement = ctx.getResources().getString(R.string.sign_element_fire);
                }
                break;
        }
    }

    private void setChineseSign() {
        if(ctx == null) {
            return;
        }

        switch (year%12) {
            case 0: ChineseSign = ctx.getResources().getString(R.string.chineseSign_monkey);
            break;

            case 1: ChineseSign = ctx.getResources().getString(R.string.chineseSign_rooster);
            break;

            case 2: ChineseSign = ctx.getResources().getString(R.string.chineseSign_dog);
            break;

            case 3: ChineseSign = ctx.getResources().getString(R.string.chineseSign_pig);
            break;

            case 4: ChineseSign = ctx.getResources().getString(R.string.chineseSign_rat);
            break;

            case 5: ChineseSign = ctx.getResources().getString(R.string.chineseSign_ox);
            break;

            case 6: ChineseSign = ctx.getResources().getString(R.string.chineseSign_tiger);
            break;

            case 7: ChineseSign = ctx.getResources().getString(R.string.chineseSign_rabbit);
            break;

            case 8: ChineseSign = ctx.getResources().getString(R.string.chineseSign_dragon);
            break;

            case 9: ChineseSign = ctx.getResources().getString(R.string.chineseSign_snake);
            break;

            case 10: ChineseSign = ctx.getResources().getString(R.string.chineseSign_horse);
            break;

            case 11: ChineseSign = ctx.getResources().getString(R.string.chineseSign_goat);
            break;

        }
    }

    public String getMonthName() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths()[month];
    }

    public int getBirthDayWeek() {
        return bornOn.get(Calendar.DAY_OF_WEEK);
    }

    public String getNextBirthDayWeekName() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getWeekdays()[nextBirthday.get(Calendar.DAY_OF_WEEK)];
    }

    public String getName() {
        return name;
    }

    public String getContactFirstName() {
        String[] firstName = name.split(" ");
        return firstName[0];
    }

    public String getSign() {
        return sign;
    }

    public String getSignElement() {
        return signElement;
    }

    public String getChineseSign() { return ChineseSign; }

    public String getKey() {
        return key;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public String getDate() { return date; }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getDaysAge() { return daysAge; }

    public Calendar getBirthday() {
        return bornOn;
    }

    public int getAge() {
        return age;
    }

    public Long getDaysUntilNextBirthDay() {
        if (shallWeCelebrateToday()) {
            return 0L;
        }
        if (nextBirthday == null) {
            return Long.MAX_VALUE;
        }

        long timeDiffMs;
        long days;

        Calendar now = Calendar.getInstance();

        if (nextBirthday.getTimeInMillis() >= now.getTimeInMillis()) {
            timeDiffMs = nextBirthday.getTimeInMillis() - now.getTimeInMillis();
        } else {
            timeDiffMs = now.getTimeInMillis() - nextBirthday.getTimeInMillis();
        }

        /* Days */
        days = (int) (timeDiffMs / DAY) + 1;

        return days;
    }

    public boolean shallWeCelebrateToday() {
        return letsCelebrate;
    }

    public boolean failOnParseDateString() {
        return failOnParseDateString;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public int getEventType() { return eventType; }
}
