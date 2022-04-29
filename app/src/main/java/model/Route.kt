package model

import com.google.gson.JsonElement
import java.io.Serializable

data class Route(val coordinates : JsonElement,
                 val turnType : Int?,
                 val facilityType : String?,
                 val roadType : Int?,
                 val totalDistance : Int?,
                 val distance : Int?
                 ):Serializable {
}
