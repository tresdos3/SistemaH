package tresdos.com.sistemah;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.R.attr.customTokens;
import static android.R.attr.name;


public class AlertaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
//    ServicioNotificacion notifica;
    private TextView txtLong, txtLat;
    private Button btnNueva;
    private Context context;
    private LocationManager locationManager ;
    private boolean GpsStatus ;
    private Intent intent1;
    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private DatabaseReference mDatabase;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList<String> permisosToRequest;
    private ArrayList<String> permisosRejected = new ArrayList<>();
    private ArrayList<String> permisos = new ArrayList<>();
    public static final String UrlNoti = "http://138.197.27.208/hijoNotificacion/";


    SessionManager sessionManager;
    private String TokenPadre = "";

    private final static int ALL_PERMISSIONS_RESULT = 101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerta);

        sessionManager = new SessionManager(getApplicationContext());
//        labels latitud longitud
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLong = (TextView) findViewById(R.id.txtLong);
//        buttom pedir nuevas coordenadas
        btnNueva = (Button) findViewById(R.id.btnNuevo);
        context = getApplicationContext();


        HashMap<String, String> usuario = sessionManager.GetDetalles();
        TokenPadre = usuario.get(SessionManager.KEY_TOKEN);


        permisos.add(ACCESS_FINE_LOCATION);
        permisos.add(ACCESS_COARSE_LOCATION);

        CheckGpsStatus();
         btnNueva.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 startActivity(intent1);
             }
         });
        permisosToRequest = findUnAskedPermissions(permisos);
//        Verificar los permisos en las sdk de android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permisosToRequest.size() > 0)
                requestPermissions(permisosToRequest.toArray(new String[permisosToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!TienePermisos(perm)) {
                result.add(perm);
            }
        }
        return result;
    }
    private boolean TienePermisos(String permission) {
        if (EsLolipop()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean EsLolipop() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckGpsStatus();
        if (!checkPlayServices()) {
            Toast.makeText(getApplicationContext(), "Falta Google Play Service.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLocation!=null)
        {
            txtLong.setText("Longitude : "+mLocation.getLongitude());
            txtLat.setText("Latitude : "+mLocation.getLatitude());
            EnviarNot(TokenPadre);
            mDatabase = FirebaseDatabase.getInstance().getReference("mapas");
            FirebaseButtonPanic person = new FirebaseButtonPanic("Luis Juarez ",String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), "true");
            mDatabase.child("3").child("hijo1").setValue(person);

        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
            txtLong.setText("Longitude : "+mLocation.getLongitude());
            txtLat.setText("Latitude : "+mLocation.getLatitude());
        EnviarNot(TokenPadre);
            mDatabase = FirebaseDatabase.getInstance().getReference("mapas");
            FirebaseButtonPanic person = new FirebaseButtonPanic("Luis Juarez ",String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), "true");
            mDatabase.child("3").child("hijo1").setValue(person);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permisosToRequest) {
                    if (!TienePermisos(perms)) {
                        permisosRejected.add(perms);
                    }
                }

                if (permisosRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permisosRejected.get(0))) {
                            showMessageOKCancel("Permiso Necesario para la aplicacion",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permisosRejected.toArray(new String[permisosRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AlertaActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(GpsStatus == true)
        {
            Toast.makeText(getApplicationContext(), "Location Services Is Enabled", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Location Services Is Disabled", Toast.LENGTH_LONG).show();
        }

    }


    public void EnviarNot(String TokenPadre){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlNoti + TokenPadre,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al enviar notificacion", Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

    }

}
