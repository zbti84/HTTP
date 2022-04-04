package retrofit

import android.util.Log
import com.google.gson.JsonElement
import model.POI
import retrofit2.Call
import retrofit2.Response
import utils.Constant

class RetrofitManager {

    companion object{
        val instance=RetrofitManager()  //자기 자신을 싱글턴으로 만들어냄
    }

    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(Constant.API.BASE_URL)?.create(IRetrofit::class.java)

    //명칭검색 api호출
    fun searchPOI(searchKeyword:String?,completion:(Constant.RESPONSE_STATE,ArrayList<POI>?)->Unit){

        //언랩핑작업 optional설정 때문에 Manager의 searchPOI의 keyword와 인터페이스의 keyword의 optional차이
        val keyword = searchKeyword.let {
            it  //keyword
        }?: ""

        val call = iRetrofit?.searchPOI(searchKeyword = keyword).let {
            it  //call
        }?: return


        //본격적인 요청
        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("로그", "RetrofitManager - onFailure() called / t: $t")

                completion(Constant.RESPONSE_STATE.FAIL, null)
                //성공실패여부와 결과값을 같이 보냄
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("로그", "RetrofitManager - onResponse() 전체 응답 : ${response.body()}")

                when(response.code()){
                    200->{ //응답코드가 200일때만 작동할 수 있게 예를 들어 응답이 없는 경우는 작동하지 않음.
                        response.body()?.let{//body가 있다면

                            var parsePOIDataArray = ArrayList<POI>() //model.POI데이터를 받아 넣는 리스트

                            val body = it.asJsonObject

                            val searchPoiInfo=body.get("searchPoiInfo").asJsonObject

                            val total = searchPoiInfo.get("totalCount").asInt

                            Log.d("로그", "RetrofitManager - onResponse() total: $total")

                            // 데이터가 없으면 no_content 로 보낸다.
                            if(total==0){
                                completion(Constant.RESPONSE_STATE.NO_CONTENT, null)
                            }
                            else{ // 데이터가 있다면

                                val pois = searchPoiInfo.get("pois").asJsonObject

                                val poi = pois.getAsJsonArray("poi")

                                poi.forEach { poiItem ->
                                    val poiItemObject = poiItem.asJsonObject
                                    val name = poiItemObject.get("name").asString
                                    val frontLat= poiItemObject.get("frontLat").asString
                                    val frontLon= poiItemObject.get("frontLon").asString

                                    val POIItem = POI(
                                        name=name,
                                        frontLat=frontLat,
                                        frontLon=frontLon
                                    )
                                    parsePOIDataArray.add(POIItem)
                                }
                                Log.d("로그", "RetrofitManager - onResponse() 필요응답 : ${parsePOIDataArray}")
                                completion(Constant.RESPONSE_STATE.OKAY,parsePOIDataArray)
                            }
                        }
                    }
                }
            }

        })

    }




}