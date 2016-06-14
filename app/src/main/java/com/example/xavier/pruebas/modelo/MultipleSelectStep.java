package com.example.xavier.pruebas.modelo;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends Step {

    private ArrayList<MultipleSelectOption> optionsToSelect;

    public MultipleSelectStep() {
        optionsToSelect = new ArrayList<MultipleSelectOption>();
    }

    public MultipleSelectStep(ArrayList<MultipleSelectOption> anOptionsToSelect) {

        optionsToSelect = anOptionsToSelect;
    }

    public ArrayList<MultipleSelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }
}
