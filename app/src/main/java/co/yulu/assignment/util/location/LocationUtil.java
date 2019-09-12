package co.yulu.assignment.util.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationUtil implements LocationListener {

    private static final String TAG = "LocationUtil";

    public static final int REQUEST_LOCATION = 199;
    public double lat, lon;
    int LOCATION_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private long MAX_WAIT_TIME = -1;
    private long LOCATION_INTERVAL = 30 * 1000L;
    private long LOCATION_FASTEST_INTERVAL = 5 * 1000L;
    private long EXPIRATION_DURATION = -1;
    private boolean isLocationListenerRegistered = false;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public static boolean isGPSOn(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void setExpirationTime(long expirationDuration) {
        EXPIRATION_DURATION = expirationDuration;
    }

    private long getExpirationDuration() {
        return EXPIRATION_DURATION;
    }

    public void setMaxWaitTime(long maxWaitTime) {
        MAX_WAIT_TIME = maxWaitTime;
    }

    private long getMaxWaitTime() {
        return MAX_WAIT_TIME;
    }

    public void setLocationFastestInterval(long fastestInterval) {
        LOCATION_FASTEST_INTERVAL = fastestInterval;
    }

    public void setLocationInterval(long interval) {
        LOCATION_INTERVAL = interval;
    }

    public void setLocationAccuracy(int accuracy) {
        LOCATION_ACCURACY = accuracy;
    }

    public void switchOnGPSAndStartReceivingLocation(Activity activity) {
        if (!isGPSOn(activity)) {
            switchOnGPS(activity);
        } else {
            startReceivingLocation(activity);
        }
    }

    public static void switchOnGPS(final Activity activity) {
        Log.d(TAG, "switchOnGPS called");
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        // **************************
        builder.setAlwaysShow(true); // this is the key ingredient
        // **************************

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    Log.d(TAG, "exception in onComplete", ex);
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.d(TAG, "RESOLUTION_REQUIRED");
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException.
                                        startResolutionForResult(activity, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG, "onComplete Exception: ", e);
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.d(TAG, "SETTINGS_CHANGE_UNAVAILABLE");
                            break;
                    }
                }
            }
        });
    }

    // ----------------------------------------------------------------
    //                     RECEIVING LOCATION
    // -----------------------------------------------------------------
    public void startReceivingLocation(Context context) {
        initLocation(context);

        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(LOCATION_INTERVAL);
            mLocationRequest.setFastestInterval(LOCATION_FASTEST_INTERVAL);
            mLocationRequest.setPriority(LOCATION_ACCURACY);
            if (getMaxWaitTime() != -1) {
                mLocationRequest.setMaxWaitTime(getMaxWaitTime());
            }
        }

        if (!isLocationListenerRegistered) {
            isLocationListenerRegistered = true;
            try {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, null);
            } catch (SecurityException unlikely) {
                isLocationListenerRegistered = false;
            }
        }
    }

    private void initLocation(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationChanged(locationResult.getLastLocation());
            }
        };
    }


    @Override
    public void onLocationChanged(Location location) {
        //Activity or service will access the object lat , lon, timestamp
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    public void stopReceivingLocation() {
        if (mFusedLocationClient != null) {
            try {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                isLocationListenerRegistered = false;
            } catch (SecurityException unlikely) {
                isLocationListenerRegistered = true;
            }
        }
    }
}