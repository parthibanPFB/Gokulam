package real.estate.gokulam.items.customer


import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import real.estate.gokulam.model.media.Image
import real.estate.gokulam.utils.ImageUtils
import real.estate.gokulam.utils.Utils.BASE_URL_MEDIA
import uk.co.senab.photoview.PhotoView


class ItemViewPagerImageView(var context: FragmentActivity, var images: List<Image>) : PagerAdapter() {


    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageUrl: String = BASE_URL_MEDIA + images.get(position).filename
        val layout = inflater.inflate(real.estate.gokulam.R.layout.item_view_pager_image, collection, false) as ViewGroup
        val pagerImage: PhotoView = layout.findViewById<PhotoView>(real.estate.gokulam.R.id.pagerImage)

        //Media.setImageFromUrl(pagerImage, imageUrl)//call to GlideApp or Picasso to load the image into the ImageView

        ImageUtils.setImageLiveRealSize(pagerImage, imageUrl, context)
        collection.addView(layout)
        return layout
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}