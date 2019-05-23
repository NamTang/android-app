package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.model.Account;
import com.example.service.RestService;
import com.example.util.AESCrypt;
import com.example.util.ApiUrl;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassword;
    TextView txtError;
    Gson gson = new Gson();
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
    }

    private void addControls() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        txtError = findViewById(R.id.txtError);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.layout_loading_dialog, null));
        builder.setCancelable(false);
        alert = builder.create();
    }

    public void login(View view) {
        alert.show();

        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if ((userName == null || userName.equals("")) || (password == null || password.equals(""))) {
            txtError.setText(R.string.login_missing_value);
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
//                alert.dismiss();
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (result == null) {
                txtError.setText(R.string.error);
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            } else {
                txtError.setText(result);
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }
        alert.dismiss();
    }
}
