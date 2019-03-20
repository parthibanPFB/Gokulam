package real.estate.gokulam.views.registerLogin

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import customs.AsteriskPasswordTransformationMethod
import customs.CustomRadioButton
import kotlinx.android.synthetic.main.fragment_registration.*
import real.estate.gokulam.R
import real.estate.gokulam.model.register.ModelRegister
import real.estate.gokulam.model.register.mediatorNames.Mediator
import real.estate.gokulam.model.register.mediatorNames.ModelMediatorName
import real.estate.gokulam.utils.FragmentCallUtils
import real.estate.gokulam.utils.MessageUtils
import real.estate.gokulam.utils.MessageUtils.log
import real.estate.gokulam.utils.SessionManager
import real.estate.gokulam.utils.Utils
import real.estate.gokulam.utils.Utils.*
import real.estate.gokulam.views.MainActivity
import real.estate.gokulam.views.colletionAgent.CollectionAgent
import real.estate.gokulam.views.dashboard.Dashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

class RegistrationFragment : Fragment() {
    var snackbar: Snackbar? = null
    var dialog: Dialog? = null
    var checkMediator: String = ""
    var customeType: String = ""
    var mediatorName: String = ""

    var mediatorListValues: List<Mediator> = ArrayList<Mediator>()

    var sessionManager: SessionManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setNavigationBarDisable(getString(R.string.registration))
        if (MainActivity.action_settings != null) {
            MainActivity.action_settings.isVisible = false
        }
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(activity)

        confirm_password.transformationMethod = AsteriskPasswordTransformationMethod()
        password_rgs.transformationMethod = AsteriskPasswordTransformationMethod()

        /*  confirm_password.typeface = MessageUtils.setType(activity,Fonts.MEDIUM)
          password_rgs.typeface = MessageUtils.setType(activity,Fonts.MEDIUM)*/





