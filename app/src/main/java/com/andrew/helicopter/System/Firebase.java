package com.andrew.helicopter.System;

import static android.content.ContentValues.TAG;
import android.util.Log;
import androidx.annotation.NonNull;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Firebase {
    FirebaseAuth auth;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    String collection;
    String document;

    public Firebase(String collection) {
        this.collection = collection;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collection);
    }

    public Firebase(String collection, String document) {
        this.collection = collection;
        this.document = document;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collection);
        documentReference = collectionReference.document(document);
    }

    public void setDocument(String document) {
        this.document = document;
    }

    /**
     * Копирует шаблонную коллекцию деталей в новую
     * Можно использовать при создании нового вертолета путем копирования его деталей из шаблонной коллекции
     * Firebase fb = new Firebase("template");
     * fb.copyTemplateDetailCollection("200");
     * @param newCollection - имя новой коллекции
     */
    public void copyTemplateDetailCollection(String newCollection) {
        collectionReference
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Firebase fb = new Firebase(newCollection);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Detail item = document.toObject(Detail.class);
                        fb.setDocument(document.getId());
                        fb.saveDocument(item);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
    }

    /**
     * Копирует шаблонную коллекцию работ в новую
     * Можно использовать при создании нового вертолета путем копирования его работ из шаблонной коллекции
     * Firebase fb = new Firebase("worksTemplate");
     * fb.copyTemplateWorkCollection("works200");
     * @param newCollection - имя новой коллекции
     */
    public void copyTemplateWorkCollection(String newCollection) {
        collectionReference
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Firebase fb = new Firebase(newCollection);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Work item = document.toObject(Work.class);
                        fb.setDocument(document.getId());
                        fb.saveDocument(item);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
    }

    /**
     * Получает документ
     * Firebase fb = new Firebase("template", "Автомат давления");
     * fb.getDocument();
     */
    public void getDocument() {
        if (document == null) return;

        documentReference
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            Detail item = snapshot.toObject(Detail.class);
                        } else {
                            Log.d("FB", "No such document");
                        }
                    } else {
                        Log.d("FB", "get failed with ", task.getException());
                    }
                }
            });
    }

    /**
     * Сохраняет документ в коллекцию
     * Firebase fb = new Firebase("template");
     * fb.document = "Автомат давления";
     * fb.saveDocument(Detail object);
     * или
     * Firebase fb = new Firebase("template", "Автомат давления");
     * fb.saveDocument(Detail object);
     * @param object - сохраняемый объект
     */
    public void saveDocument(Object object) {
        if (document == null) return;
        collectionReference.document(document).set(object)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("FB: ", "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("FB: ", "Error writing document", e);
                }
            });
    }

    /**
     * Удаляет документ коллекции
     * Firebase fb = new Firebase("users");
     * fb.document = "jopa";
     * fb.deleteDocument();
     * или
     * Firebase fb = new Firebase("users", "jopa");
     * fb.deleteDocument();
     */
    public void deleteDocument() {
        if (document == null) return;
        collectionReference.document(document).delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("FB: ", "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("FB: ", "Error writing document", e);
                }
            });
    }

    /**
     * Удаляет коллекцию
     * Firebase fb = new Firebase("1111");
     * fb.deleteCollection();
     */
    public void deleteCollection() {
        collectionReference
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collectionReference.document(document.getId()).delete();
                    }
                } else {
                    Log.d("FB", "Error getting documents: ", task.getException());
                }
            });
    }

    @NonNull
    @Override
    public String toString() {
        return "Firebase{" +
                "collection='" + collection + '\'' +
                ", document='" + document + '\'' +
                '}';
    }
}
