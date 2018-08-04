package com.kumar.akshay.familylocator.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.MessageClasses.LocationMessage;
import com.kumar.akshay.familylocator.R;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<LocationMessage> {

    Context context;

    public MessageAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView userTextView = convertView.findViewById(R.id.userNameTextView);
        TextView locationTextView = convertView.findViewById(R.id.locationTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        DBHelper dbHelper = new DBHelper(context);

        LocationMessage message = getItem(position);

        userTextView.setText(dbHelper.getUserNameFromUserId(message.getUid()));
        locationTextView.setText(message.getCurrentLocation());
        timeTextView.setText(message.getTime());

        return convertView;
    }
}
