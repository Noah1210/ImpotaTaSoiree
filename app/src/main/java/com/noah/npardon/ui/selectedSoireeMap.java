package com.noah.npardon.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.npardon.R;
import com.noah.npardon.beans.Soiree;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class selectedSoireeMap extends Activity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapSoiree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_soiree_map);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mapSoiree = (MapView) findViewById(R.id.map);
        mapSoiree.setTileSource(TileSourceFactory.MAPNIK);

        String[] tabPerm = new String[1];
        tabPerm[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        requestPermissionsIfNecessary(tabPerm);
        mapSoiree.setMultiTouchControls(true);
        Soiree so = (Soiree) getIntent().getSerializableExtra("so");
        GeoPoint p = makeMarkers(so);
        positionnerSurCentre(p);

        ((Button) findViewById(R.id.bRetour)).setOnClickListener(v -> {
            finish();
        });
        ((Button) findViewById(R.id.bNaviguer)).setOnClickListener(v -> {
            Uri location = Uri.parse("google.navigation:q="+so.getLatitude()+","+so.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            Log.d("TAG", "noIdea: ");
        });

    }

    private void positionnerSurCentre(GeoPoint p) {
        IMapController mapController = mapSoiree.getController();
        mapController.setZoom(14.5);
        mapController.setCenter(p);
    }

    private GeoPoint makeMarkers(Soiree so) {
        mapSoiree.getOverlays().clear();
        ArrayList<OverlayItem> items = new ArrayList<>();
        OverlayItem soirees;
        soirees = new OverlayItem(so.getLibelleCourt(), "Par " + so.getLogin(), new GeoPoint(so.getLatitude(), so.getLongitude()));
        soirees.setMarker(getResources().getDrawable(R.drawable.location));
        items.add(soirees);
        ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });
        ((ItemizedOverlayWithFocus<OverlayItem>) mOverlay).setFocusItemsOnTap(true);
        mapSoiree.getOverlays().add(mOverlay);
        return new GeoPoint(so.getLatitude(), so.getLongitude());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}