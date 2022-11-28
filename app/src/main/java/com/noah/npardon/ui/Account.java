package com.noah.npardon.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DelegateAsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Account extends Activity {
    private TextView myAcc;
    private ImageView pfpImg;
    private Uri uri;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        myAcc = findViewById(R.id.tvAccount);
        myAcc.setText(Connexion.menbreConnecte.getPrenom() + " " + Connexion.menbreConnecte.getNom());
        pfpImg = findViewById(R.id.ivAcc);

        pfpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((ImageView) findViewById(R.id.ivBaldMan)).setOnClickListener(new View.OnClickListener() {
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

        ((Button) findViewById(R.id.bDelAcc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMenbre.getInstance().getDelAccount(new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        boolean res = (boolean) result;
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
                });
            }
        });

    }

    public void getImageFromGallery() {
        Intent GalIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            try {
                uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ImageView myImage = (ImageView) findViewById(R.id.ivAcc);
                    myImage.setImageBitmap(bitmap);
                    //ProfilePictureUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Log.d("error", "onActivityResult: " + e.toString());
            }
        }
    }
}