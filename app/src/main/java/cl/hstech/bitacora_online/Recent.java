package cl.hstech.bitacora_online;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import cl.hstech.bitacora_online.adapter.CustomList;

public class Recent extends AppCompatActivity {

    ListView list;
    CustomList listAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent);

        if (list.getAdapter() == null) {
            listAdapter = new CustomList(this,MainActivity. getCodes(), null);
            list.setAdapter(listAdapter);
        }
        listAdapter.notifyDataSetChanged();

    }
}
