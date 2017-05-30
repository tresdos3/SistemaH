package tresdos.com.sistemah;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroHijoActivity extends AppCompatActivity {
//    private Spinner ListaSpiner;
    ArrayList<HashMap<String, String>> ListAHijos;
    MaterialSpinner ListaSpiner;
    ImageView imagenNino1, imagenNino0, imagenGirl0, imagenGirl1;
    EditText txrNombre;
    boolean nino1 = false, nino0 = false, girl0 = false, girl1 = false;
    Button BtnRegistrar, BtnSeleccionar;
    public static final String GET_HIJOS = "http://138.197.27.208/hijosFIltar/";
    public static final String POST_REGISTRO = "http://138.197.27.208/hijosAgregar";
    SessionManager sessionManager;
//    varibles registro
    private String name, image, imei, idusers, estado;
    private int idHijo;
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_IDUSERS = "idusers";
    public static final String KEY_ESTADO = "estado";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_hijo);

//        sesssion manan-==ger
        sessionManager = new SessionManager(getApplicationContext());
//        imagen select
        imagenNino0 = (ImageView) findViewById(R.id.ImageNino0);
        imagenNino1 = (ImageView) findViewById(R.id.ImageNino1);
        imagenGirl0 = (ImageView) findViewById(R.id.ImageGirl0);
        imagenGirl1 = (ImageView) findViewById(R.id.ImageGirl1);
        BtnRegistrar = (Button) findViewById(R.id.btnRegistro);
        BtnSeleccionar = (Button) findViewById(R.id.btnSeleccionar);
        txrNombre = (EditText) findViewById(R.id.txtNombre);
        ListAHijos = new ArrayList<>();
        obtenerHijos(3);
        ListaSpiner = (MaterialSpinner) findViewById(R.id.ListaHijos);
//        ListaSpiner.setAdapter();
        ListaSpiner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

//        imagenes click listener funtion
        imagenNino0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nino0 = true;
                nino1 = false;
                girl0 = false;
                girl1 = false;
//                Snackbar.make(view, "Clicked " + imagenNino0.getTag(), Snackbar.LENGTH_LONG).show();
                imagenNino0.setBackgroundResource(R.drawable.image_brder);
                imagenNino1.setBackgroundResource(R.drawable.image_no_border);
                imagenGirl0.setBackgroundResource(R.drawable.image_no_border);
                imagenGirl1.setBackgroundResource(R.drawable.image_no_border);
            }
        });
        imagenNino1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nino0 = false;
                nino1 = true;
                girl0 = false;
                girl1 = false;
//                Snackbar.make(view, "Clicked " + imagenNino1.getTag(), Snackbar.LENGTH_LONG).show();
                imagenNino1.setBackgroundResource(R.drawable.image_brder);
                imagenNino0.setBackgroundResource(R.drawable.image_no_border);
                imagenGirl0.setBackgroundResource(R.drawable.image_no_border);
                imagenGirl1.setBackgroundResource(R.drawable.image_no_border);
            }
        });
        imagenGirl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nino0 = false;
                nino1 = false;
                girl0 = true;
                girl1 = false;
//                Snackbar.make(view, "Clicked " + imagenNino1.getTag(), Snackbar.LENGTH_LONG).show();
                imagenGirl0.setBackgroundResource(R.drawable.image_brder);
                imagenGirl1.setBackgroundResource(R.drawable.image_no_border);
                imagenNino1.setBackgroundResource(R.drawable.image_no_border);
                imagenNino0.setBackgroundResource(R.drawable.image_no_border);
            }
        });
        imagenGirl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nino0 = false;
                nino1 = false;
                girl0 = false;
                girl1 = true;
