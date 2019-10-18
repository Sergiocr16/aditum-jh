package com.lighthouse.aditum.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import com.lighthouse.aditum.service.dto.RoundConfigurationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class FireBaseService {

    private final Logger log = LoggerFactory.getLogger(FireBaseService.class);
    private Firestore db;
    Resource xlsRes = new ClassPathResource("config/aditum-rondas-firebase-adminsdk.json");
    InputStream serviceAccount = xlsRes.getInputStream();
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(credentials)
        .build();
    InputStream result = new ByteArrayInputStream("anyString".getBytes(StandardCharsets.UTF_8));

    public FireBaseService() throws IOException {
        FirebaseApp.initializeApp(options);
        this.db = FirestoreClient.getFirestore();
    }

    public List<QueryDocumentSnapshot> getCollectionByCompany(String collectionName,String companyId) throws ExecutionException, InterruptedException {

        ApiFuture<QuerySnapshot> future =
         this.db.collection(collectionName).whereEqualTo("companyId", Double.parseDouble(companyId)).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents;

    }
//    private void getListItems() {
//        mFirebaseFirestore.collection("some collection").get()
//            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot documentSnapshots) {
//                    if (documentSnapshots.isEmpty()) {
//                        Log.d(TAG, "onSuccess: LIST EMPTY");
//                        return;
//                    } else {
//                        // Convert the whole Query Snapshot to a list
//                        // of objects directly! No need to fetch each
//                        // document.
//                        List<Type> types = documentSnapshots.toObjects(Type.class);
//
//                        // Add all to your list
//                        mArrayList.addAll(types);
//                        Log.d(TAG, "onSuccess: " + mArrayList);
//                    }
//                })
//                    .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }

}



