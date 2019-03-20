package real.estate.gokulam.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.Stack;

import customs.CustomButton;
import customs.CustomTextView;
import customs.CustomTypefaceSpan;
import customs.Fonts;
import okhttp3.ResponseBody;
import real.estate.gokulam.BuildConfig;
import real.estate.gokulam.R;
import real.estate.gokulam.model.logout.ModelLogout;
import real.estate.gokulam.model.payment.ModelPaymentInfoPass;
import real.estate.gokulam.utils.DatetimeUtils;
import real.estate.gokulam.utils.FragmentCallUtils;
import real.estate.gokulam.utils.MessageUtils;
import real.estate.gokulam.utils.PushUtils;
import real.estate.gokulam.utils.SessionManager;
import real.estate.gokulam.utils.Utils;
import real.estate.gokulam.views.about.AboutFragment;
import real.estate.gokulam.views.colletionAgent.CollectionAgent;
import real.estate.gokulam.views.contact.ContactFragment;
import real.estate.gokulam.views.dashboard.Dashboard;
import real.estate.gokulam.views.liveChat.chats1810034.ConnectionManager;
import real.estate.gokulam.views.liveChat.chats1810034.GroupChannelActivity;
import real.estate.gokulam.views.mediator.AgentMediator;
import real.estate.gokulam.views.mediator.MediatorBooked;
import real.estate.gokulam.views.propertyInfo.bookedProperty.ListOfBookedProperty;
import real.estate.gokulam.views.registerLogin.LoginFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static real.estate.gokulam.utils.Utils.DUE_AMOUNT_PAYMENT;
import static real.estate.gokulam.utils.Utils.FONEPAISAPG_RET_CODE;
import static real.estate.gokulam.utils.Utils.PRIVACY_POLICY;
import static real.estate.gokulam.utils.Utils.USER_COLLECTION_AGENT;
import static real.estate.gokulam.utils.Utils.USER_CUSTOMER;
import static real.estate.gokulam.utils.Utils.USER_MEDIATOR;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Dialog dialog;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce;
    public static MenuItem action_settings;
    public static CustomTextView mAppVersion, mAppUserName, mAppEmailID, mToolbarTitle;
    private SessionManager sessionManager;
    public static Stack<Fragment> fragmentStack;
    FragmentManager fragmentManager;
    Fragment homeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(MainActivity.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentStack = new Stack<Fragment>();

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAppVersion = findViewById(R.id.app_version);
        navigationView = findViewById(R.id.nav_view);
        mToolbarTitle = findViewById(R.id.tool_bar_title);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
        try {
            View hView = navigationView.getHeaderView(0);
            mAppEmailID = hView.findViewById(R.id.textView);
            mAppUserName = hView.findViewById(R.id.name);
            mAppEmailID.setText(SessionManager.getEmailId(MainActivity.this));
            mAppUserName.setText(SessionManager.getName(MainActivity.this) + " " + SessionManager.getMobile(MainActivity.this));
            Log.d("versionName", "" + BuildConfig.VERSION_NAME + "  " + BuildConfig.VERSION_CODE);
            mAppVersion.setText("v" + BuildConfig.VERSION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAppEmailID.setText(SessionManager.getEmailId(MainActivity.this));
        mAppUserName.setText(SessionManager.getName(MainActivity.this));

      /*  homeListFragment = new SplashScreen();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container_body, homeListFragment);
        fragmentStack.push(homeListFragment);
        ft.commit();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Utils.haveNetworkConnection(MainActivity.this)) {
                Fragment ff = getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (ff != null) { // Re-call same fragment after enable network connection
                    FragmentCallUtils.passFragmentWithoutBackStatck(MainActivity.this, ff);
                } else {
                    FragmentCallUtils.passFragmentWithoutBackStatck(MainActivity.this, new SplashScreen());
                }
            } else {
                dialog = MessageUtils.showNetworkDialog(MainActivity.this);
            }
        } catch (Exception e) {
            FragmentCallUtils.passFragmentWithoutBackStatck(MainActivity.this, new Dashboard());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activityResult", "on_activity_resul " + requestCode + " FONEPAISAPG_RET_CODE " + FONEPAISAPG_RET_CODE + "  " + data + "  " + resultCode);
        try {
            if (requestCode == FONEPAISAPG_RET_CODE && resultCode == RESULT_OK) {
                Log.d("activityResult", "Payment");
                assert data != null;
                String returnedcode = data.getStringExtra("resp_code");
                Log.d("returnedcodesdsd", "" + returnedcode);
                String identity = data.getStringExtra("identity");
                String message = data.getStringExtra("resp_msg");
                String sentjson = data.getStringExtra("data_sent");
                String returnedjson = data.getStringExtra("data_recieved");

                sendSucess(message, returnedcode, sentjson, returnedjson, data, Integer.parseInt(identity));


            } else if (requestCode == DUE_AMOUNT_PAYMENT && resultCode == RESULT_OK) {
                Log.d("activityResult", "Payment");
                String message = data.getStringExtra("resp_msg");
                String returnedcode = data.getStringExtra("resp_code");
                String sentjson = data.getStringExtra("data_sent");
                String returnedjson = data.getStringExtra("data_recieved");

                sendSucess(message, returnedcode, sentjson, returnedjson, data, 2);

            } else {

                Log.d("activityResult", "Payment no");
                assert data != null;
                String message = data.getStringExtra("resp_msg");
                MessageUtils.showToastMessage(getApplicationContext(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSucess(String message, String returnedcode, String sentjson, final String returnedjson, Intent data, final int indentity) {
        try {
            JsonObject postParam = new JsonParser().parse(returnedjson).getAsJsonObject();
            postParam.addProperty("message", message);
            postParam.addProperty("mobiles", SessionManager.getMobile(MainActivity.this));
            postParam.addProperty("userId", SessionManager.getUserId(MainActivity.this));

            Call<ResponseBody> modelPaymentInfoPassCall = Utils.getInstance(MainActivity.this).callWebApi("payment_status", SessionManager.getToken(MainActivity.this), postParam);
            final Dialog dialog = MessageUtils.showDialog(MainActivity.this);

            modelPaymentInfoPassCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        MessageUtils.dissmiss(dialog);
                        if (response.isSuccessful()) {
                            ModelPaymentInfoPass modelPaymentInfoPass = new Gson().fromJson(response.body().string(), ModelPaymentInfoPass.class);
                            if (modelPaymentInfoPass.mStatus) {

                                /*MessageUtils.showSnackBar(MainActivity.this, drawer, modelPaymentInfoPass.mMessage);
                                FragmentCallUtils.passFragment(MainActivity.this, new ListOfBookedProperty());*/

                                showPaymentStatus(indentity, returnedjson);

                            } else {
                                //MessageUtils.showSnackBar(MainActivity.this, drawer, modelPaymentInfoPass.mMessage);
                                MessageUtils.showToastMessage(getApplicationContext(), modelPaymentInfoPass.mMessage);
                            }
                        } else {
                            String msg = MessageUtils.setErrorMessage(response.code());
                            //MessageUtils.showSnackBar(MainActivity.this, drawer, msg);
                            MessageUtils.showToastMessage(getApplicationContext(), msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MessageUtils.showToastMessage(getApplicationContext(), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        MessageUtils.dissmiss(dialog);
                        String msg = MessageUtils.setFailurerMessage(MainActivity.this, t.getMessage());
                        //MessageUtils.showSnackBar(MainActivity.this, drawer, msg);

                        MessageUtils.showToastMessage(getApplicationContext(), msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPaymentStatus(final int indentity, String returnedjson) {
        try {
            Log.d("returnedjsonsds", "" + returnedjson);
            JsonObject postParam = new JsonParser().parse(returnedjson).getAsJsonObject();
            Log.d("postParamsdsd", "" + postParam);
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_payment);
            dialog.show();
            dialog.setCancelable(false);

            final CustomTextView amount = dialog.findViewById(R.id.payment_amount);
            CustomTextView payment_status = dialog.findViewById(R.id.payment_status);
            CustomTextView trans_id = dialog.findViewById(R.id.trans_id);
            CustomTextView date_pay = dialog.findViewById(R.id.date_pay);
            CustomTextView time_pay = dialog.findViewById(R.id.time_pay);

            CustomButton done_payment = dialog.findViewById(R.id.done_payment);

            String payment_reference = postParam.get("payment_reference").toString().replace("\"", "").replace("\"", "");
            String status = postParam.get("status").toString().replace("\"", "").replace("\"", "");
            String message;//postParam.get("message").toString().replace("\"", "").replace("\"", "");
            String amountStr = postParam.get("amount").toString().replace("\"", "").replace("\"", "");

      /*  String payment_detail = postParam.get("payment_detail").toString();
        String merchant_display = postParam.get("merchant_display").toString();
        String invoice = postParam.get("invoice").toString();
        String payment_type = postParam.get("payment_type").toString();*/

            date_pay.setText(DatetimeUtils.getDate("dd MMM yyyy"));
            time_pay.setText(DatetimeUtils.getDate("hh:mm a"));

            if (status.toLowerCase().startsWith("s".toLowerCase())) {
                message = "Payment Successful";
                amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0);
            } else {
                message = "Payment Failure";
                amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_failure, 0);
            }

            trans_id.setText("Transaction Id : " + payment_reference);
            payment_status.setText(message);
            amount.setText(getString(R.string.inr) + " " + amountStr);


            done_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageUtils.dissmiss(dialog);
                /*if (!isPressed) {
                    isPressed = true;
                    amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_failure, 0)
                } else {
                    isPressed = false;
                    amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0)
                }*/

                    if (indentity == FONEPAISAPG_RET_CODE) {   // Initial Amount Booked

                        if (SessionManager.getUserType(MainActivity.this).equals(Utils.USER_MEDIATOR)) {
                            FragmentCallUtils.passFragment(MainActivity.this, new MediatorBooked());
                        } else {
                            FragmentCallUtils.passFragment(MainActivity.this, new ListOfBookedProperty());
                        }

                    } else { // Due Amount Payment
                        FragmentCallUtils.passFragment(MainActivity.this, new ListOfBookedProperty());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Fonts.MEDIUM);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        try {
            Fragment ff = getSupportFragmentManager().findFragmentById(R.id.container_body);
            Log.d("back_pressed", "" + fragmentStack.size() + "  " + ff.getTag());
            String userType = SessionManager.getUserType(MainActivity.this);
            if (userType.equals(USER_CUSTOMER)) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } /*else if (fragmentStack.size() > 1) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragmentStack.lastElement().onPause();
                ft.remove(fragmentStack.pop());
                fragmentStack.lastElement().onResume();
                ft.show(fragmentStack.lastElement());
                ft.commit();
            }*/ else if (ff instanceof Dashboard || ff instanceof LoginFragment) {
                    if (doubleBackToExitPressedOnce) {
                        super.onBackPressed();
                        finish();
                        return;
                    }
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press again to back", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } else {
                    super.onBackPressed();
                }
            } else if (userType.equals(USER_MEDIATOR)) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (ff instanceof Dashboard || ff instanceof LoginFragment) {
                    if (doubleBackToExitPressedOnce) {
                        super.onBackPressed();
                        finish();
                        return;
                    }
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press again to back", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } else if (ff instanceof MediatorBooked) {
                    FragmentCallUtils.passFragmentWithoutBackStatck(MainActivity.this, new Dashboard());
                } else {
                    super.onBackPressed();
                }
            } else if (userType.equals(USER_COLLECTION_AGENT)) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (ff instanceof CollectionAgent || ff instanceof LoginFragment) {
                    if (doubleBackToExitPressedOnce) {
                        super.onBackPressed();
                        finish();
                        return;
                    }
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press again to back", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String userType = SessionManager.getUserType(MainActivity.this);
        if (userType.equals(Utils.USER_CUSTOMER)) {
            if (id == R.id.nav_home) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new Dashboard());
            } else if (id == R.id.nav_booked_property) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new ListOfBookedProperty());
            } else if (id == R.id.nav_live_chat) {
                //FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new LiveChat());

                try {
                    connectsendbird();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (id == R.id.nav_logout) {
                logout();
            } else if (id == R.id.nav_about_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new AboutFragment());
            } else if (id == R.id.nav_contact_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new ContactFragment());
            } else if (id == R.id.nav_privacy_policy) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                builder.setExitAnimations(this, R.anim.right_to_left_end, R.anim.left_to_right_end);
                builder.setStartAnimations(this, R.anim.left_to_right_start, R.anim.right_to_left_start);
                builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(PRIVACY_POLICY));
            }

        } else if (userType.equals(USER_MEDIATOR)) {
            if (id == R.id.nav_home) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new Dashboard());
            } else if (id == R.id.nav_my_booking) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new MediatorBooked());
            } else if (id == R.id.nav_booked_property) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new AgentMediator());
            } else if (id == R.id.nav_live_chat) {
                //FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new LiveChat());

                try {
                    connectsendbird();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (id == R.id.nav_logout) {
                logout();
            } else if (id == R.id.nav_about_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new AboutFragment());
            } else if (id == R.id.nav_contact_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new ContactFragment());
            } else if (id == R.id.nav_privacy_policy) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                builder.setExitAnimations(this, R.anim.right_to_left_end, R.anim.left_to_right_end);
                builder.setStartAnimations(this, R.anim.left_to_right_start, R.anim.right_to_left_start);
                builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(PRIVACY_POLICY));
            }
        } else if (userType.equals(USER_COLLECTION_AGENT)) {
            if (id == R.id.nav_home) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new CollectionAgent());
            } else if (id == R.id.nav_booked_property) {
                //FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new ListOfBookedProperty());
            } else if (id == R.id.nav_live_chat) {
                //FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new LiveChat());
                try {
                    connectsendbird();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (id == R.id.nav_logout) {
                logout();
            } else if (id == R.id.nav_about_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new AboutFragment());
            } else if (id == R.id.nav_contact_us) {
                FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new ContactFragment());
            } else if (id == R.id.nav_privacy_policy) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                builder.setExitAnimations(this, R.anim.right_to_left_end, R.anim.left_to_right_end);
                builder.setStartAnimations(this, R.anim.left_to_right_start, R.anim.right_to_left_start);
                builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(PRIVACY_POLICY));
            }

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        action_settings = menu.findItem(R.id.action_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            MessageUtils.showToastMessage(getApplicationContext(), "Main Item Click");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void connectsendbird() throws Exception {
        if (Utils.haveNetworkConnection(MainActivity.this)) {

            dialog = MessageUtils.showDialog(this);
            try {

                ConnectionManager.inti(this);
                ConnectionManager.login(SessionManager.getMobile(this), new SendBird.ConnectHandler() {
                    @Override
                    public void onConnected(User user, SendBirdException e) {
                        if (e != null) {
                            Log.d("Connect", "" + e);
                        } else {

                            updateCurrentUserInfo(SessionManager.getName(MainActivity.this));
                            updateCurrentUserPushToken();


                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            MessageUtils.showSnackBarAction(MainActivity.this, drawer, getString(R.string.check_inter_net));
        }
    }


    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(MainActivity.this, null);
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            MainActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    //showSnackbar("Update user nickname failed");

                    return;
                }

                navigationView.getMenu().getItem(0).setChecked(true);
                MessageUtils.dissmiss(dialog);
                Intent intent = new Intent(getApplication(), GroupChannelActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }

    ArrayList<Fragment> fragmentArrayLis = new ArrayList<>();

    private void checkUserBasedUI(int i) {
        fragmentArrayLis.add(new Dashboard());
        String userType = SessionManager.getUserType(MainActivity.this);
        if (userType.equals(Utils.USER_CUSTOMER)) {
            FragmentCallUtils.passFragmentWithoutAnim(MainActivity.this, new Dashboard());
        }
    }

    private void logout() {

        final Dialog dialog = MessageUtils.showDialog(MainActivity.this);
        Call<ModelLogout> modelLogoutCall = Utils.getInstance(MainActivity.this).onCallLogout(SessionManager.getUserId(getApplicationContext()));
        modelLogoutCall.enqueue(new Callback<ModelLogout>() {
            @Override
            public void onResponse(Call<ModelLogout> call, Response<ModelLogout> response) {
                MessageUtils.dissmiss(dialog);
                try {
                    if (response.isSuccessful()) {
                        ModelLogout logout = response.body();
                        if (logout.getSuccess()) {
                            sessionManager.logoutUser(MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, "" + logout.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String msg = MessageUtils.setErrorMessage(response.code());
                        MessageUtils.showToastMessage(getApplicationContext(), msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ModelLogout> call, Throwable t) {
                MessageUtils.dissmiss(dialog);
                try {
                    String msg = MessageUtils.setFailurerMessage(MainActivity.this, t.getMessage());
                    MessageUtils.showToastMessage(getApplicationContext(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void setNavigationBar(String title, int selectPoistion) {
        navigationView.getMenu().clear();
        if (SessionManager.getUserType(MainActivity.this).toLowerCase().equals("Mediator".toLowerCase())) {
            navigationView.inflateMenu(R.menu.menu_mediator);
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        }

        getSupportActionBar().show();
        navigationView.getMenu().getItem(selectPoistion).setChecked(true);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(null);
        mToolbarTitle.setText(title);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.setDrawerIndicatorEnabled(true);
        if (action_settings != null) {
            if (title.equals("Home")) {
                action_settings.setVisible(false);
            } else {
                action_settings.setVisible(false);
                //action_settings.setVisible(true);
            }
        }
    }

    public void setNavigationBarDisable(String title) {
        getSupportActionBar().show();
        getSupportActionBar().setTitle(null);
        mToolbarTitle.setText(title);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (action_settings != null) {
            action_settings.setVisible(false);
        }
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void setDisableToolBar() {
        getSupportActionBar().hide();
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            View view = getCurrentFocus();
            if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
                int scrcoords[] = new int[2];
                view.getLocationOnScreen(scrcoords);
                float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                float y = ev.getRawY() + view.getTop() - scrcoords[1];
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                    ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }

}
