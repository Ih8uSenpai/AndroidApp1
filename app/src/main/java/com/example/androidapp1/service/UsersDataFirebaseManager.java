package com.example.androidapp1.service;

import androidx.annotation.NonNull;

import com.example.androidapp1.models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UsersDataFirebaseManager {
    private DatabaseReference usersDataRef;

    public UsersDataFirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersDataRef = database.getReference("UsersData");
    }

    public DatabaseReference getUsersDataRef() {
        return usersDataRef;
    }

    public void addUserData(final String userId, final UserData userData) {
        DatabaseReference userRef = usersDataRef.child(userId);
        userRef.setValue(userData);
    }

    public void updateUserData(final String userId, final UserData userData) {
        DatabaseReference userRef = usersDataRef.child(userId);
        userRef.setValue(userData);
    }

    public void deleteUserData(final String userId) {
        DatabaseReference userRef = usersDataRef.child(userId);
        userRef.removeValue();
    }
    public void clearUsersData() {
        usersDataRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Обработка успешного удаления
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Обработка ошибки при удалении
                    }
                });
    }
    public ArrayList<UserData> fetchAllUsersData() {
        ArrayList<UserData> usersData = new ArrayList<>();
        usersDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    usersData.add(coneSnapshot.getValue(UserData.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки, если не удается получить данные
            }
        });
        return usersData;
    }

    public interface UserDataCallback {
        void onUserDataReceived(UserData userData);
        void onDataNotAvailable();
    }

    public void getUserDataByID(String id, UserDataCallback callback) {
        usersDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(Objects.requireNonNull(coneSnapshot.getValue(UserData.class)).getId(), id)){
                        callback.onUserDataReceived(coneSnapshot.getValue(UserData.class));
                        return;
                    }
                }
                callback.onDataNotAvailable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataNotAvailable();
            }
        });
    }


    // Дополнительные методы для получения данных и других операций с таблицей UsersData
}
