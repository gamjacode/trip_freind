package com.example.trip_freind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trip_freind.database.model.Plan
import com.example.trip_friend.ui.adapter.PlanAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_plan.*

class PlanActivity : AppCompatActivity() {

    lateinit var post : Plan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        
        p_button.setOnClickListener(){
            val myRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Plan").child(FirebaseAuth.getInstance().currentUser!!.uid)
            val comment2 = p_edit.text.toString()
            val input1 = Plan(comment2)
            myRef.setValue(input1)

//                myRef.addValueEventListener(object : ValueEventListener {
//                    override fun onCancelled(error: DatabaseError) {
//                        println("Failed to read value.")
//                    }
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다.
//                        val value = snapshot?.value
//                        text3.text = "$value"
//                    }
//                })
            val database1 = FirebaseDatabase.getInstance().getReference("Plan").child(FirebaseAuth.getInstance().currentUser!!.uid)
            database1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    post = dataSnapshot.getValue(Plan::class.java)?: Plan()
                    //데이터를 클래스 형태로 가져와줌
                    var data: MutableList<Plan> = setData()
                    var adapter = PlanAdapter(this@PlanActivity)
                    adapter.listData = data
                    p_recylcerview.adapter = adapter
                    p_recylcerview.layoutManager = LinearLayoutManager(this@PlanActivity)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }
    fun setData():MutableList<Plan>{
        var data : MutableList<Plan> = mutableListOf()
        var uiddata = post
        data.add(uiddata)
        return data
    }
}
//91:6C:A1:B7:AB:63:E9:91:93:68:9F:E4:90:4E:53:6A:77:77:47:C1

