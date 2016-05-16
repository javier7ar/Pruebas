package com.example.xavier.pruebas;

import android.app.Fragment;
import android.app.FragmentContainer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TakeSampleActivity extends AppCompatActivity implements PhotoFragment.OnPhotoFragmentInteractionListener,
                                                                     InformationFragment.OnInformationFragmentInteractionListener{

    private List<Class> pasos;
    private int proximoPaso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        pasos = new ArrayList<Class>();

        pasos.add(InformationFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(InformationFragment.class);

        siguientePaso();


    }

    private void siguientePaso() {
        try {
            Fragment fragmento = (Fragment) pasos.get(proximoPaso).newInstance();

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragmento).commit();

            proximoPaso++;
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
