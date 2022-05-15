package cl.hstech.bitacora_online;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int REQUEST_CODE_PERMISSION = 123;
    private static final int REQUEST_CODE = 123;
    String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.VIBRATE, Manifest.permission.CAMERA};

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;

    ArrayList<String> codigos = new ArrayList<>();
    static ArrayList<String> foto = new ArrayList<>();
    //static Array[][] personal = new Array[5][1];
    static ArrayList<String> personal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      /* Intent intent = getIntent();
        finish();
        startActivity(intent);*/

        setContentView(R.layout.activity_main);
        setTitle("Bitacora Online");

        for (int i = 0; i < mPermission.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission[i]}, REQUEST_CODE_PERMISSION);
            }
        }

        //   startActivity(new Intent(MainActivity.this,AgregarItems.class));

        // SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        /*ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);*/
        //startActivity(new Intent(MainActivity.this, Detalles.class).putExtra("Codigo", "equipo1"));

        // .putExtra("foto",foto.get(Integer.parseInt(qrCodes.valueAt(0).displayValue)));
        /*
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        surfaceView = findViewById(R.id.camerapreview);

        /*  Button hola = findViewById(R.id.hola);
        hola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surfaceView.setVisibility(View.VISIBLE);
            }
        });*/
        /*   if (ActivityCompat.checkSelfPermission(this, CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            surfaceView.setVisibility(View.VISIBLE);
        }*/

        textView = findViewById(R.id.textView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(holder);
                        Log.d("debug", "Check permission: granted - start camera");
                    } else {
                        Log.d("debug", "Check permission: restricted - request");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }

        });

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Write a message to the database
       // FirebaseDatabase database = FirebaseDatabase.getInstance();
      //  DatabaseReference myRef = database.getReference();
        //bd: https://console.firebase.google.com/u/0/project/hstechinspection/database/hstechinspection/rules
        // Read from the database

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    Log.d("itera", String.valueOf(snapshot.getValue()));
//                    Log.d("itera1", snapshot.getKey());
//                    Log.d("itera2", String.valueOf(snapshot));
//                    Log.d("itera3", String.valueOf(snapshot.child("foto").getValue()));
//                    Log.d("itera4", String.valueOf(snapshot.child("Personal").getValue()));
//
//                    //codigos.add(snapshot.getKey());
//                    codigos.add(String.valueOf(snapshot.getKey()));
//                    foto.add(String.valueOf(snapshot.child("foto").getValue()));
//                    //personal.add(String.valueOf(snapshot.child("Personal").getValue()));
//             /*       personal[0][0] = String.valueOf(snapshot.child("Personal").getKey());
//                    personal[0][1] = String.valueOf(snapshot.child("Personal").getValue();*/
//                }
//
////                String value = dataSnapshot.getValue(String.class);
//                //        Log.d("basededatos", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("basededatos", "Failed to read value.", error.toException());
//            }
//        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(final Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {

                    //  barcodeDetector.release();
                    Log.d("detectado", qrCodes.valueAt(0).displayValue);

                    textView.post(new Runnable() {
                        @Override
                        public void run() {

                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(400);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            if (IsCodeRecognized(qrCodes.valueAt(0).displayValue)) {
                                //  Toast.makeText(getApplicationContext(), "Se encontró codigo", Toast.LENGTH_SHORT).show();
                                surfaceView.setVisibility(surfaceView.INVISIBLE);
                                Intent IrAlDetalle = new Intent(MainActivity.this, Detalles.class).putExtra("Codigo", qrCodes.valueAt(0).displayValue).putExtra("foto", foto.get(codigos.indexOf(qrCodes.valueAt(0).displayValue)));
                                startActivity(IrAlDetalle);
                                //  surfaceView.setVisibility(surfaceView.VISIBLE);
                            } else
                                Toast.makeText(getApplicationContext(), "No esta en nuestra base de datos", Toast.LENGTH_SHORT).show();
                            surfaceView.setVisibility(surfaceView.VISIBLE);

                        }
                    });
                } else

                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Enfocar a codigo QR");
                        }
                    });
            }
        });

    }   //on create

    protected void refreshSurface() {
        surfaceView.setVisibility(surfaceView.INVISIBLE);
        surfaceView.setVisibility(surfaceView.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(getApplicationContext(), "codigo1", Toast.LENGTH_SHORT).show();
                        refreshSurface();
                        return;
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(getApplicationContext(), "Favor cerrar y abrir aplicacion", Toast.LENGTH_SHORT).show();
                            refreshSurface();

                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            Toast.makeText(getApplicationContext(), "Tiene que aceptar el permiso para poder escanear", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        }
    }

    public Boolean IsCodeRecognized(String code) {

        if (codigos.contains(code)) {
            return true;
        } else
            return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}