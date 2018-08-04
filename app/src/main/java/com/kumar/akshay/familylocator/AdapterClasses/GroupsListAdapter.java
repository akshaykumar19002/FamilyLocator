package com.kumar.akshay.familylocator.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.GroupMessage;
import com.kumar.akshay.familylocator.R;

import java.util.List;

public class GroupsListAdapter extends ArrayAdapter<GroupMessage> {
    Context context;

    public GroupsListAdapter(@NonNull Context context, @LayoutRes int resource, List<GroupMessage> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.group_list_message, parent, false);
        }

        GroupMessage groupMessage = getItem(position);
        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_group_list);
        TextView groupNameTextView = view.findViewById(R.id.groupNameTextView);
        TextView groupAdminNameTextView = view.findViewById(R.id.groupAdminNameTextView);
        groupNameTextView.setText(groupMessage.getGroupname());
        groupAdminNameTextView.setText(new DBHelper(context).getUserNameFromUserId(groupMessage.getGroupAdmin()));
        return view;
    }

}
