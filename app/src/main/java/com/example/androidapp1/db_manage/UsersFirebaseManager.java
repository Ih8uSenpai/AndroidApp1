package com.example.androidapp1.db_manage;

import androidx.annotation.NonNull;

import com.example.androidapp1.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    // Дополнительные методы для получения данных, обновления и удаления элементов
}
