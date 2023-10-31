package com.example.androidapp1.service;

import androidx.annotation.NonNull;

import com.example.androidapp1.models.Cone;
import com.example.androidapp1.models.ConeUserdata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConeFirebaseManager {
    private DatabaseReference conesRef;
    public static int currentId = 0;
    public static ArrayList<Cone> cones_data;

    public ConeFirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        conesRef = database.getReference("Cones");
        cones_data = fetchCones();
    }

    public void addCone(final Cone cone) {
        conesRef.child(String.valueOf(currentId)).setValue(cone);
        currentId++;
    }

    public void clearAllCones() {
        conesRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Обработка успешной очистки
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Обработка ошибки при очистке
                    }
                });
    }

    public void deleteCone(String coneId) {
        // Ссылка на конкретный конус, который вы хотите удалить
        DatabaseReference coneToDeleteRef = conesRef.child(coneId);
        coneToDeleteRef.removeValue()
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


    public void updateCone(String oldConeId, Cone newCone) {
        // Ссылка на конкретный конус, который вы хотите обновить
        DatabaseReference coneToUpdateRef = conesRef.child(oldConeId);

        // Установите новый объект Cone вместо старого значения
        coneToUpdateRef.setValue(newCone)
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

    public List<ConeUserdata> fetchConesAndInitializeUserData() {
        List<ConeUserdata> coneUserdataList = new ArrayList<>();
        conesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coneUserdataList.clear(); // Очистите список перед добавлением новых значений
                int id = 0;
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    String name = (coneSnapshot.child("name").getValue(String.class));
                    ConeUserdata coneUserdata = new ConeUserdata(id, name, 0, 1);
                    id++;
                    coneUserdataList.add(coneUserdata);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки, если не удается получить данные
            }
        });
        return coneUserdataList;
    }

    public ArrayList<Cone> fetchCones() {
        ArrayList<Cone> cones = new ArrayList<>();
        conesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot coneSnapshot : dataSnapshot.getChildren()) {
                    cones.add(coneSnapshot.getValue(Cone.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки, если не удается получить данные
            }
        });
        return cones;
    }
    // Дополнительные методы для получения данных, обновления и удаления элементов
}
