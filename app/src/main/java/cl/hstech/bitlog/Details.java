package cl.hstech.bitlog;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cl.hstech.bitlog.R;
import cl.hstech.bitlog.adapter.CustomList;
import cl.hstech.bitlog.equipments.EquipmentData;

import static cl.hstech.bitlog.MainActivity.codes;
import static cl.hstech.bitlog.MainActivity.mQuery;

public class Details extends AppCompatActivity {

    String machine, picture, lastSeenLocation;
    TextView machineName;
    ImageView pictureDetail;
    View mMap;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        machineName = findViewById(R.id.machine_name);
        pictureDetail = findViewById(R.id.picture_detail);
        mMap = findViewById(R.id.detail_last_seen);

        String Codigo = getIntent().getStringExtra("Codigo");

        if (Codigo != null) {
            mQuery.getmEquipmentIdRef().child(Codigo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getKey().equals("machine"))
                            machine = ds.getValue().toString();
                        if (ds.getKey().equals("picture"))
                            picture = ds.getValue().toString();
                        if (ds.getKey().equals("lastSeenLocation"))
                            lastSeenLocation = ds.getValue().toString();
                    }

                    EquipmentData Data = new EquipmentData(machine, picture, lastSeenLocation);

                    machineName.setText(Data.getMachine());
                    Picasso.get().load(picture).into(pictureDetail);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

    }
}
