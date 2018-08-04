package com.kumar.akshay.familylocator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.akshay.familylocator.Database.FamilyDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddUserDialogFragment extends DialogFragment {

    Context context;
    EditText usernameET, emailET;
    TextView textView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final String group_id = getArguments().getString("groupId");
        final View view = inflater.inflate(R.layout.add_user_dialog, null);
        textView = view.findViewById(R.id.text_view_geofence);
        usernameET = view.findViewById(R.id.usernameET);
        emailET = view.findViewById(R.id.emailET);


        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String username = usernameET.getText().toString();
                        String email = emailET.getText().toString();
                        if (validate(username, email)) {
                            Map<String, String> map = new HashMap<>();
                            map.put("user_name", username);
                            map.put("email_id", email);
                            map.put("group_id", group_id);
                            map.put("init_user_id", LauncherActivity.mUserid);
                            FamilyDatabase.getmInstance(context).addToRequestQueue("addUserToGroup", map);
                        }
                        //addGeoFenceListener.onDialogAddButtonClick(AddGeofencesDialogFragment.this, geofenceName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddUserDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private boolean validate(String username, String email) {
        if (username.isEmpty()) {
            usernameET.setError("No input");
            return false;
        } else if (email.isEmpty()) {
            emailET.setError("No input");
            return false;
        } else if (email.contains("@") && email.contains(".")) {
            emailET.setError("Invalid input");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