//                Snackbar.make(view, "Clicked " + imagenNino1.getTag(), Snackbar.LENGTH_LONG).show();
                imagenGirl1.setBackgroundResource(R.drawable.image_brder);
                imagenGirl0.setBackgroundResource(R.drawable.image_no_border);
                imagenNino1.setBackgroundResource(R.drawable.image_no_border);
                imagenNino0.setBackgroundResource(R.drawable.image_no_border);
            }
        });
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Tag = "";
                String txtNombre = txrNombre.getText().toString();
                if (txtNombre.matches("")){
                    Snackbar.make(view, "Falta Nombre", Snackbar.LENGTH_LONG).show();
                }
                else{
                    if (nino1 == false && nino0 == false && girl1 == false && girl0 == false){
                        Snackbar.make(view, "Seleccione una imagen", Snackbar.LENGTH_LONG).show();
                    }
                    else{
//                        Snackbar.make(view, "Imagen Sessionanda", Snackbar.LENGTH_LONG).show();
                        if (nino0){
                            Tag = imagenNino0.getTag().toString();
                            Registro_hijo(txtNombre,Tag,"mmnmnmnm","3");
//                        Snackbar.make(view, Tag, Snackbar.LENGTH_LONG).show();
                        }
                        if (nino1){
                            Tag = imagenNino1.getTag().toString();
//                        Snackbar.make(view, Tag, Snackbar.LENGTH_LONG).show();
                        }
                        if (girl0){
                            Tag = imagenGirl0.getTag().toString();
//                        Snackbar.make(view, Tag, Snackbar.LENGTH_LONG).show();
                        }
                        if (girl1){
                            Tag = imagenGirl1.getTag().toString();
//                        Snackbar.make(view, Tag, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
//        btn para seleccionar hijo
        BtnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int DI = ListaSpiner.getSelectedIndex();
                String anan = ListAHijos.get(DI).toString();
                Snackbar.make(view, anan, Snackbar.LENGTH_LONG).show();
                DatosHijos(anan);
//                HashMap<String, String> usuario = sessionManager.GetDetalles();
//                String iduser = usuario.get(SessionManager.KEY_ID);
                sessionManager.CrearHijoSession(Integer.toString(idHijo),name, imei, image, estado);
                openProfile();
            }
        });
    }
//    registro de nuevos hijos
    private void Registro_hijo(String nombre, String imagen, String imeiT, String iduser){
        name = nombre.trim();
        image = imagen.trim();
        imei = imeiT.trim();
        idusers = iduser.trim();
        estado = "1".trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_REGISTRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(RegistroHijoActivity.this,"Imei Repetido",Toast.LENGTH_LONG ).show();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }
                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(error instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
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
                map.put(KEY_NAME,name);
                map.put(KEY_IMEI,imei);
                map.put(KEY_IMAGE,image);
                map.put(KEY_ESTADO,estado);
                map.put(KEY_IDUSERS,idusers);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //    Obtener todos los hijos segun hijos
    private void obtenerHijos(int id){
//        Toast.makeText(getApplicationContext(), "Correcto",Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_HIJOS+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals(" ")){
                            Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JsonParser(response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
//    Json Parser
    String[] Hello;
    private void JsonParser(String response){

        JSONObject jsonObject = null;
        if (response != null){
            try{
                jsonObject = new JSONObject(response);
                JSONArray Datos = jsonObject.getJSONArray("Lista");
                Hello = new String[Datos.length()];
                for (int i=0; i < Datos.length(); i++){
                    JSONObject Lista = Datos.getJSONObject(i);
                    Hello[i] = Lista.getString("name");

                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("id", Lista.getString("id"));
                    contact.put("name", Lista.getString("name"));
                    contact.put("imei", Lista.getString("imei"));
                    contact.put("estado", Lista.getString("estado"));
                    contact.put("idusers", Lista.getString("idusers"));
                    contact.put("image", Lista.getString("image"));
                    ListAHijos.add(contact);
                }
//                }
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "Error Aki",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
//        Hello[2] = "Seleccione Un Hijo";
//        String[] str={"Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow"};
           ListaSpiner.setItems(Hello);
//        ListaSpiner.setSelectedIndex(2);
//        Toast.makeText(getApplicationContext(), Hello[0],Toast.LENGTH_SHORT).show();
    }
//    obtner Datos de hijos
    private void DatosHijos(String JsonHIj) {
        String JsonHIjo = "{Hijo:["+JsonHIj+"]}";
        JSONObject jsonObject = null;
        if (JsonHIjo != null) {
            try {
                jsonObject = new JSONObject(JsonHIjo);
                JSONArray Datos = jsonObject.getJSONArray("Hijo");
                for (int i = 0; i < Datos.length(); i++) {
                    JSONObject Lista = Datos.getJSONObject(i);
                    name = Lista.getString("name");
                    imei = Lista.getString("imei");
                    image = Lista.getString("image");
                    idHijo = Lista.getInt("id");
                    estado = Lista.getString("estado");
                    idusers = Lista.getString("idusers");
                }
//                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error Aki", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void openProfile(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
