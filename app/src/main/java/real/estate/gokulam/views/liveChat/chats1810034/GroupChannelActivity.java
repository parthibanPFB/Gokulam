package real.estate.gokulam.views.liveChat.chats1810034;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.sendbird.android.SendBird;
        import com.sendbird.android.SendBirdException;
        import com.sendbird.android.User;

        import real.estate.gokulam.R;
        import real.estate.gokulam.chat.chatUtils.BaseApplication;
        import real.estate.gokulam.localdatabase.MyDBHandler;
        import real.estate.gokulam.utils.MessageUtils;
        import real.estate.gokulam.utils.SessionManager;
        import real.estate.gokulam.views.liveChat.chats1810034.customuserlist.UserListActivity;


public class GroupChannelActivity extends AppCompatActivity {


    private boolean doubleBackToExitPressedOnce = false;
    private ImageView backIcon, menuimg;
    private TextView title, lastseenoract;

    MyDBHandler myDBHandler;
    boolean isConnected;
    String channelUrl;
    String channelname;
    View snackView;
    int channelID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_group_channel);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        myDBHandler = new MyDBHandler(getApplicationContext());
        connectsendbird();
        Log.d("Chartdsd", "0");
        getSupportActionBar().hide();

/*
        if (savedInstanceState == null) {
            // Load list of Group Channels

            Fragment fragment = GroupChannelListFragment.newInstance();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }*/

        try {
            channelUrl = getIntent().getStringExtra("groupChannelUrl");
            channelname = getIntent().getStringExtra("groupChannelname");
            channelID = getIntent().getIntExtra("groupChannelID", 99);
//            Log.d("Chartdsd", "0  " + channelUrl + " name \n " + channelname + "\n  ID   " + channelID);


//            if (connected) {
            Log.d("Chartdsd", "" +channelUrl);
            if (channelUrl != null && !channelUrl.equals("group")) {
                // If started from notification
                Fragment fragment = GroupChatFragment.newInstance(channelUrl);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .addToBackStack(null)
                        .commit();
            }else {
                Fragment fragment = GroupChannelListFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .addToBackStack(null)
                        .commit();
            }
            myDBHandler.truncate();
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        backIcon = findViewById(R.id.back_chats);
        title = findViewById(R.id.chat_title);
        lastseenoract = findViewById(R.id.chat_lastseen);
        menuimg = findViewById(R.id.menu_settings);
        snackView = findViewById(R.id.container_group_channel);

    }


    interface onBackPressedListener {
        boolean onBack();
    }

    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }

        Fragment fragmentManager = getSupportFragmentManager().findFragmentById(R.id.container_group_channel);
        if (fragmentManager instanceof GroupChannelListFragment) {
            finish();
        } else if (fragmentManager instanceof GroupChatFragment) {
            Fragment fragment = new GroupChannelListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            super.onBackPressed();
        }
    }

    private void checkbackpress() {
        try {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            doubleBackToExitPressedOnce = true;

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void settitle(String titile) {
        try {
            lastseenoract.setVisibility(View.GONE);
            menuimg.setVisibility(View.GONE);
            title.setText(titile);
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setchatsList(String titile) {
        try {
            menuimg.setVisibility(View.VISIBLE);
            title.setText(titile);
            lastseenoract.setVisibility(View.GONE);
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            menuimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GroupChannelActivity.this, SettingsActivity.class));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void settitlelastseen(String titile, String lastseen) {
        Log.d("LASTSEEN", " 22 " + lastseen);
        try {
            lastseenoract.setVisibility(View.VISIBLE);
            menuimg.setVisibility(View.GONE);
            title.setText(titile);
            lastseenoract.setText(lastseen);
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public  boolean connectsendbird() {
        Log.d("ReConnectSendBird", " sessionManager " + SessionManager.getMobile(getApplication()) + " \n\n ID \n " + BaseApplication.APP_ID);
        try {
            SendBird.init(BaseApplication.APP_ID, getApplication());
            SendBird.connect(SessionManager.getMobile(getApplication()), new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {
                        Log.d("ReConnectSendBird ", "" + e);
                        isConnected = false;
                    } else {
                        Log.d("ReConnectSendBird", "" + SendBird.getCurrentUser().getUserId());
                        isConnected = true;
                    }
                }
            });
        } catch (Exception cv) {
            cv.printStackTrace();
        }
        return isConnected;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 123:
                if (grantResults ==null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Contacts", "permission requested");
                    ActivityCompat.requestPermissions(this, new String [] {android.Manifest.permission.READ_CONTACTS} , 123);
                } else {
                    Log.d("Contacts", "permission Allowed");
                    Fragment fragment = new UserListActivity();
                    FragmentManager manager =getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.container_group_channel, fragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;

        case 13:
        if (grantResults ==null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("Contacts", "permission requested");
           requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    13);
        } else {
            MessageUtils.showSnackBar(this,snackView,"Now Select Image");
        }
        break;
    }
    }
}
