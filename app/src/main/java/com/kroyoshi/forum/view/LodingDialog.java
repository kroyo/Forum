package com.kroyoshi.forum.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.kroyoshi.forum.R;


public class LodingDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loding_view,null);

        AlertDialog dialog=new AlertDialog.Builder(getActivity(),R.style.note_dialog)
                .setCancelable(true)
                .setView(view)
                .create();
        return dialog;
    }

    public void show(Activity context) {

        show(context.getFragmentManager(), "LodingDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
