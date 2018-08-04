package com.kumar.akshay.familylocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddGeofencesDialogFragment extends DialogFragment {

    public interface AddGeoFenceListener {
        public void onDialogAddButtonClick(DialogFragment dialog, String geofenceName);
    }

    AddGeoFenceListener addGeoFenceListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.add_geofences_dialog, null);
        TextView textView = view.findViewById(R.id.text_view_geofence);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText geofenceNameET = (EditText) view.findViewById(R.id.geofenceNameET);
                        String geofenceName = geofenceNameET.getText().toString();
                        if (geofenceName.equals(""))
                            Toast.makeText(getContext(), "Please enter a geofence name ", Toast.LENGTH_LONG).show();
                        else
                            addGeoFenceListener.onDialogAddButtonClick(AddGeofencesDialogFragment.this, geofenceName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddGeofencesDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            addGeoFenceListener = (AddGeoFenceListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
