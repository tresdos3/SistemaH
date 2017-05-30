package tresdos.com.sistemah;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView lbId,lbnombre,lbEmail,lbNameHijo, lbImagen,lbEstado,lbIdHijo;
    private Button btnLogoutl, btnPermisos;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());
        lbId = (TextView) findViewById(R.id.txtid);
        lbnombre = (TextView) findViewById(R.id.txtname);
        lbEmail = (TextView) findViewById(R.id.txtemail);
        lbNameHijo = (TextView) findViewById(R.id.txt_nameHijo);
        lbImagen = (TextView) findViewById(R.id.txtimagen);
        lbIdHijo = (TextView) findViewById(R.id.txt_idHijo);
        lbEstado = (TextView) findViewById(R.id.txt_estado);
        btnLogoutl = (Button) findViewById(R.id.btnLogout);
        btnPermisos = (Button) findViewById(R.id.permisos);

        Toast.makeText(getApplicationContext(),"User Login Status "+ sessionManager.EstaEnSession(),Toast.LENGTH_LONG).show();

//        Obtenenos los datos de la clase SessionManager
        if (sessionManager.ObservarLogin())
            finish();

        if(SolicitarPermisos())
            HabilitarButton();


        HashMap<String, String> usuario = sessionManager.GetDetalles();
        String name = usuario.get(SessionManager.KEY_NAME);
        String email = usuario.get(SessionManager.KEY_EMAIL);
        String id = usuario.get(SessionManager.KEY_ID);

        HashMap<String, String> Hijo = sessionManager.GetDetallesHijos();
        String nameHijo = Hijo.get(SessionManager.KEY_NAME_HIJO);
        String imagen = Hijo.get(SessionManager.KEY_IMAGE);
        String estado = Hijo.get(SessionManager.KEY_ESTADO);
        String IdHijo = Hijo.get(SessionManager.KEY_ID_HIJO);
//        if(nameHijo == null){
////            Toast.makeText(getApplicationContext(), "Variable vacia Revisar",Toast.LENGTH_SHORT).show();
//            Intent padreActivity = new Intent(getApplicationContext(), MapsHijoActivity.class);
//            padreActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            padreActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(padreActivity);
//            finish();
//            onBackPressed();
//        }
//        mostrar los datos en el activity
        lbId.setText("Id Padre: "+ id);
        lbnombre.setText("Nombre Padre: " + name);
        lbEmail.setText("Email Padre: "+ email);
        lbNameHijo.setText("Nombre Hijo: "+ nameHijo);
        lbImagen.setText("Imagen: " + imagen);
        lbEstado.setText("Estado Hijo: "+ estado);
        lbIdHijo.setText("Id Hijo: "+ IdHijo);

        btnLogoutl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.CerrarSesion();
            }
        });
    }
//    permisos ar los buttons
    private void HabilitarButton() {
        btnPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ServicioNotificacion.class);
                startService(i);
            }
        });
    }
    //    solicitar permisos al empezar la session
    public boolean SolicitarPermisos(){
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},10);
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager
                    .PERMISSION_GRANTED) {
                HabilitarButton();
            }
            else{
                SolicitarPermisos();
            }
        }
    }
}
