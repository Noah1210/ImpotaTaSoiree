package com.noah.npardon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.npardon.R;
import com.noah.npardon.daos.DaoMenbre;

public class Connexion extends Activity {
    private TextView inputLogin, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        DaoMenbre.init(this);
        TextView btnInscription = findViewById(R.id.txClickInscription);

        inputLogin = findViewById(R.id.txLogin);
        inputPassword = findViewById(R.id.txPassword);
        inputLogin.setText("noah.pardon");
        inputPassword.setText("12345");

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Inscription.class));
            }
        });
        ((Button) findViewById(R.id.bConnexion)).setOnClickListener(v -> {
            OnClickConnexion();
        });


    }

    private void OnClickConnexion() {
        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();
        if (login.isEmpty()) {
            showError(inputLogin, "Please enter a login");
        } else if (password.isEmpty()) {
            showError(inputLogin, "Please enter a password");
        } else {
            DaoMenbre.getInstance().getConnexion(login, password);

        }
    }
    private void showError(TextView input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}