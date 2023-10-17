package com.example.androidapp1.db_manage;

import androidx.annotation.NonNull;

import com.example.androidapp1.models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // Дополнительные методы для получения данных и других операций с таблицей UsersData
}
