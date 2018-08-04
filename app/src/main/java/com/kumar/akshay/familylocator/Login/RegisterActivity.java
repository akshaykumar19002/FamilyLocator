package com.kumar.akshay.familylocator.Login;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, emailET, passET;
    Button registerBtn;
    FamilyDatabase fdb;
    DBHelper db;
    int uid;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        random = new Random();
        db = new DBHelper(this);
        fdb = FamilyDatabase.getmInstance(RegisterActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameET = (EditText) findViewById(R.id.editTextName);
        emailET = (EditText) findViewById(R.id.editTextEmail);
        passET = (EditText) findViewById(R.id.editTexrPass);
        registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, pass;
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                pass = passET.getText().toString();
                if (validate(name, email, pass)){
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", generateANewUserId());
                    map.put("user_name", name);
                    map.put("user_email", email);
                    map.put("user_pass", FamilyDatabase.getMD5(pass));
                    fdb.addToRequestQueue("register", map);
                }
            }
        });
    }

    private String generateANewUserId() {
        uid = random.nextInt(999999999);
        while (!db.verifyUserId(Integer.toString(uid))){
            while (!(uid >= 100000000)){
                uid = random.nextInt(999999999);
            }
        }
        Log.v("RegisterActivity", Integer.toString(uid));
        return Integer.toString(uid);
    }

    private boolean validate(String name, String email, String pass) {
        if (TextUtils.isEmpty(name)){
            nameET.setError("Invalid input");
            return false;
        }
        if (TextUtils.isEmpty(email)){
            emailET.setError("Invalid input");
            return false;
        }
        if (TextUtils.isEmpty(pass)){
            passET.setError("Invalid input");
            return false;
        }
        if (pass.length() < 5){
            passET.setError("Password too short");
            return false;
        }
        if (!email.contains("@")&&!email.contains(".")){
            emailET.setError("Invalid email");
            return false;
        }
        return true;
    }

}
