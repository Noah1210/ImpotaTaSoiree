package com.noah.npardon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DelegateAsyncTask;

public class Connexion extends Activity {
    private TextView inputLogin, inputPassword;
    public static Menbre menbreConnecte = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        TextView btnInscription = findViewById(R.id.txClickInscription);

        inputLogin = findViewById(R.id.txLogin);
        inputPassword = findViewById(R.id.txPassword);

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Inscription.class);
                startActivityForResult(intent, 2);
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
            showError(inputPassword, "Please enter a password");
        } else {
            DaoMenbre.getInstance().getConnexion(login, password, new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {
                    Menbre me = (Menbre) result;
                    if (me != null) {
                        Toast.makeText(getApplicationContext(), "Bonjour " + me.getPrenom(), Toast.LENGTH_SHORT).show();
                        menbreConnecte = me;
                        Intent intent = new Intent(getApplicationContext(), ToutesSoirees.class);
                        intent.putExtra("me", me);
                        finish();
                        Connexion.this.startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous connecter", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void showError(TextView input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == 3) {
                Menbre me = (Menbre) data.getSerializableExtra("me");
                inputLogin.setText(me.getLogin());
                inputPassword.setText(me.getPassword());
            } else if(resultCode == 4){
                inputLogin.setText("");
                inputPassword.setText("");
            }else{

            }
        }
    }
}