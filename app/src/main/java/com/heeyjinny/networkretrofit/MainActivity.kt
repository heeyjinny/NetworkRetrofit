package com.heeyjinny.networkretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.heeyjinny.networkretrofit.databinding.ActivityMainBinding
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**깃허브 사용자 정보를 가져오는 앱 개발**/

//1
//레트로핏, converter-gson, glide 라이브러리 추가
//build.gradle(:app)

//2
//인터넷 접근을위한 권한 추가
//AndroidManifest.xml

//3
//안드로이드는 JSON형식으로 된 텍스트 데이터를 코틀린 클래스로
//간단하게 변환해주는 플러그인 JSON To Kotlin Class 을 지원함
//상단 메뉴 Android Studio - Preference - Plugins
//JSON To Kotlin Class 검색 후 설치

//4
//기본패키지 우클릭 - New - Kotlin data class File from JSON
//웹 브라우저에서 가져올 JSON형식 텍스트 데이터 복사 붙여넣기
//https://api.github.com/users/Kotlin/repos
//클래스명 Repository 설정 - Generate

//변환된 데이터 클래스 자동생성 됨
//(License, Owner, Repository, RepositoryItem)

//License, Owner: JOSN데이터가 JSON오브젝트를 값으로 사용하는 경우
//해당 데이터 이름으로 클래스 생성하여 사용

//Repository, RepositoryItem: Repository클래스는
//RepositoryItem의 배열을 상속받는 형태로 생성됨
//실제 데이터 구조는 RepositoryItem클래스에 생성됨

//5
//데이터를 출력할 화면 만들기
//activity_main.xml, recyclerView사용

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //7
        //데이터 요청을위해
        //커스텀 어댑터를 생성하여 어댑터변수 생성
        //레이아웃 리사이클러뷰 어댑터에 커스텀 어댑터 연결
        val adapter = CustomAdapter()
        binding.recyclerView.adapter = adapter

        //7-1
        //리사이클러뷰 리니어 레이아웃 매니저 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //8
        //Retrofit.Builder()를 사용해 레트로핏 생성하여
        //baseUrl이 되는 깃허브 도메인 주소와 JSON데이터를
        //Repository클래스의 컬렉션으로 변환해주는 컨버터 입력 후
        //build()메서드 호출하여 생성하여 변수에 담아 변수 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //9
        //버튼을 클릭하면 레트로핏을 이용해 데이터를 불러오고 어댑터에 세팅할 코드 작성
        //요청버튼을 클릭리스너 연결
        binding.btnRequest.setOnClickListener {

            //10
            //레트로핏 create() 메서드에
            //인터페이스를 파라미터로 넘겨주면
            //실행 가능한 서비스 객체를 생성해 반환해줌
            val githubService = retrofit.create(GithubService::class.java)
            //11
            //githubService는 GithubService인터페이스를 이용해 객체를 생성했기에
            //실행가능한 상태의 users()메서드를 가지고 있음
            //레트로핏 create() 메서드는 인터페이스를 실행가능한 객체로 만들면서
            //users()메서드 안에 비동기 통신으로 데이터를 가져오는
            //enqueue()메서드를 추가해 놓음...
            //enqueue()메서드가 호출되면 통신이 시작됨

            //enqueue()메서드 호출 후 깃허브API 서버로부터 응답을 받으면
            //enqueue()안에 작성하는 콜백 인터페이스가 작동하게 됨
            //enqueue()파라미터로 콜백 인터페이스를 구현해야함
            //콜백 인터페이스 구현 후 필수 메서드 2가지 구현...
            githubService.users().enqueue(object: Callback<Repository>{

                override fun onResponse(
                    call: retrofit2.Call<Repository>,
                    response: Response<Repository>
                ) {
                    //11-1
                    //통신이 성공적이면 onResponse메서드 호출
                    //두 번째 파라미터인 response의 body()메서드 호출 시
                    //서버로부터 전송된 데이터를 꺼낼 수 있음
                    //꺼낸 데이터를 Repository로 형변환 후 어댑터의 userList에 담음
                    adapter.userList = response.body() as Repository
                    //11-2
                    //어댑터로 리사이클러뷰에 변경된 사항 반영
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: retrofit2.Call<Repository>, t: Throwable) {
                }

            })

        }//btnRequest

    }//onCreate

}//MainActivity

//6**********
//레트로핏을 사용해 데이터를 조회하여 가져오고
//어댑터를 통해 목록에 출력하는 코드 작성

//레트로핏 사용을 위해서는 인터페이스 정의 필수*****
//레트로핏 인터페이스는 호출방식, 주소, 데이터 등 지정
//레트로핏 라이브러리는 인터페이스를 해석해 HTTP통신을 처리함
interface GithubService{

    //6-1
    //Github Api를 호출할 users메서드 생성
    //반환값은 Call<데이터클래스>형태로 작성
    //Call클래스 사용 시 retrofit2패키지 선택
    //레트로핏은 인터페이스에 지정된 방식으로 서버와 통신하고 데이터를 가져옴

    //@GET 어노테이션을 사용해 요청 주소 설정
    //요청주소는 Github도메인 제외하고 작성
    @GET("users/Kotlin/repos")
    fun users(): retrofit2.Call<Repository>
}