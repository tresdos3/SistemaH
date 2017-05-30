package tresdos.com.sistemah;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    //    iniciar session datps
    SessionManager sessionManager;

    public static final String LOGIN_URL = "http://138.197.27.208/auth_iniciar";
    public static final String KEY_USERNAME = "email";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String email;
    private String password;

    private String IdUsuario = "";
    private String NameUsuario = "";
    private String emailUsuario = "";
    private String tokenUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = (ImageView) findViewById(R.id.ImgLogo);
        imageView.setImageBitmap(RecuperarImagen("logoTarija.png"));
        Resources res = getResources();

        //      ========== Login Servicios Rest ==========
        sessionManager = new SessionManager(getApplicationContext());
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        Toast.makeText(getApplicationContext(),"User Login Status "+ sessionManager.EstaEnSession(),Toast.LENGTH_LONG).show();

//        sessionManager.ObservarLogin();
        if (sessionManager.EstaEnSession()){
            openPassword();
        }
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
    }
    //    ==========funcion recuperar imagen==========
    private Bitmap RecuperarImagen(String fileName){

        AssetManager assetmanager = getAssets();
        InputStream is = null;
        try{

            is = assetmanager.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }
    //   ========== Funcion Login ==========
    private void userLogin (){
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals(" ")){
                            Toast.makeText(getApplicationContext(), "ERROR",Toast.LENGTH_SHORT).show();
                        }else{
                            JsonParser(response.toString());
//                            System.out.println(IdUsuario + NameUsuario + emailUsuario);
                            if (IdUsuario != null && NameUsuario != null && emailUsuario != null){
                                System.out.println("Existen Datos");
                                sessionManager.CrearUsuarioSession(IdUsuario,NameUsuario,emailUsuario, tokenUsuario);
                                openProfile();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,email);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void JsonParser(String response){
//        sdxxxxxxxxxxxxxxxxxxx
        JSONObject jsonObject = null;
        if (response != null){
            try{
                jsonObject = new JSONObject(response);
                JSONObject Datos = jsonObject.getJSONObject("user");
                for (int i=0; i < Datos.length(); i++){
                    IdUsuario = Datos.getString("id");
                    NameUsuario = Datos.getString("name");
                    emailUsuario = Datos.getString("email");
                    tokenUsuario = Datos.getString("remember_token");
                }
//                }
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
//    INiciar el el perfil autmaticamente
    private void openProfile(){
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(KEY_USERNAME, email);tre
//        MainActivity.this.finish();
        startActivity(intent);
        finish();
    }
//    Iniciar en pedir password
    private void openPassword(){
        HashMap<String, String> tipo = sessionManager.GetTipo();
        String Tipo = tipo.get(SessionManager.KEY_TIPO);
//        Toast.makeText(LoginActivity.this,Tipo,Toast.LENGTH_LONG ).show();
        if (Tipo.equals("true")){
            Intent intent = new Intent(this, MainPActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(KEY_USERNAME, email);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(KEY_USERNAME, email);
            startActivity(intent);
            finish();
        }
//
    }
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseid", "Refreshed token: " + refreshedToken);
    }
    @Override
    public void onClick(View v) {
        userLogin();
    }
}
