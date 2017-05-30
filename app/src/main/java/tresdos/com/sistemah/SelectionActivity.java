package tresdos.com.sistemah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {
    private Button paraPadres, paraHijos;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        sessionManager = new SessionManager(getApplicationContext());
        paraPadres = (Button) findViewById(R.id.btnPadres);
        paraHijos = (Button) findViewById(R.id.btnHijos);
        paraPadres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sera true si la app es para padre
                sessionManager.TipoDeSesion("true");
                Intent padreActivity = new Intent(getApplicationContext(), MainPActivity.class);
                padreActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                padreActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(padreActivity);
                finish();
                onBackPressed();
            }
        });
        paraHijos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sera false si la app es para hijos
                sessionManager.TipoDeSesion("false");
                Intent HijoActivity = new Intent(getApplicationContext(), RegistroHijoActivity.class);
                HijoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HijoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(HijoActivity);
                finish();
            }
        });
    }
}
