package real.estate.gokulam.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import customs.Fonts;
import real.estate.gokulam.R;


/**
 * Created by AND I5 on 05-02-2018.
 */

public class MessageUtils {

    public MessageUtils() {

    }

    /**
     * Show messge using snack Bar
     *
     * @param context
     * @param view
     * @param message
     */

    @SuppressLint("NewApi")
    public static Snackbar showSnackBar(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, String.valueOf(message), Snackbar.LENGTH_LONG);
        try {
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(16);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), Fonts.MEDIUM);
            textView.setTypeface(custom_font);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return snackbar;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return snackbar;
    }


    /**
     * Show ERROR/SUCCESS  messge using toast SHORT time
     *
     * @param context
     * @param msg
     */

    public static void showToastMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, String.valueOf(msg), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    /**
     * * Show ERROR/SUCCESS messge using toast LONG time
     *
     * @param context
     * @param msg
     */
    public static void showToastMessageLong(Context context, String msg) {
        Toast toast = Toast.makeText(context, String.valueOf(msg), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * Show doalog on loading time
     *
     * @param activity
     * @return
     */

    public static Dialog showDialog(FragmentActivity activity) {

        /*InputStream stream = null;
        try {
            stream = activity.getAssets().open("dialog_loader_img.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GifImageView gifImageView = new GifImageView(activity, stream);
        dialog.setContentView(gifImageView);*/
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.my_progress_indeterminate));
        dialog.setContentView(progressBar);

        //dialog.setContentView(R.layout.dialog_internet);

        //dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_trans);
      /* ImageView gifImageView = dialog.findViewById(R.id.dialog_loader_img);
        ImageUtils.setImage(gifImageView, R.drawable.loader_gif, activity);*/
        /*ImageView imageView = new ImageView(activity);
        imageView.setBackgroundColor(activity.getResources().getColor(R.color.black_transparent_50_op));
        ImageUtils.setImage(imageView, R.drawable.loading_throbber, activity);*/

        dialog.setCancelable(false);

        /*Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        // &=~ is showing dim dialog
        wlp.flags &= ~WindowManager.LayoutParams.DIM_AMOUNT_CHANGED;
        window.setAttributes(wlp);*/

        dialog.show();
        return dialog;

    }


    public static Dialog showNetworkDialog(final FragmentActivity activity) {

        final Dialog netWorkDialog = new Dialog(activity);
        netWorkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        netWorkDialog.setContentView(R.layout.dialog_internet);
        netWorkDialog.setCancelable(false);
        netWorkDialog.getWindow().setBackgroundDrawableResource(R.color.dialog_trans);
        TextView gotoSt = netWorkDialog.findViewById(R.id.gotoSt);
        TextView cancel = netWorkDialog.findViewById(R.id.cancel);

        gotoSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netWorkDialog.dismiss();
                try {
                    Intent intent = new Intent();
                    //$DataUsageSummaryActivity
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
                    activity.startActivity(intent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netWorkDialog.dismiss();
                activity.finish();
                MessageUtils.showToastMessageLong(activity, activity.getResources().getString(R.string.net_message));
            }
        });
        netWorkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                netWorkDialog.dismiss();
                activity.finish();
                MessageUtils.showToastMessageLong(activity, activity.getResources().getString(R.string.net_message));
                return false;
            }
        });
        netWorkDialog.show();
        return netWorkDialog;
    }

    /**
     * @param context
     * @param view
     * @param message
     * @return
     */

    @SuppressLint("NewApi")
    public static Snackbar showSnackBarAction(final Context context, View view, String message) throws Exception {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, String.valueOf(message), Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.black));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(16);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), Fonts.MEDIUM);
        textView.setTypeface(custom_font);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
        snackbar.setAction("Go", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
                context.startActivity(intent);
            }
        });

        return snackbar;


    }
