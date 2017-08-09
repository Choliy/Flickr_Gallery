package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.event.PrefRestoreEvent;
import com.choliy.igor.flickrgallery.fragment.AboutFragment;
import com.choliy.igor.flickrgallery.fragment.HistoryFragment;
import com.choliy.igor.flickrgallery.fragment.SavedFragment;
import com.choliy.igor.flickrgallery.fragment.SecretFragment;

import org.greenrobot.eventbus.EventBus;

public final class DialogUtils {

    private DialogUtils() {
    }

    public static void clearDialog(Context context, final HistoryFragment.SaveHistoryAsyncTask asyncTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_clear, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask.execute(Boolean.TRUE);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void deleteDialog(Context context, final SavedFragment.DeletePicTask picTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_delete, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picTask.execute();
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public static void restoreDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_restore, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new PrefRestoreEvent(Boolean.TRUE));
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void showSecretDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new SecretFragment().show(fragmentManager, SecretFragment.SECRET_TAG);
    }

    static void showHistoryDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new HistoryFragment().show(fragmentManager, HistoryFragment.HISTORY_TAG);
    }

    static void showAboutDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new AboutFragment().show(fragmentManager, AboutFragment.ABOUT_TAG);
    }
}