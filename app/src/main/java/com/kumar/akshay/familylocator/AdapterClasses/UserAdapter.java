package com.kumar.akshay.familylocator.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kumar.akshay.familylocator.MessageClasses.UserMessage;
import com.kumar.akshay.familylocator.R;

import java.util.List;

import static android.view.View.GONE;


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

        TextView userTextView = convertView.findViewById(R.id.userTextView);
        TextView uidTextView = convertView.findViewById(R.id.uidTextView);

        UserMessage message = getItem(position);

        userTextView.setText(message.getUsername());
        uidTextView.setText(message.getGroupsToWhichUserBelongs());

        return convertView;
    }
}
