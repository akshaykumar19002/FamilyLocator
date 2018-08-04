package com.kumar.akshay.familylocator;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kumar.akshay.familylocator.MessageClasses.LocationMessage;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by LENOVO on 12-07-2017.
 */

public class UserAdapter extends ArrayAdapter<UserMessage> {
    public UserAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_adapter_layout, parent, false);
        }

        TextView userTextView = (TextView) convertView.findViewById(R.id.userTextView);
        TextView uidTextView = (TextView) convertView.findViewById(R.id.uidTextView);

        UserMessage message = getItem(position);

        userTextView.setText(message.getUsername());
        uidTextView.setText(message.getUserEmail());

        return convertView;
    }
}
