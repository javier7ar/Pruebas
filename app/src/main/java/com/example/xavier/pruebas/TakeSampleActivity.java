package com.example.xavier.pruebas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TakeSampleActivity extends Activity implements PhotoFragment.OnPhotoFragmentInteractionListener,
                                                                     InformationFragment.OnInformationFragmentInteractionListener{

    private List<Class> pasos;
    private int proximoPaso = 0;
    private TextView lb_nro_paso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        lb_nro_paso = (TextView) findViewById(R.id.lb_nro_paso);

        pasos = new ArrayList<Class>();

        pasos.add(InformationFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(InformationFragment.class);

        siguientePaso();


    }

    private void siguientePaso() {
        try {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment fragmento = (Fragment) pasos.get(proximoPaso).newInstance();

            transaction.replace(R.id.container, fragmento);
            transaction.addToBackStack(null);
            Log.e("TakeSampleActivity", "addToBackStack");
            transaction.commit();

            proximoPaso++;

            lb_nro_paso.setText(String.valueOf(proximoPaso)+"/"+String.valueOf(pasos.size()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        /*PhotoFragment fragmento = PhotoFragment.newInstance("","");

        getFragmentManager().beginTransaction()
                .add(R.id.container, fragmento).commit();
         */
    }

    @Override
    public void onPhotoTaked(Uri uri) {
        Log.e("TakeSampleActivity", "onPhotoTaked");
        siguientePaso();
    }

    @Override
    public void onInformationReaded() {
        Log.e("TakeSampleActivity", "onInformationReaded");
        siguientePaso();
    }
}
