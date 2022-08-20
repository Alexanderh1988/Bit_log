package cl.hstech.bitlog.helpers;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class storageRequest {

    private StorageReference equipmentLocationPic;

    public storageRequest() {

        equipmentLocationPic = FirebaseStorage.getInstance().getReference().child("equipmentId");

    }

    public StorageReference getEquipmentLocationPic() {
        return equipmentLocationPic;
    }
}
