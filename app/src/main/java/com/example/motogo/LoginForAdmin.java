package com.example.motogo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginForAdmin extends AppCompatActivity {
    private TextView txtUser, txtPass;
    final String FakeUsername = "admin";
    final String FakePassword = "admin";
    private Button btnLogin;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_for_admin);

        CreateView();

        btnLogin.setOnClickListener(v -> Login());
    }

    private void Login(){
        if (count == 5){
            count = 0 ;
            finish();
        }else{
            CheckInformation();
            count++;
        }
    }


    private void CheckInformation(){
        if (!txtUser.getText().toString().equals(FakeUsername) || !txtPass.getText().toString().equals(FakePassword)){
            Toast.makeText(this, "Wrong Information", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this,AdminBrowseOderActivity.class);
            startActivity(intent);
        }
    }


    private void CreateView(){
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
    }
}