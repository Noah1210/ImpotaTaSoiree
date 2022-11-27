package com.noah.npardon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DelegateAsyncTask;

public class Account extends Activity {
    TextView myAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        myAcc = findViewById(R.id.tvAccount);
        myAcc.setText(Connexion.menbreConnecte.getPrenom() + " " + Connexion.menbreConnecte.getNom());

        ((ImageView)findViewById(R.id.ivBaldMan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, BaldMan.class));
            }
        });

        ((Button)findViewById(R.id.bDeconnexion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMenbre.getInstance().getDeconnexion(new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        boolean res = (boolean) result;
                        if (res == true) {
                            Toast.makeText(getApplicationContext(), "Vous vous êtes bien déconnecter", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Connexion.class);
                            finish();
                            Account.this.startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous déconnecter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ((Button)findViewById(R.id.bDelAcc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMenbre.getInstance().getDelAccount(new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        boolean res = (boolean) result;
                        if (res == true) {
                            Toast.makeText(getApplicationContext(), "Le compte "+Connexion.menbreConnecte.getLogin()+" a bien été supprimer", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent(getApplicationContext(), Connexion.class);
                            setResult(4,returnIntent);
                            finish();
                            startActivity(returnIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Votre compte n'a pas été supprimer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}