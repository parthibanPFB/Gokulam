package real.estate.gokulam.items.agent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.model.mediator.customerDetailsMediator.Customer
import real.estate.gokulam.utils.FragmentCallUtils
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.colletionAgent.AgentPropertyDetails

class ItemCustomerNamesCollectionAgent(var activity: FragmentActivity, var customerDetails: ArrayList<Customer>) : RecyclerView.Adapter<ItemCustomerNamesCollectionAgent.ItemHolderCollectionAgent>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemHolderCollectionAgent {
        return ItemHolderCollectionAgent(LayoutInflater.from(activity!!).inflate(R.layout.item_customer_in_mediator, null))
    }

    override fun getItemCount(): Int {
        return customerDetails.size
    }

    override fun onBindViewHolder(holder: ItemHolderCollectionAgent, i: Int) {
        holder.parent_lay.layoutParams = Utils.setParamsForListViewInAll(i, customerDetails.size, "c")

        /*val imgURL: String = Utils.BASE_URL_IMAGE + customerDetails[i].landdetail.layout_image
        ImageUtils.setImageLive(holder.land_img, imgURL, activity)*/
        holder.land_img.visibility = View.GONE
        holder.customer_name_in_mediatort.text = customerDetails[i].name
        holder.customer_address_in_mediator.text = customerDetails[i].address
        holder.parent_lay.setOnClickListener {

            val fragment = AgentPropertyDetails()
            val bundle: Bundle = Bundle()
            bundle.putString("id", customerDetails[i].id.toString())
            bundle.putString("customer_name", customerDetails[i].name)
            fragment.arguments = bundle
            FragmentCallUtils.passFragment(activity, fragment)

        }

        holder.call_img.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customerDetails[i].mobile_no))
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)

        }
    }


    class ItemHolderCollectionAgent(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parent_lay = itemView.findViewById<CardView>(R.id.parent_lay_cutomer_mediator)
        var land_img = itemView.findViewById<ImageView>(R.id.land_img_in_mediator)
        var call_img = itemView.findViewById<ImageView>(R.id.call_img)
        var customer_name_in_mediatort = itemView.findViewById<CustomTextView>(R.id.customer_name_in_mediator)
        var customer_address_in_mediator = itemView.findViewById<CustomTextView>(R.id.customer_address_in_mediator)
    }
}