package com.example.xavier.pruebas.ui.take_sample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xavier.pruebas.R;
import com.example.xavier.pruebas.modelo.MultipleSelectOption;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultipleSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleSelectFragment extends Fragment {

    private static final String ARG_PARAM1 = "param_options";

    private ArrayList<MultipleSelectOption> mParamOptionsToShow;
    private OnFragmentInteractionListener mListener;

    public MultipleSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mParamOptionsToShow A list of options to show as checkboxes.
     * @return A new instance of fragment MultipleSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultipleSelectFragment newInstance(ArrayList<MultipleSelectOption> mParamOptionsToShow) {
        MultipleSelectFragment fragment = new MultipleSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mParamOptionsToShow);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamOptionsToShow = (ArrayList<MultipleSelectOption>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_multiple_select, container, false);

        LinearLayout vertical_layout = (LinearLayout) rootView.findViewById(R.id.vertical_layout);


        CheckBox checkBox;

        if (vertical_layout != null) {
            for (MultipleSelectOption option : mParamOptionsToShow) {
                checkBox = new CheckBox(getActivity());
                checkBox.setText(option.getTextToShow());
                checkBox.setTextSize(20);
                checkBox.setTag(option);
                checkBox.setChecked(option.isSelected());
                checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

                vertical_layout.addView(checkBox);
            }
        }
        else
            Log.e("MultipleSelectFragment", "NULLLLLL");

        Button bt_siguiente = (Button) rootView.findViewById(R.id.bt_next);
        bt_siguiente.setOnClickListener(new NextClickListener());

        return rootView;
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MultipleSelectOption option = (MultipleSelectOption) buttonView.getTag();
            option.setSelected(isChecked);
        }
    }


    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                Log.e("NextClickListener", "mListener NULL !!!");
            }
            else {
                mListener.onOptionsSelected(mParamOptionsToShow);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onOptionsSelected(ArrayList<MultipleSelectOption> aOptionsToShow);
    }
}