        submit.setOnClickListener {
            if (Utils.haveNetworkConnection(activity)) {

                val id = rg.checkedRadioButtonId
                if (id != -1) {
                    val userType = rg.findViewById<CustomRadioButton>(id).text.toString()
                    if (userType.equals(activity!!.getString(R.string.customer))) {  // select customer
                        if (!checkMediator.isEmpty()) {
                            if (checkMediator.equals(activity!!.getString(R.string.mediator))) {   // if select mediator check below mediator Name also
                                //mediatorName = mediator_name_sp.selectedItem.toString()

                                if (!mediatorName.isEmpty()) {
                                    validationAll(userType, checkMediator, mediatorName)
                                } else {
                                    snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Select Mediator Name")
                                }
                            } else {
                                validationAll(userType, checkMediator, "")
                            }
                        } else {
                            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Select Lead Source")
                        }
                    } else {
                        validationAll(userType, "", "")  // if select Mediator
                    }
                } else {
                    snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Select User Type")
                }
            } else {
                snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, getString(R.string.check_inter_net))

            }
        }



        mediator_name_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                try {
                    if (mediatorListValues.size != 0) {
                        Toast.makeText(activity, " " + mediatorListValues[position].id, Toast.LENGTH_SHORT).show()
                        mediatorName = mediatorListValues[position].id.toString()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        rg.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = rg.findViewById<CustomRadioButton>(checkedId)
            customeType = radioButton.text.toString()
            if (customeType.equals(activity!!.getString(R.string.customer))) {
                customer_lay.visibility = View.VISIBLE
            } else {
                customer_lay.visibility = View.GONE
            }
        }


        rg_customer.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = rg_customer.findViewById<CustomRadioButton>(checkedId)
            checkMediator = radioButton.text.toString()
            if (checkMediator.equals(activity!!.getString(R.string.mediator))) {
                mediator_lay.visibility = View.VISIBLE
                //log("call_mediator_names")

                callMediatorNames()

            } else {
                mediator_lay.visibility = View.GONE
            }
        }


    }

    private fun callMediatorNames() {
        dialog = MessageUtils.showDialog(activity)
        //http://192.168.2.9:8000/api/real/mediatorlist
        val callModel = Utils.getInstance(activity).onCallMediatorName()
        callModel.enqueue(object : Callback<ModelMediatorName> {
            override fun onFailure(call: Call<ModelMediatorName>?, t: Throwable?) {
                Log.d("namess", "asasas fail")
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, msg)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ModelMediatorName>?, response: Response<ModelMediatorName>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response != null) {
                        if (response.isSuccessful) {
                            val mediatorNamesModel: ModelMediatorName = response.body()
                            if (mediatorNamesModel.success) {
                                Log.d("namess", "asasas")
                                Log.d("namess", "asasas" + mediatorNamesModel)
                                var count = 0
                                mediatorListValues = mediatorNamesModel.data
                                if (mediatorListValues != null) {
                                    count = mediatorListValues.size
                                }

                                if (count != 0) {
                                    val strings: MutableList<String> = ArrayList()
                                    for (items in mediatorListValues) {
                                        strings.add(items.name)
                                    }
                                    Log.d("stringssdsd", "" + strings.size)
                                    mediator_name_sp.adapter = ArrayAdapter<String>(activity, R.layout.simple_item, R.id.item_name, strings)
                                } else {
                                    MessageUtils.showSnackBar(activity, snack_view_rgs, "Mediator Not Found")
                                }
                            } else {
                                MessageUtils.showSnackBar(activity, snack_view_rgs, mediatorNamesModel.message)
                            }
                        } else {
                            val msg = MessageUtils.setErrorMessage(response.code())
                            MessageUtils.showToastMessage(activity, msg)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        )
    }

    private fun validationAll(userType: String, checkMediator: String, mediatorName: String) {
        val name = name.text.toString()
        val email = email.text.toString()
        val mobileNumber = mobileNumber.text.toString()
        val address = address.text.toString()
        val password = password_rgs.text.toString()
        val confirmPassword = confirm_password.text.toString()
        val aadharCard = aadhar_card_no.text.toString()
        val panCard = pan_card.text.toString()
        val accountNumber = account_number.text.toString()
        val ifscCode = ifsc_code.text.toString()
        val branch = branch.text.toString()
        val bankName = bank_name.text.toString()

        if (validation(userType, name, email, mobileNumber, address, password, confirmPassword, panCard, aadharCard, accountNumber, ifscCode, branch, bankName)) {
            val hashMap = HashMap<String, String>()
            hashMap["name"] = name
            hashMap["email"] = email
            hashMap["mobile_number"] = mobileNumber
            hashMap["address"] = address
            hashMap["password"] = password
            hashMap["confirm_password"] = confirmPassword
            hashMap["aadhar_card"] = aadharCard
            hashMap["pan_card"] = panCard
            hashMap["account_number"] = accountNumber
            hashMap["ifsc_code"] = ifscCode
            hashMap["branch"] = branch
            hashMap["bank_name"] = bankName
            hashMap["user_type"] = userType
            hashMap["check_mediator"] = checkMediator
            hashMap["mediator_name"] = mediatorName
            hashMap["token_reference"] = "token_reference"
            onCallBackRegister(hashMap)
        }
    }

    private fun onCallBackRegister(hashMap: HashMap<String, String>) {
        dialog = MessageUtils.showDialog(activity)
        val onRegister = Utils.getInstance(activity).onCallRegister(hashMap)
        onRegister.enqueue(object : Callback<ModelRegister> {
            override fun onFailure(call: Call<ModelRegister>?, t: Throwable?) {
                MessageUtils.dissmiss(dialog)
                try {
                    val msg = MessageUtils.setFailurerMessage(activity, t!!.message)
                    snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, msg)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onResponse(call: Call<ModelRegister>?, response: Response<ModelRegister>?) {
                MessageUtils.dissmiss(dialog)
                try {
                    if (response!!.isSuccessful) {
                        val modelRegister = response.body()
                        if (modelRegister.success) {
                            if (modelRegister.data != null) {
                                sessionManager?.createLoginSession(modelRegister.data.userdetail.name, modelRegister.data.userdetail.email, modelRegister.data.userdetail.mobile_no, modelRegister.data.userdetail.id.toString(), modelRegister.data.token, modelRegister.data.userdetail.user_type.toString())
                                MainActivity.mAppEmailID.text = modelRegister.data.userdetail.email
                                MainActivity.mAppUserName.text = modelRegister.data.userdetail.name
                                MessageUtils.showToastMessage(activity, modelRegister.message)

                                if (SessionManager.getUserType(activity!!).toLowerCase() == USER_CUSTOMER.toLowerCase()) {
                                    FragmentCallUtils.passFragmentWithoutAnim(activity!!, Dashboard())
                                } else if (SessionManager.getUserType(activity!!).toLowerCase() == USER_MEDIATOR.toLowerCase()) {
                                    FragmentCallUtils.passFragmentWithoutAnim(activity!!, Dashboard())
                                } else if (SessionManager.getUserType(activity!!).toLowerCase() == USER_COLLECTION_AGENT.toLowerCase()) {
                                    FragmentCallUtils.passFragmentWithoutAnim(activity!!, CollectionAgent())
                                }

                            } else {
                                snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "User Details not found")
                            }
                        } else {
                            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, modelRegister.message)
                        }
                    } else {
                        val msg = MessageUtils.setErrorMessage(response.code())
                        snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, msg)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })
    }

    private fun validation(userType: String, name: String, email: String, mobileNumber: String, address: String, password: String, confim_password: String, pan_card: String, aadhar_card: String, account_number: String, ifsc_code: String, branch: String, bank_name: String): Boolean {
        log(userType)
        if (name.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Name can't be empty")
            return false
        } else if (email.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Email Id can't be empty")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Enter valid Email ID")
            return false
        } else if (mobileNumber.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Mobile Number can't be empty")
            return false
        } else if (mobileNumber.length != 10) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Enter Valid Mobile Number ")
            return false
        } else if (address.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Address can't be empty")
            return false
        } else if (password.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Password can't be empty")
            return false
        } else if (confim_password.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Confirm Password can't be empty")
            return false
        } else if (confim_password != password) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Password not Matched")
            return false
        } else if (aadhar_card.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Aadhar Card Number can't be empty")
            return false
        } else if (pan_card.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "PAN Card Number can't be empty")
            return false
        } else if (bank_name.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Bank Name can't be empty")
            return false
        } else if (account_number.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Account Number can't be empty")
            return false
        } else if (branch.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "Branch Name can't be empty")
            return false
        } else if (ifsc_code.isEmpty()) {
            snackbar = MessageUtils.showSnackBar(activity, snack_view_rgs, "IFSC Code can't be empty")
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MessageUtils.dismissSnackBar(snackbar)
        MessageUtils.dissmiss(dialog)
    }
}