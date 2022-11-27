package com.noah.npardon.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.daos.DaoMenbre;
import com.noah.npardon.daos.DelegateAsyncTask;

import java.util.Calendar;

public class Inscription extends Activity {

    private TextView inputLogin, inputPassword, inputVerifPassword, inputLastName, inputFirstName, inputDDN, inputMail;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        inputLogin = findViewById(R.id.txLogin);
        inputPassword = findViewById(R.id.txPassword);
        inputVerifPassword = findViewById(R.id.txVerifPassword);
        inputLastName = findViewById(R.id.txLastName);
        inputFirstName = findViewById(R.id.txFirstName);
        inputDDN = findViewById(R.id.txDDN);
        inputMail = findViewById(R.id.txMail);

        ((Button) findViewById(R.id.bInscription)).setOnClickListener(v -> {
            OnClickConnexion();
        });

        ((Button) findViewById(R.id.bAnnuler)).setOnClickListener(v -> {
            finish();
        });

        inputDDN.setOnClickListener(v -> {
            showDatePickerDialog(v);
        });
    }

    private void showDatePickerDialog(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i+"-"+i1+"-"+i2;
                inputDDN.setText(date);
                //SimpleDateFormat
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, dateSetListener, year, month, day);
        datePicker.show();
    }

    private void OnClickConnexion() {
        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();
        String veriPassword = inputVerifPassword.getText().toString();
        String lastName = inputLastName.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String ddn = inputDDN.getText().toString();
        String mail = inputMail.getText().toString();
        Menbre me = new Menbre(login, lastName, firstName, ddn, mail, password);
        if (login.isEmpty()) {
            showError(inputLogin, "Please enter a login");
        } else if (password.isEmpty()) {
            showError(inputPassword, "Please enter a password");
        } else if (veriPassword.isEmpty()) {
            showError(inputVerifPassword, "Please enter a second password");
        } else if (!veriPassword.equals(password)) {
            showError(inputVerifPassword, "Passwords do not match");
        } else if (lastName.isEmpty()) {
            showError(inputLastName, "Please enter a last name");
        } else if (firstName.isEmpty()) {
            showError(inputFirstName, "Please enter a first name");
        } else if (ddn.isEmpty()) {
            showError(inputDDN, "Please enter a birth date");
        } else if (mail.isEmpty()) {
            showError(inputMail, "Please enter a mail");
        } else {
            DaoMenbre.getInstance().getInscription(me, new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {
                    boolean res = (boolean) result;
                    if (res == true) {
                        Toast.makeText(getApplicationContext(), "Bonjour "+me.getPrenom()+". Vous aller recevoir un mail de confirmation ", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("me", me);
                        /*TODO  arreter la connerie*/
                        setResult(3,returnIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas pu vous inscrire", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void showError(TextView input, String s) {
        input.setError(s);
        input.requestFocus();
    }


}