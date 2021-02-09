package com.example.speedalertingdevice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class Geofencehelper extends ContextWrapper {

    private static final String TAG = "Geofencehelper";
    PendingIntent pendingIntent;

    public Geofencehelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence){

        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();


    }

    public Geofence getGeofence(String ID, LatLng school, float radius, int transitionTypes){

        return new Geofence.Builder()
                .setCircularRegion(school.latitude,school.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(100)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent(){

        if(pendingIntent != null){
            return pendingIntent;
        }
        Intent intent = new Intent(this, MapsActivity.GeofenceSampleReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public String getErrorString(Exception e){

        if (e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()){

                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GeoFence_not_available";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GeoFence_too_many";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GeoFence_too_many_pending";
            }
        }

        return e.getLocalizedMessage();
    }
}
