package com.example.hello_android3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mValues;
    private int mTextColor;
    private int mDropdownTextColor;

    public CustomSpinnerAdapter(Context context, int resource, String[] values, int textColor, int dropdownTextColor) {
        super(context, resource, values);
        mContext = context;
        mValues = values;
        mTextColor = textColor;
        mDropdownTextColor = dropdownTextColor;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(mDropdownTextColor);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(mTextColor);
        return view;
    }
}
