package com.example.stufftoremindmeof2022;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Reminder {
    private String message;
    private int    minutes;
    private Date   startDateTime;

    public Reminder(String message, int minutes) {
        this.message = message;
        this.minutes = minutes;
        this.startDateTime = new Date();
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public int getMinutes() {
        return minutes;
    }
    public void setMinutes(int minutes) { this.minutes = minutes; }


    // make reminder responsible for adding self to layout -- more OO oriented
    public void addTextViewToLayout(LinearLayout ll, Context context,
                                    LinearLayout.LayoutParams textLayoutParams) {
        TextView tv = new TextView(context);
        ///tv.setTypeface(null, Typeface.BOLD);
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm");
        // spannable to make partial sentence italic
        final SpannableStringBuilder sbSpannable = new SpannableStringBuilder(this.getMessage() + "\n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t - start time: " + fmt.format(startDateTime));
        sbSpannable.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), this.getMessage().length(), sbSpannable.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.d("Reminder", "addTextViewToLayout - " + sbSpannable);
        tv.setText(sbSpannable);
        tv.setTextColor(context.getResources().getColor(R.color.text_view_font_color));
        tv.setTextSize(18.0f);
        //tv.setLines(2);
        //tv.setSingleLine(true);  TODO: check if deprecated
        //tv.setMaxHeight(100);
        tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        //tv.setPadding(10, 10, 10, 10);
        //LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        //llParams.setMargins(50, 50, 50, 50);
        //tv.setLayoutParams(llParams);
        //tv.setHorizontallyScrolling(true);
        ll.addView(tv, textLayoutParams);
    }
}
