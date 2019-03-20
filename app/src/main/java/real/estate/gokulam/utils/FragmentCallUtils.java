package real.estate.gokulam.utils;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import real.estate.gokulam.R;

/**
 * Created by AND I5 on 09-03-2018.
 */

public class FragmentCallUtils {


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(FragmentActivity activity, @Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int frameId) {
        if (Utils.haveNetworkConnection(activity)) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentWithBackStack(FragmentActivity activity, @Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int frameId) {
        if (Utils.haveNetworkConnection(activity)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public static void passFragment(FragmentActivity activity, Fragment fragment) {
        Log.d("network_texter", "" + Utils.haveNetworkConnection(activity));
        if (Utils.haveNetworkConnection(activity)) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.left_to_right_start, R.anim.right_to_left_start, R.anim.right_to_left_end, R.anim.left_to_right_end);
            transaction.replace(R.id.container_body, fragment, "" + fragment.getClass());
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void passFragmentWithoutAnim(FragmentActivity activity, Fragment fragment) {
        if (Utils.haveNetworkConnection(activity)) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(R.anim.left_to_right_start, R.anim.right_to_left_start, R.anim.right_to_left_end, R.anim.left_to_right_end);
            transaction.replace(R.id.container_body, fragment, "" + fragment.getClass());
            transaction.addToBackStack(null);
            activity.getFragmentManager().findFragmentById(R.id.container_body);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void passFragmentWithoutBackStatck(FragmentActivity activity, Fragment fragment) {
        if (Utils.haveNetworkConnection(activity)) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.left_to_right_start, R.anim.right_to_left_start, R.anim.right_to_left_end, R.anim.left_to_right_end);
            transaction.replace(R.id.container_body, fragment, "" + fragment.getClass());
            //transaction.addToBackStack(null);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void passFragmentWithoutBackStatckAnaAnim(FragmentActivity activity, Fragment fragment) {
        if (Utils.haveNetworkConnection(activity)) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            // transaction.setCustomAnimations(R.anim.left_to_right_start, R.anim.right_to_left_start, R.anim.right_to_left_end, R.anim.left_to_right_end);
            transaction.replace(R.id.container_body, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        } else {
            try {
                MessageUtils.showSnackBarAction(activity, activity.getCurrentFocus(), activity.getString(R.string.check_inter_net));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void callIntent(FragmentActivity activity, Class<Object> siteVideosClass) {

    }
/*
    public static void callIntent(FragmentActivity fragmentActivity, Class<Activity> passActivity) {
        Intent intent = new Intent(fragmentActivity, passActivity);
        fragmentActivity.startActivity(intent);
        fragmentActivity.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end);
    }*/
}
