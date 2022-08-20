package cl.hstech.bitlog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.UploadTask;

import cl.hstech.bitlog.helpers.storageRequest;


public class ManualAdd extends AppCompatActivity implements View.OnClickListener {

    private static final Object TAG = "debug";
    EditText itemName;
    Button uploadPic;
    Button scanCode;
    Button saveData;
    Button cancel;
    private int pictureCamRequest = 21124121;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_add);

        itemName = findViewById(R.id.item_name);

        cancel = findViewById(R.id.cancel);
        uploadPic = findViewById(R.id.upload_pic);
        scanCode = findViewById(R.id.code_scaner);
        saveData = findViewById(R.id.save_data);

    }

    public void takePicture() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Elegir foto"), pictureCamRequest);

    }

    public void uploadPicAtDatabase(Uri uri) {

        storageRequest st = new storageRequest();

        //esto es para foto de portada
        UploadTask uploadTask = st.getEquipmentLocationPic().putFile(uri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return st.getEquipmentLocationPic().getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Uri downloadUri = task.getResult();

            }
        }).addOnFailureListener(e -> {

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == pictureCamRequest) {
            uploadPicAtDatabase(data.getData());
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancel:
                break;

            case R.id.upload_pic:
                takePicture();
                break;
        }
    }
}
