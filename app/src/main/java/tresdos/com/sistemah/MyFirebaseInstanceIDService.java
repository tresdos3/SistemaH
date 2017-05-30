package tresdos.com.sistemah;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tresdos on 16/03/2017.
 */

public class MyFirebaseInstanceIDService extends com.google.firebase.iid.FirebaseInstanceIdService{

    public void onTokenRefresh() {
//        aki se genera el token para cada dispositivo
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        return  refreshedToken;
    }
}
