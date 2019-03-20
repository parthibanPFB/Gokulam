package real.estate.gokulam.views.mediator


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.gesture.GestureOverlayView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.gson.Gson
import customs.CustomTextView
import kotlinx.android.synthetic.main.fragment_agent_mediator.*
import kotlinx.android.synthetic.main.fragment_view_customer_details.*
import kotlinx.android.synthetic.main.fragment_view_register_and_due_dates.*
import kotlinx.android.synthetic.main.view_property_info_details_more.*
import okhttp3.ResponseBody
import real.estate.gokulam.R
import real.estate.gokulam.items.ItemDueHistory
import real.estate.gokulam.model.payment.ModelPaidSuccess
import real.estate.gokulam.model.payment.paymentHistory.ModelPaymentHistory
import real.estate.gokulam.utils.ImageUtils
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.views.MainActivity
import real.estate.gokulam.views.propertyInfo.media.SitePhotos
import real.estate.gokulam.views.propertyInfo.media.SiteVideos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * A simple [Fragment] subclass. at 01-03-2019
 *
 */
class ViewCustomerPropertyDetailsFromMediator : Fragment() {

    var dialog: Dialog? = null
    var snackBar: Snackbar? = null
    var modelPaymentHistory: ModelPaymentHistory? = null
    var flatId: String = ""
    var latLang: String = ""
    var adapter: ItemDueHistory? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.property_details))
        return inflater.inflate(R.layout.fragment_view_customer_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        flatId = arguments!!.getString("id")
        super.onViewCreated(view, savedInstanceState)
        no_of_payment_details?.layoutManager = GridLayoutManager(activity, 1) as RecyclerView.LayoutManager?
        no_of_payment_details?.setHasFixedSize(true)
        no_of_payment_details?.visibility = View.VISIBLE

        onCallBookedPropertyInfo()

        expandable?.setOnClickListener {
            Log.d("Sdsd", "" + adapter?.itemCount)
            try {
                if (adapter != null) {
                    if (adapter?.itemCount != 0) {
                        if (due_details_lay?.visibility == View.VISIBLE) {
                            due_details_lay?.visibility = View.GONE
                            total_due?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                        } else {
                            due_details_lay?.visibility = View.VISIBLE
                            total_due?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)

                        }
                    } else {
                        MessageUtils.showSnackBar(activity!!, view_due_details, "Payment Details Not Found")
                    }
                } else {
                    MessageUtils.showSnackBar(activity!!, snac_view_pay, "Payment Details Not Found")
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                MessageUtils.showSnackBar(activity!!, view_due_details, "Payment Details Not Found")
            }
        }

        paid?.setOnClickListener {

            showSignDialog()
        }


        images_mediator.setOnClickListener {
            val intent = Intent(activity, SitePhotos::class.java)
            intent.putExtra("landId", flatId)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
        }

        video_mediator.setOnClickListener {
            val intent = Intent(activity, SiteVideos::class.java)
            intent.putExtra("landId", flatId)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
        }

        map_mediator.setOnClickListener {
            if (!latLang.isEmpty() && latLang != ",") {
                Log.d("latLangsdsd", "" + latLang)
                val mapa = "http://maps.google.com/maps?q=loc:$latLang"
                //Uri gmmIntentUri = Uri.parse("geo:11.247268683, 77.004409&q=11.247268683, 77.004409");
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapa))
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
                activity!!.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
            } else {
                snackBar = MessageUtils.showSnackBar(activity, view_due_details, "Location Not Found")
            }
        }
    }


    @SuppressLint("WrongViewCast")
    private fun showSignDialog() {

        val mDialog = Dialog(activity!!)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.signature)
        mDialog.setCancelable(false)
        mDialog.window!!.setBackgroundDrawableResource(R.color.dialog_trans)

        mDialog.show()

        val mCancel = mDialog.findViewById(R.id.CancelButton) as CustomTextView
        val mDoneBtn = mDialog.findViewById(R.id.DoneButton) as CustomTextView
        val gestureView = mDialog.findViewById(R.id.signaturePad) as GestureOverlayView
        val sign_sanck = mDialog.findViewById(R.id.sign_sanck) as CardView
        mDoneBtn.setOnClickListener(View.OnClickListener {

            gestureView.setDrawingCacheEnabled(true)
            Log.d("dskdlskdls", "" + gestureView.getGesture())
            if (gestureView.getGesture() == null) {
                //MessageUtils.showToastMessage(activity, "Please add your sign.")

                MessageUtils.showSnackBar(activity, sign_sanck, "Please add Sign")
            } else {
                val bm = Bitmap.createBitmap(gestureView.getDrawingCache())
                //signImage.setImageBitmap(bm)
                //ignImage.setVisibility(View.VISIBLE)
                //mDialog.dismiss()
                val signImg = convertBase64(bm)


                callCollectedAmount(signImg, modelPaymentHistory!!.data.plotdetails.id, mDialog, sign_sanck)

            }
        })

        mCancel.setOnClickListener(View.OnClickListener {
            gestureView.setDrawingCacheEnabled(true)
            val bm = Bitmap.createBitmap(gestureView.getDrawingCache())
            /*signImage.setImageBitmap(bm)
            signImage.setImageDrawable(null)
            signImage.setVisibility(View.GONE)*/
            mDialog.dismiss()
        })
    }

    private fun callCollectedAmount(signImg: String, id: Int, mDialog: Dialog, sign_sanck: CardView) {
        dialog = MessageUtils.showDialog(activity!!)
        val hashMap = HashMap<String, Any>()
        hashMap["sign_image"] = signImg
        hashMap["id"] = id
        hashMap["mediator_id"] = SessionManager.getUserId(activity)
        Utils.getInstance(activity!!).callWebApi("payment_collection_mediator", SessionManager.getToken(activity!!), hashMap).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    customer_and_land_info_no?.visibility = View.VISIBLE
                    customer_and_land_info_no?.text = msg
                    view_all_customer_land_details?.visibility = View.GONE
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    snackBar = MessageUtils.showSnackBar(activity, sign_sanck, ex.message)
                }

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelPaidSuccess = Gson().fromJson<ModelPaidSuccess>(response.body().string(), ModelPaidSuccess::class.java)
                        if (modelPaymentHistory!!.success) {
                            mDialog.dismiss()
                        } else {
                            snackBar = MessageUtils.showSnackBar(activity, sign_sanck, modelPaymentHistory!!.message)
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        snackBar = MessageUtils.showSnackBar(activity, sign_sanck, msg)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    snackBar = MessageUtils.showSnackBar(activity, sign_sanck, ex.message)
                }
            }

        })

    }

    private fun convertBase64(bm: Bitmap): String {

        //createDirectoryAndSaveFile(bm, "" + System.currentTimeMillis() + "Normal.jpg");

        val baos = ByteArrayOutputStream()

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        //  Log.d("imageStringFrom", "" + imageString);

        Log.d("imageStringsdsd", "" + imageString)

        /*ContentValues contentValues = new ContentValues();
        contentValues.put("idProofName", "");
        contentValues.put("idProofID", "");
        contentValues.put("idProofImage", "" + imageString);
        contentValues.put("idProofRefID", "4");
        contentValues.put("idProofType", "E_SIGN");*/

        //screen5.onInsert(contentValues);


        imageBytes = Base64.decode(imageString, Base64.NO_WRAP)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        //        createDirectoryAndSaveFile(decodedImage, "" + System.currentTimeMillis() + "Base64.jpg");

        //signImage.setImageBitmap(decodedImage)
        return imageString;

    }


    private fun onCallBookedPropertyInfo() {
        val hashMap = HashMap<String, Any>()
        hashMap["user_id"] = SessionManager.getUserId(activity)
        hashMap["id"] = flatId

        dialog = MessageUtils.showDialog(activity)
        val modelProperty: Call<ResponseBody> = Utils.getInstance(activity).callWebApi("plotbookingdetail", SessionManager.getToken(activity), hashMap)
        modelProperty.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                    customer_and_land_info_no?.visibility = View.VISIBLE
                    customer_and_land_info_no?.text = msg
                    view_all_customer_land_details?.visibility = View.GONE
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }


            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        modelPaymentHistory = Gson().fromJson<ModelPaymentHistory>(response.body().string(), ModelPaymentHistory::class.java)
                        if (modelPaymentHistory!!.success) {
                            if (modelPaymentHistory!!.data.plotdetails != null) {
                                booked_list_in_mediator?.visibility = View.VISIBLE
                                customer_and_land_info_no?.visibility = View.GONE
                                Log.d("iddiddidi ", "" + modelPaymentHistory!!.data.plotdetails.id + " no_Of_payment " + modelPaymentHistory!!.data.payments.size)


                                latLang = modelPaymentHistory!!.data.plotdetails.landdetail.lattitude + "," + modelPaymentHistory!!.data.plotdetails.landdetail.longitude
                                var paymentCount: Int = 0

                                if (modelPaymentHistory!!.data.payments != null) {
                                    paymentCount = modelPaymentHistory!!.data.payments.size
                                }

                                if (paymentCount != 0) {
                                    Collections.reverse(modelPaymentHistory!!.data.payments)
                                    adapter = ItemDueHistory(activity!!, modelPaymentHistory!!.data.payments)
                                    no_of_payment_details?.adapter = adapter

                                }

                                total_due?.text = Math.round(modelPaymentHistory!!.data.plotdetails.no_of_years * 12).toInt().toString()
                                no_of_due?.text = paymentCount.toString()

                                amount.text = modelPaymentHistory!!.data.plotdetails.amount.toString() + " /" + modelPaymentHistory!!.data.plotdetails.square_feet + " sf"
                                ImageUtils.setImageLive(plot_image, Utils.BASE_URL_IMAGE + modelPaymentHistory!!.data.plotdetails.plot_image, activity)
                                plot_no?.text = "Plot No : " + modelPaymentHistory!!.data.plotdetails.plot_number

                                land_name?.text = modelPaymentHistory!!.data.plotdetails.landdetail.layout_name
                                area_name?.text = modelPaymentHistory!!.data.plotdetails.landdetail.area + ", " + modelPaymentHistory!!.data.plotdetails.landdetail.layout_address
                                if (!modelPaymentHistory!!.data.plotdetails.facing.isEmpty() && modelPaymentHistory!!.data.plotdetails.facing != null) {
                                    facing?.text = "  " + modelPaymentHistory!!.data.plotdetails.facing
                                } else {
                                    facing?.visibility = View.GONE
                                }
                                booking_amount?.text = getString(R.string.inr) + " " + modelPaymentHistory!!.data.plotdetails.landdetail.booking_amount
                                opening_date?.text = modelPaymentHistory!!.data.plotdetails.landdetail.opening_date
                                registerNo?.text = modelPaymentHistory!!.data.plotdetails.landdetail.registeration_number.trim()
                                registerDate?.text = modelPaymentHistory!!.data.plotdetails.landdetail.registeration_date
                            } else {
                                view_all_customer_land_details?.visibility = View.GONE
                                customer_and_land_info_no?.visibility = View.VISIBLE
                                customer_and_land_info_no?.text = "No data found"
                            }
                        } else {
                            view_all_customer_land_details?.visibility = View.GONE
                            customer_and_land_info_no?.visibility = View.VISIBLE
                            customer_and_land_info_no?.text = modelPaymentHistory!!.message
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        //snackbar = MessageUtils.showSnackBar(activity, snack_view_property, msg)
                        customer_and_land_info_no?.text = msg
                        customer_and_land_info_no?.visibility = View.VISIBLE
                        view_all_customer_land_details?.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    customer_and_land_info_no?.text = ex.message
                    customer_and_land_info_no?.visibility = View.VISIBLE
                    view_all_customer_land_details?.visibility = View.GONE
                    ex.printStackTrace()

                }
            }
        })
    }
}
