package com.noah.npardon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.npardon.R;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.beans.Soiree;
import com.noah.npardon.daos.DaoSoiree;
import com.noah.npardon.daos.DelegateAsyncTask;

import java.util.List;

public class ToutesSoirees extends Activity {
    private ArrayAdapter<Soiree> soireeArrayAdapter;
    private List<Soiree> soirees;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soiree);

        soirees = DaoSoiree.getInstance().getLocalSoirees();
        soireeArrayAdapter = new ArrayAdapter<Soiree>(this, android.R.layout.simple_list_item_1, soirees);
        lv = (ListView) findViewById(R.id.lvSoiree);
        lv.setAdapter(soireeArrayAdapter);

        ((Button) findViewById(R.id.bAdd)).setOnClickListener(v -> {
            OnClickLaunchAdd();
        });

        ((Button) findViewById(R.id.bMap)).setOnClickListener(v -> {
            OnClickLaunchMap();
        });

        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Soiree so = (Soiree) adapterView.getAdapter().getItem(i);
            clickLV(view, so, i);
        });

        getSoiree();

        Menbre me = (Menbre) this.getIntent().getSerializableExtra("me");
        ((TextView)findViewById(R.id.tvMyAcc)).setText(me.getPrenom());
        ((ImageView)findViewById(R.id.ivAcc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToutesSoirees.this, Account.class));
            }
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.lay2);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void refreshData() {
        getSoiree();
    }

    private void clickLV(View view, Soiree so, int i) {
        Intent intent = new Intent(getApplicationContext(), InfoSoiree.class);
        intent.putExtra("index", i);
        intent.putExtra("so", so);
        startActivityForResult(intent, 2);
    }

    private void OnClickLaunchAdd() {
        Intent intent = new Intent(getApplicationContext(), AddSoiree.class);
        startActivityForResult(intent, 2);
    }

    private void OnClickLaunchMap() {
        Intent intent = new Intent(getApplicationContext(), ToutesSoireeMap.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == 3){
                Soiree so = (Soiree) data.getSerializableExtra("so");
                soirees.add(so);
                getSoiree();
            }else if (resultCode == 4){
                int i = data.getIntExtra("index", 0);
                soirees.remove(i);
                getSoiree();
            }else{
            }
        }
    }
    private void getSoiree(){
        DaoSoiree.getInstance().getSoirees(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                soireeArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}