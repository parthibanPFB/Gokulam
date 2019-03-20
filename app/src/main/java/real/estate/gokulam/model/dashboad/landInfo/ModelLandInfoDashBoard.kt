package real.estate.gokulam.model.dashboad.landInfo

import java.io.Serializable

data class ModelLandInfoDashBoard(
        val `data`: Data,
        val message: String,
        val success: Boolean
) : Serializable