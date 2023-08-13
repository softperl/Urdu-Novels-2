package com.softperl.urdunovelscollections.EditTextUtil;

import android.content.Context;
import android.util.AttributeSet;

import com.softperl.urdunovelscollections.FontUtil.Font;

public class RalewayRegularEditText extends androidx.appcompat.widget.AppCompatEditText {
    public RalewayRegularEditText(Context context) {
        super(context);
        setFont(context);
    }

    public RalewayRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public RalewayRegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Font.raleway_regular_font(context));
    }
}

