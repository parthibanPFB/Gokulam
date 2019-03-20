package real.estate.gokulam.model.property.property_1

import real.estate.gokulam.model.Landdetail
import real.estate.gokulam.model.Plotdetail

data class Data(
        val landdetails: List<Landdetail>,
        val plotdetails: List<Plotdetail>
)