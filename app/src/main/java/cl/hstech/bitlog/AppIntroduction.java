package cl.hstech.bitlog;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import static cl.hstech.bitlog.MainActivity.COMPLETED_ONBOARDING_PREF_NAME;

public class AppIntroduction extends AppCompatActivity {

    ViewPager viewPager;
    SlideAdapter slideAdapter;

    //sorry for this intro..I know it's lame :/
    public static String[] titlesIntro = {"Acepta el permiso requerido", "escanea el codigo", "obten el historial"};
    public static String[] pictures = {"https://hstech.cl/BitLogIntro/Intro%20(1).jpg", "https://hstech.cl/BitLogIntro/Intro%20(2).jpg", "https://hstech.cl/BitLogIntro/Intro%20(3).jpg"};

       @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_activity);

        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true);   //con false se inicia una sola vez

        sharedPreferencesEditor.apply();

        viewPager = findViewById(R.id.pager);

        slideAdapter = new SlideAdapter(this);

        viewPager.setAdapter(slideAdapter);

        viewPager.addOnPageChangeListener(viewListener);

    } //oncreate

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        private static final String TAG = "debug";

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "onPageScrolled: ");
        }

        @Override
        public void onPageSelected(int position) {

            //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  //animacion para desvanecerse
            Log.d(TAG, "page position: " + position);
            if (position == pictures.length - 1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            Log.d(TAG, "onPageScrollStateChanged: ");
        }
    };


}



