package com.noah.npardon.ui;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.npardon.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DelegateAsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class Account extends Activity {
    private TextView myAcc;
    private ImageView pfpImg;
    private Uri uri;
    private ImageView ivBaldMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        myAcc = findViewById(R.id.tvAccount);
        myAcc.setText(Connexion.menbreConnecte.getPrenom() + " " + Connexion.menbreConnecte.getNom());
        pfpImg = findViewById(R.id.ivAcc);
        ivBaldMan = findViewById(R.id.ivBaldMan);

        pfpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImg();
                ImagePicker.with(Account.this)
                        .cropSquare()
                        .compress(1000)
                        .maxResultSize(1080, 1080)
                        .start(2);
            }
        });
        ivBaldMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, BaldMan.class));
            }
        });

        ((Button) findViewById(R.id.bDeconnexion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMenbre.getInstance().getDeconnexion(new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        boolean res = (boolean) result;
                        deconnexion(res);
                    }
                });
            }
        });
        ((Button) findViewById(R.id.bAnnuler)).setOnClickListener(v -> {
            finish();
        });
        ((Button) findViewById(R.id.bDelAcc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMenbre.getInstance().getDelAccount(new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        boolean res = (boolean) result;
                        delAcc(res);
                    }
                });
            }
        });

    }
    private void deconnexion(Boolean res){
        if (res == true) {
            Toast.makeText(getApplicationContext(), "Vous vous êtes bien déconnecter", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Connexion.class);
            finish();
            Account.this.startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous déconnecter", Toast.LENGTH_SHORT).show();
        }
    }
    private void delAcc(Boolean res){
        if (res == true) {
            Toast.makeText(getApplicationContext(), "Le compte " + Connexion.menbreConnecte.getLogin() + " a bien été supprimer", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent(getApplicationContext(), Connexion.class);
            setResult(4, returnIntent);
            finish();
            startActivity(returnIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Votre compte n'a pas été supprimer", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            uri = data.getData();
            pfpImg.setImageURI(uri);
        }else{
        }
    }

}