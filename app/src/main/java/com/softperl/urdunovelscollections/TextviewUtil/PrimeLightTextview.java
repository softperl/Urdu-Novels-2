package com.softperl.urdunovelscollections.TextviewUtil;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.softperl.urdunovelscollections.FontUtil.Font;

public class PrimeLightTextview extends AppCompatTextView {
    public PrimeLightTextview(Context context) {
        super(context);
        setFont(context);
    }

    public PrimeLightTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public PrimeLightTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Font.prime_light_font(context));
    }
}

