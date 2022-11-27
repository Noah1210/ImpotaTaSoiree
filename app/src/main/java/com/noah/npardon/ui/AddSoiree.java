package com.noah.npardon.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.npardon.R;
import com.noah.npardon.beans.Soiree;
import com.noah.npardon.daos.DaoSoiree;
import com.noah.npardon.daos.DelegateAsyncTask;

import java.util.Calendar;
import java.util.Locale;

public class AddSoiree extends Activity {

    private TextView inputLib, inputDesc, inputDate, inputTime, inputAddress;
    private DatePickerDialog datePicker;
    private int hour, min ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_soiree);

        inputLib = findViewById(R.id.txLib);
        inputDesc = findViewById(R.id.txDescription);
        inputDate = findViewById(R.id.txDate);
        inputTime = findViewById(R.id.txTime);
        inputAddress = findViewById(R.id.txLoca);

        ((Button) findViewById(R.id.bAdd)).setOnClickListener(v -> {
            OnClickAdd();
        });

        ((Button) findViewById(R.id.bAnnuler)).setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            setResult(6, returnIntent);
            finish();
        });

        inputDate.setOnClickListener(v -> {
            showDatePickerDialog(v);
        });

        inputTime.setOnClickListener(v -> {
            showTimePickerDialog(v);
        });

    }

    private void showTimePickerDialog(View v) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
                hour = selectedHour;
                min = selectedMin;
                inputTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, 00));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, min, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i + "-" + i1 + "-" + i2;
                inputDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, dateSetListener, year, month, day);
        datePicker.show();
    }

    private void OnClickAdd() {
        String lib = inputLib.getText().toString();
        String desc = inputDesc.getText().toString();
        String date = inputDate.getText().toString();
        String time = inputTime.getText().toString();
        String address = inputAddress.getText().toString();
        Soiree so = new Soiree("",lib, desc, date, time, address, Connexion.menbreConnecte.getLogin());
        Log.d("Membre", "OnClickAdd: " + Connexion.menbreConnecte.getLogin());
        if (lib.isEmpty()) {
            showError(inputLib, "Please enter a party name");
        } else if (desc.isEmpty()) {
            showError(inputDesc, "Please enter a description");
        } else if (date.isEmpty()) {
            showError(inputDate, "Please enter a starting date");
        } else if (time.isEmpty()) {
            showError(inputTime, "Please enter a starting time");
        } else if (address.isEmpty()) {
            showError(inputAddress, "Please enter an address");
        } else {
            DaoSoiree.getInstance().addSoiree(so, new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {
                    boolean res = (boolean) result;
                    if (res == true) {
                        Toast.makeText(getApplicationContext(), "Votre soirée " + so.getLibelleCourt()+" a été ajouter", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("so",so);
                        setResult(3,returnIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Votre soirée n'a pas été ajouter", Toast.LENGTH_SHORT).show();
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