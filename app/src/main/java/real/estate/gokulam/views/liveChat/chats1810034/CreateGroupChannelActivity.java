package real.estate.gokulam.views.liveChat.chats1810034;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBirdException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import real.estate.gokulam.R;
import real.estate.gokulam.utils.ImageUtils;
import real.estate.gokulam.utils.MessageUtils;
import real.estate.gokulam.utils.PathUtil;

/**
 * An Activity to create a new Group Channel.
 * First displays a selectable list of users,
 * then shows an option to create a Distinct channel.
 */

public class CreateGroupChannelActivity extends AppCompatActivity implements SelectUserFragment.UsersSelectedListener {

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";

    static final int STATE_SELECT_USERS = 0;
    static final int STATE_SELECT_DISTINCT = 1;
    View groupDialogView;
    RelativeLayout cardView;
    Dialog dialog;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private Button mNextButton;

    private ImageView mNextimg;
    ImageView imgGroupImage, imgBack;
    AlertDialog.Builder builder;
    Uri imageUri = null;
    private List<String> mSelectedIds;
    private boolean mIsDistinct = true;

    private int mCurrentState;
    private FloatingActionButton mAddnewContact;

    private Toolbar mToolbar;
    private String TAG = "CreateGroup";
    private TextView toolbartitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_activity_create_group_channel);
        getSupportActionBar().hide();
        mSelectedIds = new ArrayList<>();

        if (savedInstanceState == null) {
            Fragment fragment = SelectUserFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_create_group_channel, fragment)
                    .commit();
        }

        mNextimg = findViewById(R.id.button_create_group_channel_create);
        imgBack = findViewById(R.id.ic_backimg);
        toolbartitle = findViewById(R.id.toolbar_title);


        mNextimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_SELECT_USERS) {
//                if (mCurrentState == STATE_SELECT_DISTINCT) {
//                    mIsDistinct = PreferenceUtils.getGroupChannelDistinct();
                    geticonandname(mSelectedIds, mIsDistinct);

                }
            }
        });
     /*   mAddnewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateGroupChannelActivity.this,CreateOpenChannelActivity.class));
            }
        });*/
        mNextimg.setVisibility(View.GONE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_create_group_channel);
      /*  setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }*/

    }

    private void geticonandname(final List<String> mSelectedIds, final boolean mIsDistinct) {
        try {
          /*  builder =new AlertDialog.Builder(this);
            builder.setCancelable(false);
            dialog  =builder.create();
            LayoutInflater  layoutInflater = LayoutInflater.from(this);
            groupDialogView = layoutInflater.inflate(R.layout.chat_dialog_upload_gruop_name,null,false);
            dialog.setView(groupDialogView);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();*/
            final FragmentActivity activity =this;
            final Dialog dialog1 = new Dialog(this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.chat_dialog_upload_gruop_name);
            dialog1.getWindow().setBackgroundDrawableResource(R.drawable.rounded_rect_border);
            dialog1.show();

            TextView cancel, create;
            RelativeLayout imgrelativelayou;

            final EditText edtGroupName;

            imgrelativelayou = dialog1.findViewById(R.id.imge_relativelayout);
            imgGroupImage = dialog1.findViewById(R.id.image_view_profile_dialog);
            cardView = dialog1.findViewById(R.id.paren_layout);
            cancel = dialog1.findViewById(R.id.btn_cancel);
            create = dialog1.findViewById(R.id.btn_create);
            edtGroupName = dialog1.findViewById(R.id.edt_group_name);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                }
            });
            imgrelativelayou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReasonforGetPermissions();
