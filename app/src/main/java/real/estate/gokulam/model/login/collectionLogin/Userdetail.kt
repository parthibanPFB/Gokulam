package real.estate.gokulam.model.login.collectionLogin

data class Userdetail(
        val active: Int,
        val address: String,
        val created_at: String,
        val email: String,
        val id: Int,
        val mobile_no: String,
        val name: String,
        val token_reference: String,
        val updated_at: String,
        val user_type: Int,
        val username: String
)