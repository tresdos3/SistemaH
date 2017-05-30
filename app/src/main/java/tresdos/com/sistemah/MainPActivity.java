package tresdos.com.sistemah;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdInternalReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MainPActivity extends AppCompatActivity {

    boolean estadoButtom = false;
    private DatabaseReference mDatabase;
    private TextView txtID, txtEmail, txtNombre;
    private Button btnLogout, btnAlerta;
    SessionManager sessionManager;
    //    solo para padres resgitro de tokens
    public static final String POST_TOKENP = "http://138.197.27.208/tokenPadre/";
    private String TokenP;
    public static final String KEY_TOKENP = "remember_token";
    //    solo para hijos resgitro de tokens
    public static final String POST_TOKENH = "http://138.197.27.208/hijosAgregar";

//    varibles de hashmap
    private String nombre, val, latitud, longitud;
    MyFirebaseInstanceIDService myFirebaseInstanceIDService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);
        txtID = (TextView) findViewById(R.id.txtID);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnAlerta = (Button) findViewById(R.id.btn_alerta);
        sessionManager = new SessionManager(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("mapas").child("3").child("hijo1");
//        verificando sessiones
        if (sessionManager.ObservarLogin())
            finish();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map)dataSnapshot.getValue();
                nombre = map.get("fullName");
                val = map.get("estado");
                latitud = map.get("latitud");
                longitud = map.get("longitud");
                //        verificar estados de alerta

                    if (!val.equals("true")){
            //            btnAlerta.setBackgroundColor(getResources().getColor(android.R.color.white));
                        btnAlerta.setBackgroundResource(R.drawable.circle_blank);
                        estadoButtom = false;
                    }
                    else{
                        btnAlerta.setText("Alerta");
                        btnAlerta.setBackgroundResource(R.drawable.circle_buttom);
                        estadoButtom = true;
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        startService(FirebaseInstanceService.class);
        HashMap<String, String> usuario = sessionManager.GetDetalles();
        String name = usuario.get(SessionManager.KEY_NAME);
        String email = usuario.get(SessionManager.KEY_EMAIL);
        String id = usuario.get(SessionManager.KEY_ID);

        String ids = id;
        txtID.setText("ID: "+ id);
        txtNombre.setText("Nombre: "+ name);
        txtEmail.setText("Email: "+ email);


        String TokenPadre = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyToken", TokenPadre);
        GuardarPadre(TokenPadre, id);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.CerrarSesion();
            }
        });
        btnAlerta.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (estadoButtom){
                    Toast.makeText(getApplicationContext(), "Existe una alerta", Toast.LENGTH_LONG).show();
                    Intent Mapintent = new Intent(getApplicationContext(), MapsHijoActivity.class);
                    Mapintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Mapintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Mapintent.putExtra("Longitud", longitud);
                    Mapintent.putExtra("Latitud", latitud);
                    startActivity(Mapintent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Existen Alertas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void GuardarPadre(String Token, String id) {
        TokenP = Token.trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_TOKENP+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_TOKENP,TokenP);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