/*
    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = (TextView) findViewById(R.id.usage);
                        if(txt.getVisibility() == View.VISIBLE){
                            txt.setVisibility(View.INVISIBLE);
                        }else{
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }*/

    public static Typeface setType(FragmentActivity activity, String fontType) {
        return Typeface.createFromAsset(activity.getAssets(), String.valueOf(fontType));
    }

    public static String setErrorMessage(int response_code) {
        String msg = "";
        switch (response_code) {
            case 400:
                //Random Error Message
                log("Random Error Message");
                msg = "Random Error Message";
                //MessageUtils.showSnackBar(activity, view, "Random Error Message");
                break;
            case 401:
                //Handle unauthorized
                log("Handle unauthorized");
                msg = "Handle unauthorized";
                //MessageUtils.showSnackBar(activity, view, "Handle unauthorized");
                break;
            case 403:
                msg = "403 -> Forbidden. The user is authenticated.";
                //showSnackBar(activity, view, "Forbidden. The user is authenticated.");
                // MessageUtils.showSnackBar(activity, view, msg);
                break;
            case 404:
                //not found
                log("not found");
                msg = "Server Not found";
                // MessageUtils.showSnackBar(activity, view, msg);
                break;
            case 405:
                msg = "405 -> Method Not Allowed";
                //  MessageUtils.showSnackBar(activity, view, msg);
                break;
            case 429:
                msg = "429 -> Too Many Requests";
                // MessageUtils.showSnackBar(activity, view, msg);
                break;
            case 500:
                //server broken
                log("Internal Server Error");
                msg = "500 -> Internal Server Error";
                //MessageUtils.showSnackBar(activity, view, "Server Broken");
                break;
            case 503:
                msg = "503 -> Service unavailable";
                // showSnackBar(activity, view, "Service unavailable");
                //MessageUtils.showSnackBar(activity, view, msg);
                break;
            case 600:
                //log("600");
                msg = "Response not found";
                //MessageUtils.showSnackBar(activity, view, "Response not found");
                break;
            default:
                //unknown error
                log("unknown error");
                msg = "Bad network connection";
                // MessageUtils.showSnackBar(activity, view, "Bad network connection");
                break;
        }
        return msg;
    }

    public static String setFailurerMessage(FragmentActivity activity, /*View view,*/ String response) {
        log(response);
        String msg = "";

        if (response.toLowerCase().contains("Unable to resolve host".toLowerCase())) {
            msg = activity.getResources().getString(R.string.net_message);
        } else if (response.toLowerCase().contains("Server not found".toLowerCase())) {
            msg = "Server not found";
        } else if (response.toLowerCase().contains("Socket closed".toLowerCase())) {
            msg = "Request Cancel";
        } else if (response.toLowerCase().contains("Expected BEGIN_ARRAY but was STRING at line".toLowerCase())) {
            msg = "Server response not found or not format";
        } else if (response.toLowerCase().contains("Software caused connection abort".toLowerCase())) {
            msg = response + ". Check Network Connection.";
        } else if (response.toLowerCase().contains("ssl handshake aborted".toLowerCase())) {
            msg = "Bad Network Connection or Server Not Found";
        } else if (response.toLowerCase().contains("No route to host".toLowerCase())) {
            msg = "No route to host";
        } else if (response.toLowerCase().contains("Failed to connect to".toLowerCase())) {
            msg = "Failed to connect to Server";
        } else if (response.toLowerCase().contains("connect timed out".toLowerCase())) {
            msg = "Connection timed out";
        } else if (response.toLowerCase().equals("No Internet Connection".toLowerCase())) {
            msg = response;
        } else if (response.isEmpty()) {
            msg = "Server response not found";
        } else {
            msg = "Something went wrong";
        }
        return msg;
    }

    public static void log(String meg) {
        Log.d("AAZP_LOG_MAIN", meg);
    }

    public static void dissmiss(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    public static void dismissSnackBar(Snackbar snackbar) {
        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    }
}
