package com.kumar.akshay.familylocator.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.R;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences uidSharedPreferences, emailSharedPreference, passwordSharedPreference, typeSharedPreference;
    String email = null, pass = null;
    int type = -1,uid = 0;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FamilyDatabase familyDatabase;
    Map<String, String> map;
    Context context;

    public LoginActivity(){
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        map = new HashMap<>();
        creatingPreferences();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        familyDatabase = FamilyDatabase.getmInstance(this);
        if (type != -1 && email != "" && pass != ""){
            new DBHelper(this).recreatingTables();
            showProgress(true);
            map.clear();
            map.put("email", email);
            map.put("pass", pass);
            familyDatabase.addToRequestQueue("userLogin",map);
        }
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        pass = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            map.clear();
            map.put("email", email);
            map.put("pass", pass);
            familyDatabase.addToRequestQueue("userLogin",map);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    public void showProgress(boolean show) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void creatingPreferences() {
        emailSharedPreference = getSharedPreferences(getString(R.string.email_shared_preference), Context.MODE_PRIVATE);
        passwordSharedPreference = getSharedPreferences(getString(R.string.password_shared_preference), Context.MODE_PRIVATE);
        typeSharedPreference = getSharedPreferences(getString(R.string.type_shared_preference), Context.MODE_PRIVATE);
        uidSharedPreferences = getSharedPreferences(getString(R.string.uid_shared_preference), Context.MODE_PRIVATE);
        email = emailSharedPreference.getString("siginEmailId", "");
        pass = passwordSharedPreference.getString("siginPass", "");
        type = typeSharedPreference.getInt("siginType", -1);
        uid = uidSharedPreferences.getInt("uid", 0);
        Log.e("famLoc", type + "," + email + "," + pass);
    }

    public static int getUid(){
        return uidSharedPreferences.getInt("uid", 0);
    }

    public static void updatingPreferences(int type, String emailId, String password) {
        SharedPreferences.Editor editor = emailSharedPreference.edit();
        editor.putString("siginEmailId", emailId);
        editor.commit();
        editor = passwordSharedPreference.edit();
        editor.putString("siginPass", password);
        editor.commit();
        editor = typeSharedPreference.edit();
        editor.putInt("siginType", type);
        editor.commit();
    }
}

