package com.softperl.urdunovelscollections.TextviewUtil;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.softperl.urdunovelscollections.FontUtil.Font;

/**
 * Created by hp on 5/20/2018.
 */

public class LemonMilkTextview extends AppCompatTextView {
    public LemonMilkTextview(Context context) {
        super(context);
        setFont(context);
    }

    public LemonMilkTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public LemonMilkTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Font.lemon_milk_font(context));
    }
}

