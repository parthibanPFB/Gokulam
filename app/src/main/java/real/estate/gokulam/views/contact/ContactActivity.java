package real.estate.gokulam.views.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import real.estate.gokulam.R;
import real.estate.gokulam.utils.FragmentCallUtils;

/**
 * Created by AND I5 on 28-09-2017.
 */

public class ContactActivity extends AppCompatActivity {

    private ContactFragment fragment;

    public static void start(FragmentActivity activity) {
        activity.startActivity(new Intent(activity, ContactActivity.class));
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        //set Tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * Set Toolbar's title
         */
        TextView txtToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbarTitle.setText("Title");

        /**
         * To set Back arrow
         */

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (fragment == null) {
            fragment = new ContactFragment();
            //For Connecting model<-->presenter<-> view(fragment)
            //new BlogPresenter(new BlogModelImp(this), fragment);
            FragmentCallUtils.addFragmentToActivity(ContactActivity.this,getSupportFragmentManager(), fragment, R.id.container_body);
        }

    }

}
