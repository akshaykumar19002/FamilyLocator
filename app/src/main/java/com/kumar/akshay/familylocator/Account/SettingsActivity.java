package com.kumar.akshay.familylocator.Account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kumar.akshay.familylocator.R;
import java.util.Arrays;

import static com.kumar.akshay.familylocator.LauncherActivity.ANONYMOUS;
import static com.kumar.akshay.familylocator.LauncherActivity.RC_SIGN_IN;

public class SettingsActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView usernameTextView;
    private String mUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Account Settings");
        setContentView(R.layout.activity_settings);
        imageView = (ImageView) findViewById(R.id.user_image_view);
        usernameTextView = (TextView) findViewById(R.id.userNameTextView);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        switch (item.getItemId()) {
//            case R.id.sign_out_menu:
//                AuthUI.getInstance().signOut(this);
//                return true;
//            case R.id.delete_account_menu:
//                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        onSignedOutCleanup();
//                        Toast.makeText(SettingsActivity.this,"Account Deleted", Toast.LENGTH_LONG).show();
//                        callingFirebaseUI();
//                    }
//                });
//                return true;
//            case R.id.update_email_menu:
//                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
//                // Get the layout inflater
//                LayoutInflater inflater = getLayoutInflater();
//
//                // Inflate and set the layout for the dialog
//                // Pass null as the parent view because its going in the dialog layout
//                builder.setView(inflater.inflate(R.layout.change_email_layout, null));
//                // Add action buttons
//                builder.setPositiveButton(R.string.update_email_id, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        EditText emailET = (EditText)findViewById(R.id.email_id);
//                        user.updateEmail(emailET.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(SettingsActivity.this, "Email Updated", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        }).show();
//                return true;
//            case R.id.info_button:
//                Toast.makeText(this, "You can edit your account setting here", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.reset_password_menu:
//                mFirebaseAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(SettingsActivity.this,"Password Reset Email Sent", Toast.LENGTH_LONG).show();
//                    }
//                });
                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }


    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    private void callingFirebaseUI(){
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(
//                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
//                        .build(),
//                RC_SIGN_IN);
    }

    public void addingImage(View view){
        Intent i = new Intent(this, AddingUserImage.class);
        startActivity(i);
    }

}
