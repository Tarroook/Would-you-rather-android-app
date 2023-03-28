package com.tarook.wouldyourather.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tarook.wouldyourather.R;

import java.util.List;

public class WYRAdapter extends ArrayAdapter<WouldYouRather> {

    public WYRAdapter(Context context, List<WouldYouRather> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WouldYouRather wyr = getItem(position);
        if(convertView == null)
            convertView = View.inflate(getContext(), R.layout.wyr_cell, null);

        TextView mainText = convertView.findViewById(R.id.main_text);
        mainText.setText(wyr.getOptions().get(0));

        return convertView;
    }
}
