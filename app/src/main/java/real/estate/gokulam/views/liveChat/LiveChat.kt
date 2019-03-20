package real.estate.gokulam.views.liveChat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import real.estate.gokulam.R
import real.estate.gokulam.views.MainActivity

/**
 * A simple [Fragment] subclass.
 *
 */
class LiveChat : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.live_chat))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_chat, container, false)
    }

}
