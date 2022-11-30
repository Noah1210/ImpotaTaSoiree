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
    private String imgB64;
    private ImageView ivBaldMan;
    private String imgB64Send;

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
                        .compress(100)
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

    private void selectImg() {
        pfpImg.setImageBitmap(null);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select image"), 2);
    }

    public static String fileUriToBase64(Uri uri, ContentResolver resolver) {
        String encodedBase64 = "";
        try {
            byte[] bytes = readBytes(uri, resolver);
            encodedBase64 = Base64.encodeToString(bytes, 0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return encodedBase64;
    }

    private static byte[] readBytes(Uri uri, ContentResolver resolver)
            throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = resolver.openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the
        // byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }


    private static String uriToB64(Uri uri, ContentResolver resolver)  {
        String b64=null;

        try {
            InputStream inputStream = resolver.openInputStream(uri);
            byte[] bytes;
            bytes = new byte[268435455];
            inputStream.read(bytes);
            b64 = Base64.encodeToString(bytes,Base64.DEFAULT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return b64 ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                imgB64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                Log.d("check", "onActivityResult: "+imgB64);
                //imgB64Send = imgB64.replaceAll("\\+", "%2B");
                decode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*File file = new File(uri. getPath());
            Log.d("Path", "Path is: "+file);
            InputStream inputStream = null; // You can get an inputStream using any I/O API
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            bytes = output.toByteArray();
            imgB64 = Base64.encodeToString(bytes, Base64.DEFAULT);*/
            //SECOND !!!!!!!!!!!!!!!!!!!!!!
            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                imgB64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("B64", imgB64);
                clipboard.setPrimaryClip(clip);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Log.d("uri is", "onActivityResult: " + uri);
            pfpImg.setImageURI(uri);
            //String imgB64 = fileUriToBase64(uri,getContentResolver());
            //Log.d("pls work", "onActivityResult: "+imgB64);
            DaoMenbre.getInstance().postPhoto(imgB64);
        }else{

        }
    }

    private void decode() {
        byte[] bytes = Base64.decode(imgB64,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivBaldMan.setImageBitmap(bitmap);
    }
}