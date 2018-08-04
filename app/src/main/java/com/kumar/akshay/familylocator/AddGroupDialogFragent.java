package com.kumar.akshay.familylocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.kumar.akshay.familylocator.Database.DBHelper;

public class AddGroupDialogFragent extends DialogFragment {

    AddGroupDialogListener addGroupDialogListener;
    String mUserId;
    boolean isUserAvailable = false;
    Context context;

    public interface AddGroupDialogListener{
        public void addGroup(String gName);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_groups_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.text_view_group);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        final TextView textViewWarning = (TextView) view.findViewById(R.id.text_view_warning);
        final EditText editText = (EditText) view.findViewById(R.id.gNameET);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                textViewWarning.setText("");
            }
        });
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!new DBHelper(context).checkIfGroupExists(editText.getText().toString())){
                            addGroupDialogListener.addGroup(editText.getText().toString());
                        }else
                            textViewWarning.setText("Group Name already exist. Please choose a different one.");
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addGroupDialogListener = (AddGroupDialogListener) activity;
    }
}
