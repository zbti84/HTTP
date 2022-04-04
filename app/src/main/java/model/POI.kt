package model

import java.io.Serializable

data class POI (var name : String,
                var frontLat : String,  //시설물 입구 위도
                var frontLon : String):Serializable  //시설물 입구 경도
{}