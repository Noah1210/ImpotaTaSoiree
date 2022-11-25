package com.noah.npardon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.beans.Soiree;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DaoSoiree;
import com.noah.npardon.daos.DelegateAsyncTask;

import java.util.List;

public class InfoSoiree extends Activity {
    private ArrayAdapter<Menbre> menbreArrayAdapter;
    private List<Menbre> menbres;
    private TextView showSoireeInfo, showSoireeLib;
    private Button delButton, subButton ;
    private Soiree so = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_soiree);

        showSoireeLib = (TextView) findViewById(R.id.tvSoireeLib);
        showSoireeInfo = (TextView) findViewById(R.id.tvSoireeInfo);
        subButton = (Button) findViewById(R.id.bInscrire);
        delButton = (Button) findViewById(R.id.bDel);

        menbres = DaoMenbre.getInstance().getLocalMenbres();
        menbreArrayAdapter = new ArrayAdapter<Menbre>(this, android.R.layout.simple_list_item_1, menbres);
        ((ListView) findViewById(R.id.lvSoiree)).setAdapter(menbreArrayAdapter);

        so = (Soiree) this.getIntent().getSerializableExtra("so");
        showSoireeLib.setText(so.getLibelleCourt());
        showSoireeInfo.setText(so.getDescriptif() + "\n\nLe " + so.getDateDebut() + " à " + so.getHeureDebut() + "\n\nSoirée de " + so.getLogin());
        getMenbres();

        if(!Connexion.menbreConnecte.getLogin().equals(so.getLogin())){
            delButton.setVisibility(View.GONE);
            subButton.setOnClickListener(view -> {
                OnClickSubButton();
            });
        }else{
            subButton.setVisibility(View.GONE);
            delButton.setOnClickListener(view -> {
                int i = (int) this.getIntent().getSerializableExtra("index");
                OnClickDelButton(i);
            });
        }

        ((Button) findViewById(R.id.bAnnuler)).setOnClickListener(v -> {
            finish();
        });
    }

    private void OnClickDelButton(int i) {
        DaoSoiree.getInstance().delSoiree(so, new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                boolean res = (boolean) result;
                if (res == true) {
                    Toast.makeText(getApplicationContext(), "Vous avez bien supprimer la soirée", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("index", i);
                    setResult(4,returnIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "La soirée n'a pas pu être supprimer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OnClickSubButton() {
        DaoSoiree.getInstance().verifInscription(so, new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object Firstresult) {
                boolean firstRes = (boolean) Firstresult;
                if (firstRes == true) {
                    desinscrire();
                }else {
                    inscire();
                }
            }});
    }

    private void inscire() {
        DaoSoiree.getInstance().inscrireSoiree(so, new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                boolean res = (boolean) result;
                if (res == true) {
                    menbres.add(Connexion.menbreConnecte);
                    menbreArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Vous êtes maintenant inscrit à la soirée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous inscrire", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void desinscrire() {
        DaoSoiree.getInstance().desinscrireSoiree(so, new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                boolean res = (boolean) result;
                if (res == true) {
                    getMenbres();
                    Toast.makeText(getApplicationContext(), "Vous n'êtes plus inscrit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous désinscrire", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getMenbres(){
        DaoMenbre.getInstance().getParticipants(so, new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                menbreArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}