package edu.cnm.deepdive.coloringapp.controler;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import android.webkit.WebView;
import edu.cnm.deepdive.coloringapp.R;


/**
 * The type Licenses dialog fragment.
 */
public class LicensesDialogFragment extends DialogFragment {

  /**
   * New instance licenses dialog fragment.
   *
   * @return the licenses dialog fragment
   */
  public static LicensesDialogFragment newInstance() {
    return new LicensesDialogFragment();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    WebView view = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog, null);
    view.loadUrl("file:///android_asset/open_source_licenses.html");
    return new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
        .setTitle(getString(R.string.action_licenses))
        .setView(view)
        .setPositiveButton(android.R.string.ok, null)
        .create();
  }

}
