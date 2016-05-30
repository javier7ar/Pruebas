package com.example.xavier.pruebas;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class TakeSampleActivity extends AppCompatActivity implements PhotoFragment.OnPhotoFragmentInteractionListener,
                                                                     InformationFragment.OnInformationFragmentInteractionListener,
                                                                     PhotoCameraFragment.OnPhotoCameraFragmentInteractionListener{

    private List<Class> pasos;
    private int proximoPaso = 0;
    private TextView lb_nro_paso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        getSupportFragmentManager().addOnBackStackChangedListener(new MyOnBackStackChangedListener());

        lb_nro_paso = (TextView) findViewById(R.id.lb_nro_paso);

        pasos = new ArrayList<Class>();

        pasos.add(InformationFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(PhotoFragment.class);
        pasos.add(InformationFragment.class);

        siguientePaso();

        actualizarPasoEnPantalla();

    }

    private void siguientePaso() {

        if ((proximoPaso >= 0) &&(proximoPaso < pasos.size())) {

//            try {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Fragment fragmento; // = (Fragment) pasos.get(proximoPaso).newInstance();

                switch (proximoPaso) {

                    case 0:
                        fragmento = InformationFragment.newInstance("Registro regional de gatos. Gracias por ayudarnos. Vamos a pedirle que saque una serie de dos fotos a su gato. Siga las instrucciones que le aparecerán en pantalla.");
                        break;
                    case 1:
                        fragmento = PhotoCameraFragment.newInstance("Saquenle una foto de cuerpo completo a su gato.","");
                        break;

                    case 2:
                        fragmento = PhotoCameraFragment.newInstance("Saquele una foto a la cabeza de su gato.","");
                        break;
                    case 3:
                        fragmento = InformationFragment.newInstance("Muchas gracias por su colaboracion!");
                        break;

                    default:
                        fragmento = null;
                        break;
                }

                if (fragmento != null) {

                    transaction.replace(R.id.container, fragmento);
                    if (proximoPaso > 0)
                        transaction.addToBackStack(null);
                    Log.e("TakeSampleActivity", "addToBackStack");
                    transaction.commit();

                    proximoPaso++;
                }

/*
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
*/
        }
        else {
            if (proximoPaso == pasos.size())
                Toast.makeText(this, "Coool!!", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("NuevoClienteActivity","KEYCODE_BACK");
            //Fragment f =  getFragmentManager().findFragmentById(R.id.container);
            /*
            if (f instanceof OnBackKeyDown) {

                ((OnBackKeyDown) f).onBackKeyDown();
                //return true;
            }*/
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPhotoTaked(Uri uri) {
        Log.e("TakeSampleActivity", "onPhotoTaked");
        siguientePaso();
    }

    @Override
    public void  onPhotoCameraFragmentInteraction(Uri uri) {
        Log.e("TakeSampleActivity", "onPhotoCameraFragmentInteraction");
        siguientePaso();
    }

    @Override
    public void onInformationReaded() {
        Log.e("TakeSampleActivity", "onInformationReaded");
        siguientePaso();
    }

    private void actualizarPasoEnPantalla() {
        lb_nro_paso.setText(String.valueOf(proximoPaso) + "/" + String.valueOf(pasos.size()));
    }

    private class MyOnBackStackChangedListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            Log.e("onBackStackChanged","onBackStackChanged: "+String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            // +1 xq el primer fragment no se agrega al BackStack
            proximoPaso = getSupportFragmentManager().getBackStackEntryCount()+1;
            actualizarPasoEnPantalla();
        }
    }
}
