package real.estate.gokulam.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import customs.CustomButton;
import customs.CustomTextView;
import real.estate.gokulam.R;
import real.estate.gokulam.chat.chatUtils.ConnectionManager;
import real.estate.gokulam.chat.chatUtils.PushUtils;
import real.estate.gokulam.model.versionUpdate.ModelVersionUpdate;
import real.estate.gokulam.utils.FragmentCallUtils;
import real.estate.gokulam.utils.MessageUtils;
import real.estate.gokulam.utils.SessionManager;
import real.estate.gokulam.utils.Utils;
import real.estate.gokulam.views.colletionAgent.CollectionAgent;
import real.estate.gokulam.views.dashboard.Dashboard;
import real.estate.gokulam.views.registerLogin.LoginFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static real.estate.gokulam.utils.MessageUtils.log;
import static real.estate.gokulam.utils.Utils.USER_COLLECTION_AGENT;
import static real.estate.gokulam.utils.Utils.USER_CUSTOMER;
import static real.estate.gokulam.utils.Utils.USER_MEDIATOR;

/**
 * Created by AND I5 on 10-03-2018.
 */

public class SplashScreen extends Fragment {

    private LinearLayout update, mSkipLay;
    private CustomButton mUpdate, mUpdateOrSkip, mSkip;
    private CustomTextView mMsg, mSkipMsg, mErrorMsg;
    private Dialog dialog;
    private SessionManager sessionManager;
    List<String> strings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).setDisableToolBar();
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
        update = view.findViewById(R.id.update);
        mUpdate = view.findViewById(R.id.update_btn);
        mSkipLay = view.findViewById(R.id.skip_lay);
        mUpdateOrSkip = view.findViewById(R.id.update_skip);
        mSkip = view.findViewById(R.id.skip);
        mErrorMsg = view.findViewById(R.id.msg_skip);
        mSkipMsg = view.findViewById(R.id.error_msg);
        update.setVisibility(View.GONE);
        mMsg = view.findViewById(R.id.msg);
        mSkipLay.setVisibility(View.GONE);
        mErrorMsg.setVisibility(View.GONE);
        strings.add("");

        mErrorMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().overridePendingTransition(0,0);
                getActivity().finish();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Utils.haveNetworkConnection(getActivity())) {
                if (sessionManager.isLoggedIn()) {
                    connectToSendBird(getActivity(), SessionManager.getMobile(getActivity()), SessionManager.getName(getActivity()));
                    //connectToSendBird(getActivity(),"8098007484","jessica");

                }
                checkVersionUpdate();
            } else {
                dialog = MessageUtils.showNetworkDialog(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connectToSendBird(final FragmentActivity activity, String userId, final String userNickname) {

        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {

                //MessageUtils.showToastMessage(activity, "" + e.getMessage());

                updateCurrentUserInfo(activity, userNickname);
                updateCurrentUserPushToken(activity);

            }
        });
    }


    /**
     * Update the user's push token.
     */
    static void updateCurrentUserPushToken(FragmentActivity activity) {
        Log.d("sendBirs", "Connection push");

        PushUtils.registerPushTokenForCurrentUser(activity, null);
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    static void updateCurrentUserInfo(final FragmentActivity activity, String userNickname) {
        Log.d("sendBirs", "Connection info");
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                /*e.printStackTrace();
                if (e != null) {
                    Toast.makeText(activity, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MessageUtils.dissmiss(dialog);
    }

    private void checkVersionUpdate() {
        try {
            if (Utils.haveNetworkConnection(Objects.requireNonNull(getActivity()))) {
                Utils.getInstance(getActivity()).checkVersionForUpdate().enqueue(new Callback<ModelVersionUpdate>() {
                    @Override
                    public void onResponse(Call<ModelVersionUpdate> call, @NonNull Response<ModelVersionUpdate> response) {
                        try {
                            if (response.isSuccessful()) {
                                ModelVersionUpdate modelVersionUpdate = response.body();
                                checkActivityPauseOrResume(modelVersionUpdate);
                            } else {
                                String msg = MessageUtils.setErrorMessage(response.code());
                                MessageUtils.showToastMessage(getActivity(), msg);
                                mErrorMsg.setVisibility(View.VISIBLE);
                                mErrorMsg.setText(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelVersionUpdate> call, Throwable t) {
                        try {
                            String msg = MessageUtils.setFailurerMessage(getActivity(), t.getMessage());
                            MessageUtils.showToastMessage(getActivity(), msg);
                            mErrorMsg.setVisibility(View.VISIBLE);
                            mErrorMsg.setText(msg);
                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Home()).commit();
                            checkLogin();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
            } else {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Home()).commit();
                checkLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Home()).commit();
            checkLogin();
        }
    }

    private void checkActivityPauseOrResume(ModelVersionUpdate modelVersionUpdate) {
        try {
            PackageInfo pInfo = Objects.requireNonNull(getActivity()).getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = pInfo.versionName;
            //int versionCode = pInfo.versionCode;
            if (modelVersionUpdate != null) {
                log(" Live  " + modelVersionUpdate.getData().getVersion_code() + " Current " + versionName + "  " + modelVersionUpdate.getMessage() + "  " + modelVersionUpdate.getSuccess());
                if (modelVersionUpdate.getData().getVersion_code().equals(versionName)) {
                    //update.setVisibility(View.GONE);
                    log("" + versionName);
                    checkLogin();
                } else {

                    showForceUpdateDialog(modelVersionUpdate.getData().getMessage(), Math.floor(Double.parseDouble(versionName)) != Math.floor(Double.parseDouble(modelVersionUpdate.getData().getVersion_code())));
                    /*if (Math.floor(Double.parseDouble(versionName)) != Math.floor(Double.parseDouble(modelVersionUpdate.getData().getVersion_code()))) {
                        mSkipLay.setVisibility(View.GONE);
                        update.setVisibility(View.VISIBLE);
                        mMsg.setText(modelVersionUpdate.getMessage());
                        mUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });
                    } else {
                        if (modelVersionUpdate.getSuccess()) {
                            showSkip(modelVersionUpdate.getMessage());
                        } else {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Home()).commit();
                        }
                    }*/
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogin() {
        Log.d("sdsds", "" + SessionManager.getUserType(Objects.requireNonNull(getActivity())));

        if (sessionManager.checkLogin()) {
            if (SessionManager.getUserType(Objects.requireNonNull(getActivity())).toLowerCase().equals(USER_CUSTOMER.toLowerCase())) {
                FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
            } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_MEDIATOR.toLowerCase())) {
                FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
            } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_COLLECTION_AGENT.toLowerCase())) {
                FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new CollectionAgent());
            }
        } else {
            FragmentCallUtils.passFragmentWithoutAnim(Objects.requireNonNull(getActivity()), new LoginFragment());
        }
    }

    public void showForceUpdateDialog(final String message, boolean isForceUpdate) {
        // Log.e("UPDATE_MSG_STATUS", message + "  into_alert_status " + isForceUpdate + "  " + message);
        try {
            dialog = new Dialog(Objects.requireNonNull(getActivity()));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet);
            dialog.setCancelable(false);
            //dialog.getWindow().setWindowAnimations(R.style.grow);
            CustomTextView mTitle = dialog.findViewById(R.id.message_title);
            CustomTextView mMessage = dialog.findViewById(R.id.message);
            CustomTextView mCancel = dialog.findViewById(R.id.cancel);
            CustomTextView mSubmit = dialog.findViewById(R.id.gotoSt);
            mTitle.setText(R.string.uodate_available);
            mTitle.setAllCaps(true);
            mMessage.setText(message);
            mCancel.setText(R.string.skip);
            mSubmit.setText(R.string.update);
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    MessageUtils.showToastMessage(getActivity(), "" + message);
                    Objects.requireNonNull(getActivity()).finish();
                    return false;
                }
            });

            if (isForceUpdate) {
                mCancel.setVisibility(View.GONE);
/*            update_skip_layout.setVisibility(View.GONE);
            update_txt.setText(capitalize(message));
            update_layout.setVisibility(View.VISIBLE);*/
            } else {
                mCancel.setVisibility(View.VISIBLE);

            /*update_skip_layout.setVisibility(View.VISIBLE);
            update_skip_txt.setText(capitalize(message));
            update_layout.setVisibility(View.GONE);*/

            }
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MessageUtils.dissmiss(dialog);
                    onUpdateFromPlayStore(Objects.requireNonNull(getActivity()));
                }
            });
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MessageUtils.dissmiss(dialog);
                    skipBtn();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void skipBtn() {
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Home()).commit();
        checkLogin();
    }


    public static void onUpdateFromPlayStore(FragmentActivity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private void showSkip(String message) {
        update.setVisibility(View.GONE);
        mSkipLay.setVisibility(View.VISIBLE);
        mSkipMsg.setText(message);
        mUpdateOrSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = Objects.requireNonNull(getActivity()).getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.getUserType(Objects.requireNonNull(getActivity())).toLowerCase().equals(USER_CUSTOMER.toLowerCase())) {
                    FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
                } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_MEDIATOR.toLowerCase())) {
                    FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
                } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_COLLECTION_AGENT.toLowerCase())) {
                    FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new CollectionAgent());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        try {
            switch (requestCode) {
                case Utils.PERMISSION_REQUEST_CODE: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (SessionManager.getUserType(Objects.requireNonNull(getActivity())).toLowerCase().equals(USER_CUSTOMER.toLowerCase())) {
                            FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
                        } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_MEDIATOR.toLowerCase())) {
                            FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new Dashboard());
                        } else if (SessionManager.getUserType(getActivity()).toLowerCase().equals(USER_COLLECTION_AGENT.toLowerCase())) {
                            FragmentCallUtils.passFragmentWithoutAnim(getActivity(), new CollectionAgent());
                        }
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                        alertDialog.setTitle(getString(R.string.permission_required));
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(getString(R.string.settings_message));
                        alertDialog.setPositiveButton(getString(R.string.app_info), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(myAppSettings, Utils.PERMISSION_REQUEST_CODE);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
