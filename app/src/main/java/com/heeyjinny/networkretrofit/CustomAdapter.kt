package com.heeyjinny.networkretrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heeyjinny.networkretrofit.databinding.ItemRecyclerBinding

//2
//RecyclerView의 Adapter상속받고 제네릭으로 Holder지정 후
//3개 필수 메서드 자동 생성
class CustomAdapter: RecyclerView.Adapter<CustomAdapter.Holder>() {

    //3
    //어댑터에서 사용할 데이터 컬렉션 변수 생성
    //사용할 데이터는 Repository클래스(아이템을 가지고 있는 배열...)사용
    //nullable로 선언
    var userList: Repository? = null

    //5
    //홀더를 생성하는 onCreateViewHolder
    //레이아웃을 인플레이트한 후 바인딩에 담아 홀더에 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return Holder(binding)
    }

    //6
    //실제 목록에 뿌려지는 아이템을 그려주는 onBindViewHolder
    //현재 위치의 사용자 데이터를 userList에 가져와
    //아직 만들어지지 않은 홀더의 setUsers()메서드에 넘김
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = userList?.get(position)
        holder.setUsers(user)
    }

    //4
    //목록에 출력되는 총 아이템 개수 구현
    override fun getItemCount(): Int {
        return userList?.size?: 0
    }

    //1
    //이너 클래스로 Holder클래스 생성
    //홀더의 생성자에서 바인딩을 전달받고
    //상속받은 ViewHolder에 binding.root전달
    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        //7
        //setUser()메서드 구현
        //1개의 RepositoryItem을 파라미터로 사용
        //userList가 nullable이기 때문에
        //userList의 현재위치를 담고있는 user변수도 nullable임
        fun setUsers(user: RepositoryItem?){
            //8
            //홀더가 가지고 있는 아이템 레이아웃에
            //데이터 하나씩 세팅
            //사용하는 데이터: 아바타주소, 사용자이름, 사용자아이디
            //RepositoryItem에는 각 데이터 이름을 사용함
            //아바타주소: user.owner.avatar_url
            //사용자이름: user.name
            //사용자아이디: user.node_id
            user?.let {
                //8-1
                //사용자 이름과 아이디 세팅
                binding.textName.text = user.name
                binding.textId.text = user.node_id
                //8-2
                //Glide.with를 사용하여 이미지 주소를 이미지뷰 안에 로드
                Glide.with(binding.imageView).load(user.owner.avatar_url).into(binding.imageView)
            }
        }//setUsers

    }//Holder

    //9
    //레트로핏을 사용해 데이터를 조회하여 가져오고
    //어댑터를 통해 목록에 출력하는 코드 작성
    //MainActivity.kt작성

}//CustomAdapter