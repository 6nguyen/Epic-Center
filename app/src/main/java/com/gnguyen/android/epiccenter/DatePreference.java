package com.gnguyen.android.epiccenter;

/**
 * Created by gnguy on 3/23/2017.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePreference extends DialogPreference {
    private int lastDate = 0;
    private int lastMonth = 0;
    private int lastYear = 0;
    private String dateval;
    private CharSequence mSummary;
    private DatePicker picker = null;
    public static int getYear(String dateval) {
        String[] pieces = dateval.split("-");
        return (Integer.parseInt(pieces[0]));
    }

    public static int getMonth(String dateval) {
        String[] pieces = dateval.split("-");
        return (Integer.parseInt(pieces[1]));
    }

    public static int getDate(String dateval) {
        String[] pieces = dateval.split("-");
        return (Integer.parseInt(pieces[2]));
    }

    public DatePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new DatePicker(getContext());

        // setCalendarViewShown(false) attribute is only available from API level 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            picker.setCalendarViewShown(false);
        }

        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.updateDate(lastYear, lastMonth - 1, lastDate);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastYear = picker.getYear();
            lastMonth = picker.getMonth()+1;
            lastDate = picker.getDayOfMonth();

            String dateval = String.valueOf(lastYear) + "-"
                    + String.valueOf(lastMonth) + "-"
                    + String.valueOf(lastDate);

            if (callChangeListener(dateval)) {
                persistString(dateval);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        dateval = null;

        if (restoreValue) {
            if (defaultValue == null) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(cal.getTime());
                dateval = getPersistedString(formatted);
            } else {
                dateval = getPersistedString(defaultValue.toString());
            }
        } else {
            dateval = defaultValue.toString();
        }
        lastYear = getYear(dateval);
        lastMonth = getMonth(dateval);
        lastDate = getDate(dateval);
    }

    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();

        dateval = text;

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    public String getText() {
        return dateval;
    }

    public CharSequence getSummary() {
        return mSummary;
    }

    public void setSummary(CharSequence summary) {
        if (summary == null && mSummary != null || summary != null
                && !summary.equals(mSummary)) {
            mSummary = summary;
            notifyChanged();
        }
    }
}
