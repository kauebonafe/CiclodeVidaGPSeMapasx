package br.usjt.ciclodevidagpsemapasx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GPS_PERMISSION = 1001;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationTextView = findViewById(R.id.locationTextView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(
                        String.
                                format(
                                        "geo:%f, %f?q=restaurantes",
                                        latitude,
                                        longitude));
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitude = lat;
                longitude = lon;
                String s = String.format("Lat: %f, Long: %f", lat, lon);
                locationTextView.setText(s);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.
                checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            locationManager.
                    requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener);
        }else{
            ActivityCompat.requestPermissions(
                    this,
                    new String []{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_GPS_PERMISSION
            );
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GPS_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){
                    locationManager.
                            requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    0,
                                    0,
                                    locationListener);
                }

            }
            else{
                Toast.makeText(this,
                        getString (R.string.sem_gps_nao_rola), Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }
}
