package com.noah.npardon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.npardon.R;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.daos.DaoMenbre;

public class Soiree extends Activity {
    private ArrayAdapter<Menbre> membreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soiree);

        membreArrayAdapter = new ArrayAdapter<Menbre>(this, android.R.layout.simple_list_item_1,
                DaoMenbre.getInstance().getLocalEtudiants());

        Menbre me = (Menbre) this.getIntent().getSerializableExtra("me");
        ((TextView)findViewById(R.id.tvMyAcc)).setText(me.getPrenom());
        ((ImageView)findViewById(R.id.ivAcc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Soiree.this, Account.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}