//                    requestPermission();
                }
            });
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (validate(edtGroupName.getText().toString(), imageUri)) {
                            if (dialog1 != null && dialog1.isShowing()) {
                                dialog1.dismiss();
                            }
                           dialog = MessageUtils.showDialog(activity);
                            creategroupChats(mSelectedIds, mIsDistinct, edtGroupName.getText().toString(), imageUri);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
//            createGroupChannel(mSelectedIds, mIsDistinct);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showReasonforGetPermissions() {
        try{
            final Dialog confirmation = new Dialog(this);
            confirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
            confirmation.setCancelable(false);
            confirmation.setContentView(R.layout.dialog_confirmation_permission);
            confirmation.getWindow().setBackgroundDrawableResource(R.drawable.rounded_rect_border);
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                    PackageManager.PERMISSION_GRANTED
                            ){
                        confirmation.show();
                        TextView title;
                        TextView reason1;
                        TextView reason2;
                        TextView accept;
                                title = confirmation.findViewById(R.id.permission_title);
                        reason1 = confirmation.findViewById(R.id.reason_of_permis1);
                        reason2 = confirmation.findViewById(R.id.reason_of_permis2);
                        accept = confirmation.findViewById(R.id.confirmation_granted);
                        title.setText(getString(R.string.permissionfor_storage));
                        reason1.setText(getString(R.string.reason_for_storag));
                        reason2.setText( getString(R.string.reason_for_storage));
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(confirmation!=null && confirmation.isShowing()){
                                    confirmation.dismiss();
                                }
                                requestPermission();
                            }
                        });
                    }else {
                        requestPermission();
                    }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void creategroupChats(List<String> mSelectedIds, boolean mIsDistinct, String groupname, Uri imageUri) {
        try {
            GroupChannelParams params = new GroupChannelParams()
                    .setPublic(false)
                    .setEphemeral(false)
                    .setDistinct(mIsDistinct)
                    .addUserIds(mSelectedIds)
//                    .setOperatorIds(operatorIds)
                    .setName(groupname)
                    .setCoverImage(new File(PathUtil.getPath(this, imageUri)));    // or .setCoverUrl(COVER_URL)
                    /*.setData(DATA)
                    .setCustomType(CUSTOM_TYPE);*/

            GroupChannel.createChannel(params, new GroupChannel.GroupChannelCreateHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e != null) {
                        // Error.
                        Log.d(TAG, "" + e);
                        return;
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validate(String gruopname, Uri imageUri) {
        try {
            if (gruopname.isEmpty()) {
                MessageUtils.showSnackBar(this, cardView, "Group Name Can't be Empty");
                return false;
            } else if (imageUri == null) {
                MessageUtils.showSnackBar(this, cardView
                        , "Group Icon Can't be Empty");
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
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

    void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
            mNextimg.setVisibility(View.GONE);
            mNextButton.setVisibility(View.GONE);
//            mCreateButton.setVisibility(View.GONE);
//            mNextButton.setVisibility(View.VISIBLE);
        } else if (state == STATE_SELECT_DISTINCT) {
            mCurrentState = STATE_SELECT_DISTINCT;
            mNextimg.setVisibility(View.GONE);
            mNextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserSelected(boolean selected, String userId) {
        if (selected) {
            mSelectedIds.add(userId);
        } else {
            mSelectedIds.remove(userId);
        }

        if (mSelectedIds.size() > 1) {
            mNextimg.setVisibility(View.VISIBLE);
//            mNextButton.setEnabled(true);
        } else {
            mNextimg.setVisibility(View.GONE);
//            mNextButton.setEnabled(false);
        }
    }


    /**
     * Creates a new Group Channel.
     * <p>
     * Note that if you have not included empty channels in your GroupChannelListQuery,
     * the channel will not be shown in the user's channel list until at least one message
     * has been sent inside.
     *
     * @param userIds  The users to be members of the new channel.
     * @param distinct Whether the channel is unique for the selected members.
     *                 If you attempt to create another Distinct channel with the same members,
     *                 the existing channel instance will be returned.
     */
    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                Log.d(TAG, "" + groupChannel.getCustomType());
                Log.d(TAG, "" + groupChannel.getMemberCount());

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            callintenttoread();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
//            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
                callintenttoread();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    callintenttoread();
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void callintenttoread() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "" + requestCode + "" + resultCode);
        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "" + requestCode + "" + data.getData().toString());
            try {
                imageUri = data.getData();
                ImageUtils.displayRoundImageFromUrl(this, imageUri.toString(), imgGroupImage);
            /*    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                  final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgGroupImage.setImageBitmap(selectedImage);*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

        }
    }


    public void setTitle(String title) {
        try {
            toolbartitle.setText(title);
            imgBack.setOnClickListener(new View.OnClickListener() {
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
