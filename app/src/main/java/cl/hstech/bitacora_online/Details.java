package cl.hstech.bitacora_online;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import cl.hstech.bitacora_online.adapter.CustomList;

public class Details extends AppCompatActivity {

    private CustomList listAdapter;
    private ListView list;
    private ArrayList<String> machineDetail;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

    }
}
