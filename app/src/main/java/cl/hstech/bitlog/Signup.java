package cl.hstech.bitlog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import cl.hstech.bitlog.R;

public class Signup extends AppCompatActivity {

    Button b1, b2;
    EditText ed1, ed2,ed3;

    TextView tx1;
    int counter = 3;

        private FirebaseAuth mAuth;
        private FirebaseUser mFirebaseUser;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

            mAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mAuth.getCurrentUser();

        b1 = (Button) findViewById(R.id.button);
        ed1 = (EditText) findViewById(R.id.user_name);
        ed2 = (EditText) findViewById(R.id.pass1);
            ed3 = (EditText) findViewById(R.id.pass2);

        b2 = (Button) findViewById(R.id.button2);
        tx1 = (TextView) findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(v -> {
            if (ed1.getText().toString().equals("admin") &&                     ed2.getText().toString().equals("admin")) {

                firebaseRegistration(ed1.getText().toString(),ed2.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

                tx1.setVisibility(View.VISIBLE);
                tx1.setBackgroundColor(Color.RED);
                counter--;
                tx1.setText(Integer.toString(counter));

                if (counter == 0) {
                    b1.setEnabled(false);
                }
            }
        });

        b2.setOnClickListener(v -> finish());
    }

        private void firebaseRegistration(String user, String pass) {

            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    mFirebaseUser = mAuth.getCurrentUser();
                    Toast.makeText(this, "Se creo la cuenta", Toast.LENGTH_SHORT).show();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user).build();

                    mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registrado correctamente", Toast.LENGTH_SHORT).show();
                            //          registrarToken(mFirebaseUser);
                            //           verificarEmail(mFirebaseUser);
                            finish();
                        }
                    });

                    mFirebaseUser.sendEmailVerification();

                }
            });
        }

}
