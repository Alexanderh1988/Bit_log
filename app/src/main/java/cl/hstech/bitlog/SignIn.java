package cl.hstech.bitlog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    Button b1, b2;
    EditText ed1, ed2;

    TextView tx1;
    int counter = 3;

    TextView SignupPlease;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        b1 = (Button) findViewById(R.id.button);
        ed1 = (EditText) findViewById(R.id.user_name);
        ed2 = (EditText) findViewById(R.id.password);

        b2 = (Button) findViewById(R.id.singin_cancel);
        tx1 = (TextView) findViewById(R.id.textView3);
        SignupPlease = findViewById(R.id.textView4);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(v -> {
            if (ed1.getText().toString().equals("admin") &&
                    ed2.getText().toString().equals("admin")) {
                Toast.makeText(getApplicationContext(),
                        "Redirecting...", Toast.LENGTH_SHORT).show();
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

        SignupPlease.setOnClickListener(v -> {
           startActivity(new Intent(this, Signup.class));
        });
    }
}
