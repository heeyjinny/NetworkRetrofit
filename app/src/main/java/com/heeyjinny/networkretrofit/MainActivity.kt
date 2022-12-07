package com.heeyjinny.networkretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.heeyjinny.networkretrofit.databinding.ActivityMainBinding

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

    }//onCreate

}//MainActivity

