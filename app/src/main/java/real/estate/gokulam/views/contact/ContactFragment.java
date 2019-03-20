package real.estate.gokulam.views.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import real.estate.gokulam.views.MainActivity;
import real.estate.gokulam.R;


/**
 * Created by AND I5 on 28-09-2017.
 */
public class ContactFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((MainActivity) getActivity()).setNavigationBarDisable(getString(R.string.contact_us));
        if (MainActivity.action_settings != null) {
            MainActivity.action_settings.setVisible(false);
        }
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
