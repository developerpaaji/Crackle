package com.blackhole.crackle;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by bhavneet singh on 06-Feb-19.
 */

public class OutlineButton extends AppCompatButton {
    private boolean selected=false;
    private String text;
    public OutlineButton(Context context,String text) {
        super(context);
        int padding=getDp(2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        this.text=text;
        int margin=getDp(40);
        params.setMargins(margin,margin,margin,margin);
        setLayoutParams(params);
        setClipToOutline(true);
        setElevation(0.0f);
        setText(text);
        setAllCaps(false);
        changeSelected(false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selected=!selected;
                setActivated(selected);
                changeSelected(selected);
            }
        });
    }
    public boolean isSelected()
    {
        return selected;
    }
    public String getText()
    {
        return text;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        changeSelected(selected);
    }

    private int getDp(int px)
    {
        return (int)(px / getContext().getResources().getDisplayMetrics().density);
    }
    private void changeSelected(boolean selected)
    {
        if(!selected)
        {
            setBackground(getResources().getDrawable(R.drawable.button_border));
            setTextColor(getResources().getColor(R.color.colorBlueAccent));
        }
        else
        {
            setBackground(getResources().getDrawable(R.drawable.button_filled));
            setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }
}
