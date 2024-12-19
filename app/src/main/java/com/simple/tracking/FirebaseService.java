package com.simple.tracking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.simple.tracking.admin.activity.delivery.AdminCreateDeliveryRecipientActivity;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryHistoryLocation;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("Firebase Message", Objects.requireNonNull(Objects.requireNonNull(message.getNotification()).getBody()));

        sendNotification(Objects.requireNonNull(message.getNotification()).getTitle(), Objects.requireNonNull(message.getNotification()).getBody());

        getCurrentLocation(Objects.requireNonNull(message.getNotification()).getBody());
    }

    private void sendNotification(String title, String messageBody) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CharSequence channelName = "Default";
            String channelId = "default_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void updateLocation(String deliveryNumber, double latitude, double longitude) {
        // Buat objek DeliveryHistoryLocation
        DeliveryHistoryLocation deliveryHistoryLocation = new DeliveryHistoryLocation();
        deliveryHistoryLocation.setDeliveryNumber(deliveryNumber);
        deliveryHistoryLocation.setLatitude(String.valueOf(latitude));
        deliveryHistoryLocation.setLongitude(String.valueOf(longitude));

        // Kirim permintaan ke API
        Call<BaseResponse<Void>> call = DeliveryAPIConfiguration.getInstance().updateLocation(deliveryHistoryLocation);
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Void>> call, @NonNull Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Void> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        Log.d("Update Location", "Location updated successfully.");
                    } else {
                        Log.e("Update Location", "Failed to update location: " + (baseResponse != null ? baseResponse.getMessage() : "Unknown error"));
                    }
                } else {
                    Log.e("Update Location", "Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                Log.e("Update Location", "Request failed: " + t.getMessage());
            }
        });
    }

    private void getCurrentLocation(String deliveryNumber) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        Log.d("Location Update", "Latitude: " + latitude + ", Longitude: " + longitude);

                        // Kirim lokasi ke fungsi updateLocation
                        updateLocation(deliveryNumber, latitude, longitude);
                    } else {
                        Log.e("Location Update", "Failed to get location.");
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Location Permission", "Location permission not granted: " + e.getMessage());
        }
    }
}
