package com.example.http

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.http.databinding.ActivityMainBinding
import retrofit.RetrofitManager
import utils.Constant

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var button:Button

    var location : String = "한성대입구역"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button=findViewById(R.id.Button)

        button.setOnClickListener {
            Log.d("로그", "MainActivity - 검색 버튼이 클릭되었다. /")

            // 검색 api 호출
            //manager에서 인터페이스를 가져오고 호출함수 사용하고
            RetrofitManager.instance.searchPOI(searchKeyword = location,completion = {
                responseState, responseDataArrayList ->

                when(responseState){
                    Constant.RESPONSE_STATE.OKAY->{  //만약 STATE가 OKEY라면
                        Log.d("로그", "api호출 성공")
                    }
                    Constant.RESPONSE_STATE.FAIL->{//만약 STATE가 FAIL라면
                        Log.d("로그", "api호출 실패")
                    }
                    Constant.RESPONSE_STATE.NO_CONTENT->{//만약 NO_CONTENT가 FAIL라면
                        Log.d("로그", "결과가 없습니다.")
                    }
                }
            })
        }

    }


}