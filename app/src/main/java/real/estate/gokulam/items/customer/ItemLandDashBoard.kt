package real.estate.gokulam.items.customer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.model.Landdetail
import real.estate.gokulam.utils.FragmentCallUtils
import real.estate.gokulam.utils.ImageUtils
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.utils.Utils.BASE_URL_IMAGE
import real.estate.gokulam.views.Home

class ItemLandDashBoard(var activity: FragmentActivity, var listOfValues: List<Landdetail>/*, var snack_view_property: RelativeLayout, var snack_bar: Snackbar*/) : RecyclerView.Adapter<ItemLandDashBoard.HolderLandDetails>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HolderLandDetails {
        return HolderLandDetails(LayoutInflater.from(activity).inflate(R.layout.item_dashboard_land_details, null))
    }

    override fun getItemCount(): Int {
        return listOfValues.size
    }

    override fun onBindViewHolder(holder: HolderLandDetails, i: Int) {
        holder.parent_lay.layoutParams = Utils.setParamsForListViewInAll(i, listOfValues.size,"l")

        holder.placeName.text = listOfValues[i].layout_name
        holder.placeArea.text = listOfValues[i].area
        holder.placePrice.text = activity.getString(R.string.inr) + " " + listOfValues[i].price + "/ sf"
        val img = listOfValues[i].layout_image
        ImageUtils.setImageLive(holder.placeImg, BASE_URL_IMAGE + img, activity)

        if (listOfValues[i].status.equals("Pending")) {
            holder.placeSale.visibility = View.VISIBLE
        } else {
            holder.placeSale.visibility = View.INVISIBLE
        }
        holder.parent_lay.setOnClickListener {
            //if (Utils.haveNetworkConnection(activity)) {
                val fragment: Fragment = Home()
                val bundle = Bundle()
                bundle.putString("flatId", "" + listOfValues[i].id)
                bundle.putString("areaName", "" + listOfValues[i].layout_name)
                fragment.arguments = bundle
                FragmentCallUtils.passFragment(activity, fragment)
         /*   } else {
                //snack_bar = MessageUtils.showSnackBarAction(activity, snack_view_property, activity.getString(R.string.check_inter_net))
                MessageUtils.showToastMessage(activity, activity.getString(R.string.check_inter_net))
            }*/
        }
    }


    class HolderLandDetails(view: View) : RecyclerView.ViewHolder(view) {
        var placeName = view.findViewById<CustomTextView>(R.id.place_name)
        var placeArea = view.findViewById<CustomTextView>(R.id.place_area)
        var placePrice = view.findViewById<CustomTextView>(R.id.area_price)
        var placeSale = view.findViewById<CustomTextView>(R.id.for_sale)
        var placeImg = view.findViewById<ImageView>(R.id.land_img)
        var parent_lay = view.findViewById<LinearLayout>(R.id.parent_lay)
    }
}