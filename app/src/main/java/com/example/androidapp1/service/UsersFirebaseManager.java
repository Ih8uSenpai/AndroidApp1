package com.example.androidapp1.service;

import androidx.annotation.NonNull;

import com.example.androidapp1.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UsersFirebaseManager {
    private DatabaseReference usersRef;

    public UsersFirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
    }

    public void addUser(final User user) {
        // Генерируйте уникальный идентификатор пользователя, например, на основе его электронной почты
        String userId = user.getEmail().replace(".", "_");

        usersRef.child(userId).setValue(user);
    }

    public void deleteUser(String userId) {
        // Ссылка на конкретного пользователя, которого вы хотите удалить
        DatabaseReference userToDeleteRef = usersRef.child(userId);
        userToDeleteRef.removeValue()
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

    public void updateUser(String userId, User newUser) {
        // Ссылка на конкретного пользователя, которого вы хотите обновить
        DatabaseReference userToUpdateRef = usersRef.child(userId);

        // Установите новый объект User вместо старого значения
        userToUpdateRef.setValue(newUser)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Обработка успешного обновления
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Обработка ошибки при обновлении
                }
            });
    }
    public void clearUsersData() {
        usersRef.removeValue()
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

    public ArrayList<User> fetchAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    users.add(coneSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки, если не удается получить данные
            }
        });
        return users;
    }


    public CompletableFuture<User> getUserByID(String id) {
        CompletableFuture<User> future = new CompletableFuture<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && Objects.equals(user.getId(), id)) {
                        future.complete(user);
                        return;
                    }
                }
                future.complete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Failed to get user by ID: " + id, databaseError.toException()));
            }
        });

        return future;
    }


    // Дополнительные методы для получения данных, обновления и удаления элементов
}
