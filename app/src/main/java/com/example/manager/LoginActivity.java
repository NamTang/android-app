package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.Account;
import com.example.service.RestService;
import com.example.util.AESCrypt;
import com.example.util.ApiUrl;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassword;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
    }

    private void addControls() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
    }

    public void login(View view) {
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if ((userName == null || userName.equals("")) || (password == null || password.equals(""))) {
            Toast.makeText(this, R.string.login_missing_value, Toast.LENGTH_SHORT).show();
        } else {
            password = AESCrypt.encrypt(password);
            Account account = new Account(userName, password);

            String result = null;

            try {
                result = new RestService(gson.toJson(account), ApiUrl.login()).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (result.equals("Success")) {
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (result == null) {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
