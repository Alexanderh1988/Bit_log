package cl.hstech.bitlog.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseQueries {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mFirebaseUserId;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEquipmentIdRef;

    public DatabaseQueries() {

        mEquipmentIdRef = mFirebaseDatabase.getInstance().getReference().child("equipmentId");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null)
            mFirebaseUserId = mFirebaseUser.getUid();
    }

    public FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    public FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public String getmFirebaseUserId() {
        return mFirebaseUserId;
    }

    public FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public DatabaseReference getmEquipmentIdRef() {
        return mEquipmentIdRef;
    }
}
