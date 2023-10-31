package com.example.androidapp1.service;

import com.example.androidapp1.models.SiteUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SiteUsersManager {

    private DatabaseReference usersRef;

    public SiteUsersManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("SiteUsers");
    }

    public DatabaseReference getUsersDataRef() {
        return usersRef;
    }

    public CompletableFuture<SiteUser> getUserByID(String id) {
        CompletableFuture<SiteUser> future = new CompletableFuture<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    SiteUser user = coneSnapshot.getValue(SiteUser.class);
                    if (user != null && Objects.equals(user.getUid(), id)) {
                        future.complete(user);
                        return;
                    }
                }
                future.complete(null); // Завершить будущее с null, если пользователь не найден
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Failed to get user by ID: " + id, databaseError.toException()));
            }
        });

        return future;
    }

}
