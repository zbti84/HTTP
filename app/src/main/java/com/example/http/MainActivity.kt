package com.example.http

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.http.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit.RetrofitManager
import utils.Constant

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var button:Button
    private lateinit var button2:Button

    var location : String = "한성대입구역"

    var startx=127.0690745902964
    var starty=37.83296140345568
    var endx=127.0617749485774
    var endy=37.84368779816498
    var startname="%EC%B6%9C%EB%B0%9C"
    var endname="%EB%B3%B8%EC%82%AC"

    var searchOption=0


    //긴 로그 출력
    fun LogLineBreak(str: String) {
        if (str.length > 3000) {    // 텍스트가 3000자 이상이 넘어가면 줄
            Log.d("로그long", str.substring(0, 3000))
            LogLineBreak(str.substring(3000))
        } else {
            Log.d("로그long", str)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button=findViewById(R.id.Button)
        button2=findViewById(R.id.Button2)

        button.setOnClickListener {
            Log.d("로그", "MainActivity - POI 버튼이 클릭되었다. /")

            // 검색 api 호출
            //manager에서 인터페이스를 가져오고 호출함수 사용하고
            RetrofitManager.instance.searchPOI(searchKeyword = location,completion = {
                    responseState, parsePOIDataArray ->

                when(responseState){
                    Constant.RESPONSE_STATE.OKAY->{  //만약 STATE가 OKEY라면
                        Log.d("로그", " POI api호출 성공")
                        if (parsePOIDataArray != null) {
                            Log.d("결과값", "${parsePOIDataArray.get(0).name}")
                        }
                        //그냥 parsePOIDataArray.get(0).name 을 치고 Alt+Enter을 하면 가장 위에 것을 선택하면 오류해결

                    }
                    Constant.RESPONSE_STATE.FAIL->{//만약 STATE가 FAIL라면
                        Log.d("로그", " POIapi호출 실패")
                    }
                    Constant.RESPONSE_STATE.NO_CONTENT->{//만약 NO_CONTENT가 FAIL라면
                        Log.d("로그", " POI 결과가 없습니다.")
                    }
                }
            })
        }

        button2.setOnClickListener{
            Log.d("로그", "MainActivity - ROUTE 버튼이 클릭되었다. /")

            // 길찾기 호출
            //manager에서 인터페이스를 가져오고 호출함수 사용하고
            RetrofitManager.instance.searchRoute(startX = startx,
                startY = starty,
                endX = endx,
                endY = endy,
                startname = startname,
                endname = endname,
                searchOption=searchOption,
                completion = {
                        responseState, parseRouteDataArray ->

                    when(responseState){
                        Constant.RESPONSE_STATE.OKAY->{  //만약 STATE가 OKEY라면
                            Log.d("로그", " ROUTE api호출 성공")

                            var coordinates = arrayListOf<List<Double>>()  //경로를 받는 이중배열리스트
                            var turnTypes = arrayListOf<Int?>()
                            var facilityTypes = arrayListOf<String?>()
                            var roadTypes = arrayListOf<Int?>()
                            var totalDistances = arrayListOf<Int?>()
                            var distances = arrayListOf<Int?>()

                            if (parseRouteDataArray != null) {
                                LogLineBreak(parseRouteDataArray.toString())

                                var jsonarr = parseRouteDataArray.listIterator()
                                while (jsonarr.hasNext()){
                                    var jsonarrNext = jsonarr.next()

                                    var corrdinate = jsonarrNext.coordinates.asJsonArray
                                    var next = Gson().fromJson(corrdinate,ArrayList::class.java).listIterator()
                                    while (next.hasNext()){
                                        val next2 = next.next()
                                        if(next2 !is Double){
                                            coordinates.add(next2 as List<Double>)
                                        }
                                    }
                                    var turnType = jsonarrNext.turnType
                                    var facilityType = jsonarrNext.facilityType
                                    var roadType = jsonarrNext.roadType
                                    var totalDistance = jsonarrNext.totalDistance
                                    var distance = jsonarrNext.distance

                                    turnTypes.add(turnType)
                                    facilityTypes.add(facilityType)
                                    roadTypes.add(roadType)
                                    totalDistances.add(totalDistance)
                                    distances.add(distance)
                                    //혹시 응답 전체를 원하시면 그냥 postman이 속 편합니다.
                                }
                                Log.d("coor로그","${coordinates}")
                                Log.d("turnType로그","${turnTypes}")
                            }
                        }
                        Constant.RESPONSE_STATE.FAIL->{//만약 STATE가 FAIL라면
                            Log.d("로그", " ROUTE api호출 실패")
                        }
                        Constant.RESPONSE_STATE.NO_CONTENT->{//만약 NO_CONTENT가 FAIL라면
                            Log.d("로그", " ROUTE 결과가 없습니다.")
                        }
                    }
                })
        }
    }
}