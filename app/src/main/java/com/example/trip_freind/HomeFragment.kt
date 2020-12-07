package com.example.trip_freind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trip_freind.R
import com.example.trip_friend.database.model.Review
import com.example.trip_friend.ui.adapter.ReviewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var adapter: ReviewAdapter
    private lateinit var myContext:Context

    val reviews: MutableList<Review> = mutableListOf() //data를 담아줄 list를 만들어 줌
    //fragment에서 context를 가져오기 위함
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseDatabase.getInstance().reference.child("Review") //경로로 들어감 (없으면 생성)
            .addListenerForSingleValueEvent(object : ValueEventListener {  //한번 읽고 다시 읽을 필요가 없을 때
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    reviews.clear()   //혹시 모를 상황을 위해 고정적으로 들어가줘야 함
                    for (i in dataSnapshot.children) {  //i에 child가 하나씩 들어가게됨(여기선 날짜값)
                        FirebaseDatabase
                            .getInstance()
                            .reference
                            .child("Review")
                            .child(i.key ?: "")            //Review경로 아래에 child의 key(날짜값)경로로 들어가줌
                                                                     //해당하는 key값을 몰라도 다음과 같이 쓸 수 있게된다.(key값을 모르기 떄문에 Review까지 들어감
                           .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {}

                                //Review -> 날짜 -> 그다음 값들을 넣어줌 key값을 알아서 value를 바로 쓸 수 있음
                                override fun onDataChange(snapshot: DataSnapshot) { //현재 경로(날짜) 아래에 변동사항이 있으면 캐치하여 저장
                                    val review = Review()         //변수에 class를 담아줌
                                    for (j in snapshot.children) {    //j에 child가 하나씩 들어가게된다.
                                        when (j.key) {   
                                            "comment" -> {//child가 comment일 경우
                                                review.comment = j.value as MutableMap<String, Map<String, String>> //value에 값이 등록이 되어 있을 경우 value변수에 값이 등록되나.
                                            }
                                            "image" -> {
                                                review.image = j.value as HashMap<String, String>
                                            }
                                            "like" -> {
                                                review.like = j.value as HashMap<String, Boolean>
                                            }
                                            "writer" -> {
                                                review.writer = j.value as String
                                            }
                                            else -> {
                                                review.number = j.value as String
                                            }
                                        }
                                    }
                                    reviews.add(review)     //data를 review 리스트에 전부 넣어줌
                                   val layoutManager = LinearLayoutManager(myContext)
                                    home_recycler_view.adapter = adapter
                                    home_recycler_view.layoutManager = layoutManager
                                    home_recycler_view.setHasFixedSize(true)
                                }

                            })

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })

        adapter = ReviewAdapter(reviews, ::onClickLike, myContext)
        return inflater.inflate(R.layout.fragment_home, container, false)   //fragment 마지막 내용
    }

    private fun setLikeToReviews(review: Review) {
        for (i in 0 until reviews.size) { //우리가 좋아요를 누른게 어떤 review인지 모르기 떄문에 size만큼 돌아야 한다.
                                    //여기서 reviews의 size는 reviews에 class값의 위인 날짜가 된다(2)
            if (reviews[i].number == review.number) {  //0번째부터 비교해줌 = review.number(좋아요를 누른 review의 번호)
                reviews[i].like =
                    mapOf(  //reviews의 key(uid) to 우리가 좋아요를 누른 uid의 값(true or false) 아무것도 안해주었다? : false
                        FirebaseAuth.getInstance().currentUser!!.uid to !(reviews[i].like[FirebaseAuth.getInstance().currentUser!!.uid]
                            ?: false)

                    )
            }
        }
    }

    private fun onClickLike(review: Review) {   //onClickLike : 좋아요를 눌렀을떄 이러이러한 것들을 할꺼다
        setLikeToReviews(review)   //우선 좋아요를 어떤것을 눌렀는지 파악해주고 세팅
        GlobalScope.launch {       //하나의 코루틴을 생성함 이 함수가 끝나고 나서 다른 것들을 하게 하기 위해(이 함수가 제일 먼저 돌아감)
            withContext(Dispatchers.Main) { //코루틴이 어디에서 실행되는지 Main(UI)에서 한다는 뜻
                adapter.notifyDataSetChanged() //notifyDataSetChanged -> adapter에 reviews를 넘겨줬는데 reviews가 자동으로 업데이트 됨
                for (r in reviews) {    //reviews 클래스의 덩어리(날짜 갯수) 만큼 돌게된다.
                    if (r.number == review.number) {  //내가 입력한 number와 r.number가 같다면
                        FirebaseDatabase.getInstance().reference.child("Review")
                            .child(review.number).child("like")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(r.like[FirebaseAuth.getInstance().currentUser!!.uid])

                    }
                }
            }
        }
    }
}
