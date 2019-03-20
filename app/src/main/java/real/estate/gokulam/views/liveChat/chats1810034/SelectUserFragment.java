package real.estate.gokulam.views.liveChat.chats1810034;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sendbird.android.ApplicationUserListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import customs.CustomTextView;
import real.estate.gokulam.R;
import real.estate.gokulam.utils.SessionManager;

import static android.support.constraint.Constraints.TAG;


/**
 * A fragment displaying a list of selectable users.
 */
public class SelectUserFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SelectableUserListAdapter mListAdapter;

    private ApplicationUserListQuery mUserListQuery;
    private UsersSelectedListener mListener;
    private ArrayList<String> contacttemp = new ArrayList<>();
    CustomTextView mNoData;

    // To pass selected user IDs to the parent Activity.
    interface UsersSelectedListener {
        void onUserSelected(boolean selected, String userId);
    }

    static SelectUserFragment newInstance() {
        SelectUserFragment fragment = new SelectUserFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((CreateGroupChannelActivity) getActivity()).setTitle(getString(R.string.creating_group));
        View rootView = inflater.inflate(R.layout.chat_fragment_select_user, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_select_user);
        mNoData = rootView.findViewById(R.id.empty_textview);
        mListAdapter = new SelectableUserListAdapter(getActivity(), false, true);

        mListAdapter.setItemCheckedChangeListener(new SelectableUserListAdapter.OnItemCheckedChangeListener() {
            @Override
            public void OnItemChecked(User user, boolean checked) {
                if (checked) {
                    mListener.onUserSelected(true, user.getUserId());
                } else {
                    mListener.onUserSelected(false, user.getUserId());
                }
            }
        });

        mListener = (UsersSelectedListener) getActivity();

        setUpRecyclerView();

        loadInitialUserList(25);


        return rootView;
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
       /* mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mListAdapter.getItemCount() - 1) {
                    loadNextUserList(10);
                }
            }
        });*/
    }

    /**
     * Replaces current user list with new list.
     * Should be used only on initial load.
     */
    private void loadInitialUserList(int size) {
        mUserListQuery = SendBird.createApplicationUserListQuery();

        mUserListQuery.setLimit(size);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                Log.d(TAG, "" + list.size());
                if (list.size() != 0) {
                    settoContactToArrayList(list);
                } else {
                    if (contacttemp.size() != 0) {
                        mNoData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mNoData.setVisibility(View.VISIBLE);
                        mNoData.setText(getString(R.string.no_user_found));
                    }

                }
            }
        });
    }

    private void loadNextUserList(int size) {
        mUserListQuery.setLimit(size);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                /*for (User user : list) {
                    mListAdapter.addLast(user);
                }*/
                settoContactToArrayList(list);
            }
        });
    }


    private void settoContactToArrayList(List<User> list) {
        try {
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            while (phones.moveToNext()) {
                String username = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (phoneNumber.length() > 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
                }
                contacttemp.add(phoneNumber);
                Log.d("Contacts", username + "  " + phoneNumber);
            }
            phones.close();
            Iterator<User> iterator = list.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (SessionManager.getMobile(getActivity()).equals(user.getUserId())) {
                    iterator.remove();
                } else {
                   /* if (contacttemp.contains(user.getUserId())) {
                        Log.d(TAG, " if " + user.getUserId());
                    } else {
                        Log.d(TAG, "" + user.getUserId());
                        iterator.remove();
                    }*/
                }
            }
            if (list.size() > 1) {
                mListAdapter.setUserList(list);
            } else {
                //Toast.makeText(getActivity(), getString(R.string.no_user_found), Toast.LENGTH_LONG).show();


/*                if (contacttemp.size() != 0) {
                    mNoData.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {*/
                mNoData.setVisibility(View.VISIBLE);
                mNoData.setText(getString(R.string.no_user_found));
                mRecyclerView.setVisibility(View.GONE);
                //}

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
