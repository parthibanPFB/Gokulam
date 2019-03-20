package real.estate.gokulam.items.customer

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import customs.CustomTextView
import real.estate.gokulam.R
import real.estate.gokulam.model.Landdetail
import real.estate.gokulam.model.Plotdetail
import real.estate.gokulam.utils.FragmentCallUtils
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.propertyInfo.PropertyInformation

class ItemLandProperty(var activity: FragmentActivity, var lisOfPeroperty: MutableList<Plotdetail>, var component1: MutableList<Landdetail>) : RecyclerView.Adapter<ItemLandProperty.HolderLandProperty>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HolderLandProperty {
        return HolderLandProperty(LayoutInflater.from(activity).inflate(R.layout.item_property, null))
    }

    override fun getItemCount(): Int {
        return lisOfPeroperty.size
    }

    override fun onBindViewHolder(holder: HolderLandProperty, i: Int) {

        holder.mPeropertyParentView.layoutParams = Utils.setParamsForGridView(i, lisOfPeroperty.size)
        holder.mPropertyName.text = "Plot No : " + lisOfPeroperty[i].plot_number
        holder.size_of_area.text = lisOfPeroperty[i].square_feet + "/ st"

        if (lisOfPeroperty[i].status.toLowerCase() == "booked".toLowerCase() || lisOfPeroperty[i].status.toLowerCase() == "Registered".toLowerCase()) {
            holder.background_color.setBackgroundColor(activity.getColor(R.color.colorPrimaryDark))
            holder.mPropertyName.setTextColor(activity.getColor(R.color.white))
            holder.size_of_area.setTextColor(activity.getColor(R.color.white))
        } else {
            holder.background_color.setBackgroundColor(activity.getColor(R.color.white))
            holder.mPropertyName.setTextColor(activity.getColor(R.color.black))
            holder.size_of_area.setTextColor(activity.getColor(R.color.black))
        }

        holder.mPeropertyParentView.setOnClickListener {

            if (lisOfPeroperty[i].status.toLowerCase() != "booked".toLowerCase() && lisOfPeroperty[i].status.toLowerCase() != "registered".toLowerCase()) {
                val fragment = PropertyInformation()
                val bundle = Bundle()
                bundle.putString("id", lisOfPeroperty[i].id.toString())
                fragment.arguments = bundle
                FragmentCallUtils.passFragment(activity, fragment)
            } else {
                MessageUtils.showToastMessage(activity, "Plot Already Booked")
            }
        }

        holder.size_of_area.setOnClickListener {

            if (lisOfPeroperty[i].status.toLowerCase() != "booked".toLowerCase() && lisOfPeroperty[i].status.toLowerCase() != "registered".toLowerCase()) {
                val fragment = PropertyInformation()
                val bundle = Bundle()
                bundle.putString("id", lisOfPeroperty[i].id.toString())
                fragment.arguments = bundle
                FragmentCallUtils.passFragment(activity, fragment)
            } else {
                MessageUtils.showToastMessage(activity, "Plot Already Booked")
            }
        }

        holder.mPeropertyParentView.setOnClickListener {

            if (lisOfPeroperty[i].status.toLowerCase() != "booked".toLowerCase() && lisOfPeroperty[i].status.toLowerCase() != "registered".toLowerCase()) {
                val fragment = PropertyInformation()
                val bundle = Bundle()
                bundle.putString("id", lisOfPeroperty[i].id.toString())
                fragment.arguments = bundle
                FragmentCallUtils.passFragment(activity, fragment)
            } else {
                MessageUtils.showToastMessage(activity, "Plot Already Booked")
            }
        }

    }

    class HolderLandProperty(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPropertyName = itemView.findViewById<CustomTextView>(R.id.property_name)
        var mPeropertyParentView = itemView.findViewById<CardView>(R.id.card_view)
        var size_of_area = itemView.findViewById<CustomTextView>(R.id.size_of_area)
        var background_color = itemView.findViewById<ConstraintLayout>(R.id.background_color)
    }

}