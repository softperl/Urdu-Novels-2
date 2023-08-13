package com.softperl.urdunovelscollections.TextviewUtil;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.softperl.urdunovelscollections.FontUtil.Font;

/**
 * Created by hp on 5/20/2018.
 */

public class RalewayRegularTextview extends AppCompatTextView {
    public RalewayRegularTextview(Context context) {
        super(context);
        setFont(context);
    }

    public RalewayRegularTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public RalewayRegularTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Font.raleway_regular_font(context));
    }
}

