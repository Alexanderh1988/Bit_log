package cl.hstech.bitlog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import java.util.List;

public class getLocationData {

    private static LocationManager locationManager;

    @SuppressLint("MissingPermission")
    public double[] getCoordinates(Context mContext) {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        //  LocationManager lm = (LocationManager) getSystemService(getLocation.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location l = null;
        for (int i = providers.size() - 1; i >= 0; i--) {

            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }
        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            //   Log.d(TAG, "Latitude: " + gps[0]);
            gps[1] = l.getLongitude();
            //     Log.d(TAG, "Longitude: " + gps[1]);
        }
        if (gps[0] == 0.0 || gps[0] == 0.0) { //si esta en null
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        }
        return gps;

    }



}
