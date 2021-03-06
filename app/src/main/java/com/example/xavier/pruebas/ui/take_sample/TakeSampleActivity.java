package com.example.xavier.pruebas.ui.take_sample;


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

import com.example.xavier.pruebas.modelo.MultipleSelectOption;
import com.example.xavier.pruebas.modelo.MultipleSelectStep;
import com.example.xavier.pruebas.modelo.PhotoStep;
import com.example.xavier.pruebas.modelo.SelectOneStep;
import com.example.xavier.pruebas.modelo.Step;
import com.example.xavier.pruebas.pruebas.PhotoFragment;
import com.example.xavier.pruebas.R;
import com.example.xavier.pruebas.modelo.InformationStep;
import com.example.xavier.pruebas.modelo.Workflow;

import java.util.ArrayList;

public class TakeSampleActivity extends AppCompatActivity implements PhotoFragment.OnPhotoFragmentInteractionListener,
                                                                     InformationFragment.OnInformationFragmentInteractionListener,
                                                                     PhotoCameraFragment.OnPhotoCameraFragmentInteractionListener,
                                                                     MultipleSelectFragment.OnFragmentInteractionListener,
                                                                     SelectOneFragment.OnFragmentInteractionListener{


    //private int proximoPaso = 0;
    private TextView lb_nro_paso;
    protected Workflow workflow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        getSupportFragmentManager().addOnBackStackChangedListener(new MyOnBackStackChangedListener());

        lb_nro_paso = (TextView) findViewById(R.id.lb_nro_paso);

/*
        // Opciones para el multi select
        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption(getResources().getString(R.string.step_3_param_1), false));
        optionsToSelect.add(new MultipleSelectOption(getResources().getString(R.string.step_3_param_2), false));
        optionsToSelect.add(new MultipleSelectOption(getResources().getString(R.string.step_3_param_3), false));
        optionsToSelect.add(new MultipleSelectOption(getResources().getString(R.string.step_3_param_4),false));

        // Opciones para el select one
        ArrayList<MultipleSelectOption> optionsToSelect2 = new ArrayList<MultipleSelectOption>();
        optionsToSelect2.add(new MultipleSelectOption(getResources().getString(R.string.step_5_param_1),false));
        optionsToSelect2.add(new MultipleSelectOption(getResources().getString(R.string.step_5_param_2),false));
        optionsToSelect2.add(new MultipleSelectOption(getResources().getString(R.string.step_5_param_3),false));
        optionsToSelect2.add(new MultipleSelectOption(getResources().getString(R.string.step_5_param_4),false));

        workflow = new Workflow();
        workflow.addStep(new InformationStep(getResources().getString(R.string.step_1_param_1)));

        workflow.addStep(new MultipleSelectStep(optionsToSelect));

        workflow.addStep(new SelectOneStep(optionsToSelect2));

        workflow.addStep(new PhotoStep(getResources().getString(R.string.step_2_param_1),getResources().getString(R.string.step_2_param_2)));


        workflow.addStep(new PhotoStep(getResources().getString(R.string.step_4_param_1),getResources().getString(R.string.step_4_param_2)));
        workflow.addStep(new InformationStep(getResources().getString(R.string.step_6_param_1)));
*/

    }

    @Override
    protected void onStart () {
        super.onStart();

        siguientePaso();
        actualizarPasoEnPantalla();
    }

    private void siguientePaso() {

        if (!workflow.isEnd()) {
            Step step = workflow.nextStep();


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment fragmento;

            if (InformationStep.class.isInstance(step)) {
                InformationStep informationStep = (InformationStep) step;
                fragmento = InformationFragment.newInstance(informationStep.getTextToShow());
            }
            else if (PhotoStep.class.isInstance(step)) {
                PhotoStep photoStep = (PhotoStep) step;
                fragmento = PhotoCameraFragment.newInstance(photoStep.getInstructionsToShow(),photoStep.getPathToImageToOverlay());
            }
            else if (MultipleSelectStep.class.isInstance(step)) {
                MultipleSelectStep multipleSelectStep = (MultipleSelectStep) step;
                fragmento = MultipleSelectFragment.newInstance(multipleSelectStep.getOptionsToSelect());
            }
            else if (SelectOneStep.class.isInstance(step)) {
                SelectOneStep selectOneStep = (SelectOneStep) step;
                fragmento = SelectOneFragment.newInstance(selectOneStep.getOptionsToSelect());
            }
            else
                fragmento = null;


                if (fragmento != null) {

                    transaction.replace(R.id.container, fragmento);
                    if (workflow.getStepPosition() > 0) {
                        transaction.addToBackStack(null);
                        Log.e("TakeSampleActivity", "addToBackStack");
                    }
                    transaction.commit();
                }

        }
        else {
            // Termino...
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
        lb_nro_paso.setText(String.valueOf(workflow.getStepPosition()+1) + "/" + String.valueOf(workflow.getStepCount()));
    }
/*
    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }*/

    @Override
    public void onOptionsSelected(ArrayList<MultipleSelectOption> aOptionsToShow) {
        for (MultipleSelectOption option : aOptionsToShow) {
            Log.e("onOptionsSelected", option.getTextToShow() +":" + String.valueOf(option.isSelected()));
        }

        siguientePaso();
    }

    @Override
    public void onOneOptionSelected(ArrayList<MultipleSelectOption> aOptionsToShow) {

        siguientePaso();
    }

    private class MyOnBackStackChangedListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            Log.e("onBackStackChanged","onBackStackChanged: "+String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

            // +1 xq el primer fragment no se agrega al BackStack
            //proximoPaso = getSupportFragmentManager().getBackStackEntryCount()+1;


            workflow.setStepPosition(getSupportFragmentManager().getBackStackEntryCount());

            actualizarPasoEnPantalla();
        }
    }
}
