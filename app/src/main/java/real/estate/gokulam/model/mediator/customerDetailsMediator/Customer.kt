package real.estate.gokulam.model.mediator.customerDetailsMediator

data class Customer(
        val aadhar_no: String,
        val active: String,
        val address: String,
        val bank_account_no: String,
        val bank_name: String,
        val branch_name: String,
        val check_mediator: String,
        val created_at: String,
        val email: String,
        val id: Int,
        val ifsc_code: String,
        val mediator_name: String,
        val mobile_no: String,
        val name: String,
        val otp: String,
        val pan_no: String,
        val password: String,
        val remember_token: Any,
        val token_reference: String,
        val updated_at: String,
        val user_type: String,
        val username: String
)