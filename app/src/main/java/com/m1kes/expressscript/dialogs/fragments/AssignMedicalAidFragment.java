package com.m1kes.expressscript.dialogs.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.m1kes.expressscript.R;


public class AssignMedicalAidFragment extends DialogFragment {

    public interface ClickListener {
        void positiveClick(DialogFragment dialog, EditText editText);
        void negativeClick(DialogFragment dialog);
    }

    ClickListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AuthorizeUserDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_assign_medical_aid, container, false);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = null;
        Dialog d = null;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_fragment_assign_medical_aid, null);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_file_plus_black_24dp);
        builder.setTitle("Enter Medical Aid Number");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);

        d = builder.create();
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog d = (AlertDialog) getDialog();

        if(d!=null){

            Button positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = d.findViewById(R.id.editMemberNo);
                    if(editText == null)return;

                    if(editText.getText().toString().length() > 0){
                        mListener.positiveClick(AssignMedicalAidFragment.this,editText);
                    }else{
                        editText.setError("Invalid Medical Aid number!");
                    }
                }
            });

            Button negativeButton = d.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.negativeClick(AssignMedicalAidFragment.this);
                }
            });


        }


    }
}
