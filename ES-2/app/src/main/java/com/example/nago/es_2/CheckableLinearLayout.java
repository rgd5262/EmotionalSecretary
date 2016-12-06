package com.example.nago.es_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016-12-02.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable
{
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean isChecked()
    {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        return cb.isChecked() ;

    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        setChecked(cb.isChecked() ? false : true) ;
        // setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }
    }
}