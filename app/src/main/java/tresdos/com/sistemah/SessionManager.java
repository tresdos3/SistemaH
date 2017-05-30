package tresdos.com.sistemah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Tresdos on 21/02/2017.
 */

public class SessionManager {
    //    compartir las referecias de la session
    SharedPreferences pref;
    //    Edita las refereciastresd
    SharedPreferences.Editor editor;
    //    contexto
    Context _context;
    //    compartir modo de preferencia
    int PRIVATE_MODE = 0;
    //    Nombre de archivo
    private static final String PREFER_NAME = "AndroidPrefer";
    //    todas los datos
    private static final String IS_USER_LOGIN = "EstaEnSession";
    private static final String IS_USER_LOGIN_HIJO = "EstaEnSessionHijo";
    //    user name or email
    public  static final String KEY_ID = "id";
    public  static final String KEY_EMAIL = "email";
    public  static final String KEY_NAME = "nameP";
    public  static final String KEY_TOKEN = "remember_token";
    // variables para session del hijo
    public static final String KEY_ID_HIJO = "idHijo";
    public static final String KEY_NAME_HIJO = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ESTADO = "estado";
    public static final String KEY_ID_USER = "iduser";
    public static final String KEY_IMEI = "imai";
//    verificar activitys padre o hijos
    public static final String KEY_TIPO = "Tipo";


//    coordenadas para apps y sessiones de notificaciones
    public static final String KEY_LATITUD = "latitud";
    public static final String KEY_LONGITUD = "logintud";
    public static final String KEY_ACTIVADO = "EstaActivo";




    //    contructor de la clase
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public boolean EstaEnSession(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean ObservarLogin(){
        if (!this.EstaEnSession()){
//            si el usuario no inicio session se le redigira a la pantalla de login
            Intent intent = new Intent(_context, MainActivity.class);
//            cierro todas las actividades
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            inicio la activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
            return true;
        }
        return false;
    }

//    crecibir datos para notificaciones

    //    crear session de login;
    public void CrearUsuarioSession(String id, String name, String email, String token){
//        System.out.println(id + name + email);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }
    //    guardando datos de la session
    public HashMap<String, String> GetDetalles(){
        HashMap<String, String> usuario = new HashMap<>();
        usuario.put(KEY_ID, pref.getString(KEY_ID, null));
        usuario.put(KEY_NAME, pref.getString(KEY_NAME, null));
        usuario.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        usuario.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        return usuario;
    }
    public void CerrarSesion(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }
    //    crear session para hijo;
    public void CrearHijoSession(String id, String name, String imei, String image, String estado){
        editor.putBoolean(IS_USER_LOGIN_HIJO, true);
        editor.putString(KEY_ID_HIJO, id);
        editor.putString(KEY_NAME_HIJO, name);
        editor.putString(KEY_IMEI, imei);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_ESTADO, estado);
        editor.commit();
    }
    //    guardando datos de la session
    public HashMap<String, String> GetDetallesHijos(){
        HashMap<String, String> usuario = new HashMap<>();
        usuario.put(KEY_ID_HIJO, pref.getString(KEY_ID_HIJO, null));
        usuario.put(KEY_NAME_HIJO, pref.getString(KEY_NAME_HIJO, null));
        usuario.put(KEY_IMEI, pref.getString(KEY_IMEI, null));
        usuario.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        usuario.put(KEY_ESTADO, pref.getString(KEY_ESTADO, null));
        usuario.put(KEY_ID_USER, pref.getString(KEY_ID_USER, null));
        return usuario;
    }
    public void TipoDeSesion(String tipo){
        editor.putString(KEY_TIPO, tipo);
        editor.commit();
    }
    public HashMap<String, String> GetTipo(){
        HashMap<String, String> tipo = new HashMap<>();
        tipo.put(KEY_TIPO, pref.getString(KEY_TIPO, null));
        return tipo;
    }
}
