package real.estate.gokulam.views.colletionAgent


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_collection_agent.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.agent.ItemCustomerNamesCollectionAgent
import real.estate.gokulam.model.mediator.customerDetailsMediator.Customer
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.MainActivity
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass. 29-02-2019
 *
 */
class CollectionAgent : Fragment() {


    var dialog: Dialog? = null
    var snackbar: Snackbar? = null
    var itemCustomerNamesCollectionAgent: ItemCustomerNamesCollectionAgent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBar(getString(real.estate.gokulam.R.string.collection_agent), 0)
        return inflater.inflate(R.layout.fragment_collection_agent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collection_customer_details?.layoutManager = GridLayoutManager(activity!!, 1)
        collection_customer_details?.setHasFixedSize(false)
    }


    override fun onResume() {
        super.onResume()
        if (Utils.haveNetworkConnection(activity)) {
            calledListOfRegisterUser()
        } else {
            no_colletion_list.visibility = View.VISIBLE
            collection_customer_details.visibility = View.GONE
            no_colletion_list.text = getString(R.string.check_inter_net)
        }
    }

    private fun calledListOfRegisterUser() {
        dialog = MessageUtils.showDialog(activity)
        val hashMap = HashMap<String, Any>()
        hashMap.put("collection_agent_id", SessionManager.getUserId(activity!!))
        Utils.getInstance(activity!!).callWebApi("customer_details", SessionManager.getToken(activity!!), hashMap).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                no_colletion_list.visibility = View.VISIBLE
                collection_customer_details.visibility = View.GONE
                MessageUtils.dissmiss(dialog)
                val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
//                snackbar = MessageUtils.showSnackBar(activity!!, mSanckView_collection_agen, msg)
                no_colletion_list.text = msg

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response != null) {
                        if (response.isSuccessful) {

                            no_colletion_list.visibility = View.GONE
                            collection_customer_details.visibility = View.VISIBLE
                            val listOfCustomer = ArrayList<Customer>()
                            itemCustomerNamesCollectionAgent = ItemCustomerNamesCollectionAgent(activity!!, listOfCustomer)
                            collection_customer_details?.adapter = itemCustomerNamesCollectionAgent


                        } else {
                            val msg = MessageUtils.setErrorMessage(response.code())
                            //snackbar = MessageUtils.showSnackBar(activity!!, mSanckView_collection_agen, msg)
                            no_colletion_list.visibility = View.VISIBLE
                            collection_customer_details.visibility = View.GONE
                            no_colletion_list.text = msg
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })
    }

}
