package cl.hstech.bitlog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.ui.AppBarConfiguration;

//import com.daandtu.webscraper.WebScraper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import cl.hstech.bitlog.helpers.DatabaseQueries;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationMenuView.OnCreateContextMenuListener /*, OnNavigationItemSelectedListener*/ {

    private static final int REQUEST_CODE_PERMISSION = 123;
    private static final int REQUEST_CODE = 123;
    private static final String TAG = "debug";
    String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.VIBRATE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;

    static ArrayList<String> codes = new ArrayList<>();
    static ArrayList<String> codesVisited = new ArrayList<>();
    static ArrayList<String> picture = new ArrayList<>();

    MutableLiveData<Boolean> isLocationPermissionSetted;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    static DatabaseQueries mQuery;
    //variables de introduccion:
    static SharedPreferences sharedPreferences;
    public static String COMPLETED_ONBOARDING_PREF_NAME = "intro";
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CheckUpdates();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true);

        isLocationPermissionSetted = new MutableLiveData<>();
        isLocationPermissionSetted.setValue(false);

        if (!sharedPreferences.getBoolean(COMPLETED_ONBOARDING_PREF_NAME, false)) {
            startActivity(new Intent(this, AppIntroduction.class));
        } else {
            verifyRequiredPermissions();
        }

        setContentView(R.layout.activity_main);
        setTitle(getApplicationContext().getPackageName());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Bit Log");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_share).
                setDrawerLayout(drawer).build();

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();


        for (int i = 0; i < mPermission.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission[i]}, REQUEST_CODE_PERMISSION);
            }
        }
        //Database Initiation:
        mQuery = new DatabaseQueries();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            if (mQuery.getmFirebaseUser() == null) {
                Toast.makeText(this, "Para agregar tiene que identificarse", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignIn.class));
            } else
                startActivity(new Intent(this, ManualAdd.class));
        });

        surfaceView = findViewById(R.id.camerapreview);
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

        mQuery.getmEquipmentIdRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (!codes.contains(ds.getValue().toString())) ;
                    codes.add(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

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

                    textView.post(() -> {

                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(400);
                        textView.setText(qrCodes.valueAt(0).displayValue);
                        if (IsCodeRecognized(qrCodes.valueAt(0).displayValue)) {
                            //  Toast.makeText(getApplicationContext(), "Se encontró codigo", Toast.LENGTH_SHORT).show();
                            surfaceView.setVisibility(surfaceView.INVISIBLE);
                            Intent IrAlDetalle = new Intent(MainActivity.this, Details.class).putExtra("Codigo", qrCodes.valueAt(0).displayValue).putExtra("foto", picture.get(codes.indexOf(qrCodes.valueAt(0).displayValue)));
                            startActivity(IrAlDetalle);
                            //  surfaceView.setVisibility(surfaceView.VISIBLE);
                        } else
                            Toast.makeText(getApplicationContext(), "No esta en nuestra base de datos", Toast.LENGTH_SHORT).show();
                        surfaceView.setVisibility(surfaceView.VISIBLE);

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

    private void CheckUpdates() {
        //check updates in playstore
    }

    private void verifyRequiredPermissions() {

        //  noPermission = findViewById(R.id.noPermissionDialog);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                Log.d(TAG, "Permision got it");
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
            }
        } else
            isLocationPermissionSetted.setValue(true);
    }

    protected void refreshSurface() {
        surfaceView.setVisibility(surfaceView.INVISIBLE);
        surfaceView.setVisibility(surfaceView.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        if (codes.contains(code)) {
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //  item.setChecked(true);
        int id = item.getItemId();
        //a futuro grabar ubicacion de maquina
//        if (id == R.id.nav_mapa) {
//
//            startActivity(new Intent(MainActivity.this, Mapa.class));
//            }


        if (id == R.id.intro) {

            sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true);

            startActivity(new Intent(this, AppIntroduction.class));

        }

        if (id == R.id.registrar) {

            startActivity(new Intent(MainActivity.this, SignIn.class));

        }

        if (id == R.id.rate_app) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + this.getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
            }
        }

        if (id == R.id.reciente) {
            if (mQuery.getmFirebaseUser() != null) {
                startActivity(new Intent(this, Recent.class));
            } else {
                startActivity(new Intent(this, SignIn.class));
            }
        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Compartir por"));

        } else if (id == R.id.contact) {

            String[] TO = {""};
            String[] CC = {""};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");


        } else if (id == R.id.qr_assign) {
            if (mQuery.getmFirebaseUser() != null) {
                startActivity(new Intent(this, Recent.class));
            } else {
                setTitle("Reasignar codigo");
            }
        } else if (id == R.id.map) {

            startActivity(new Intent(this, Mapa.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search) {
            startActivity(new Intent(MainActivity.this, Search.class));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public static ArrayList<String> getCodes() {
        return codesVisited;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
//    }
}