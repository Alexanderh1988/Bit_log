package cl.hstech.bitlog.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cl.hstech.bitlog.R;


public class CustomList extends ArrayAdapter<String> {

    private final Context context;
    private static ArrayList<String> Key = new ArrayList<>();
    private static ArrayList<String> Value = new ArrayList<>();

    public CustomList(Context context, ArrayList<String> Key, ArrayList<String> Value) {

        super(context, R.layout.layout_listitem, Key);  //<--- no sé que es esta wea
        this.context = context;
        this.Key = Key;
        this.Value = Value;

    }

    class MyViewHolder      //clase almacena inicializacion de objetos para no llamarlos a cada rato
    {
        TextView nombre;
        TextView Precio;



        MyViewHolder(View view) {

            //update, se agrega con el texto y no el icono
            nombre = view.findViewById(R.id.key);
            Precio = view.findViewById(R.id.value);

        }
    }


   /* public CustomList(ValueEventListener valueEventListener, ArrayList<String> key, ArrayList<String> value) {
        super();
    }*/

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        //LayoutInflater inflater = context.getLayoutInflater();

        //  LayoutInflater mInflater = Inflater.inflate(R.layout.)

        MyViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_listitem, parent, false);
            holder = new MyViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        TextView key1 = (TextView) view.findViewById(R.id.key);

        TextView value1 = (TextView) view.findViewById(R.id.value);

        key1.setText(Key.get(position) + ": ");
        value1.setText(Value.get(position));

        return view;

    }

}


