package com.example.trip_freind


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trip_freind.R
import com.example.trip_freind.database.model.Plan
import com.example.trip_friend.ui.adapter.PlanAdapter
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_plan.*
import kotlinx.android.synthetic.main.fragment_plan.view.*
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNREACHABLE_CODE")
class PlanFragment : Fragment(), OnMapReadyCallback {
    lateinit var post: Plan
    lateinit var myContext: Context
    lateinit var mMap:GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)
        view.p_button.setOnClickListener {
            val comment2 = p_edit.text.toString()
            val input1 = Plan(comment2)
            FirebaseDatabase.getInstance().getReference("Plan")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(input1)

            val database1 = FirebaseDatabase.getInstance().getReference("Plan")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            database1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    post = dataSnapshot.getValue(Plan::class.java) ?: Plan()
                    //데이터를 클래스 형태로 가져와줌
                    var data: MutableList<Plan> = setData()
                    var adapter = PlanAdapter(myContext)
                    adapter.listData = data
                    p_recylcerview.adapter = adapter
                    p_recylcerview.layoutManager = LinearLayoutManager(myContext)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
        return view

        // 구글맵에서 검색창 editText 와 검색된 위치 불러올 textView 초기화

        p_button.setOnClickListener(View.OnClickListener { // 검색창에서 텍스트를 가져온다
           onMapSearch(view)
        })
    }

    fun setData(): MutableList<Plan> {
        var data: MutableList<Plan> = mutableListOf()
        var uiddata = post
        data.add(uiddata)
        return data
    }

    fun onMapSearch(view: View) {
        val location = view.p_edit.text.toString()
        var addressList: List<Address>? = null
        if(location.isNotEmpty()){
            val geocoder = Geocoder(myContext)
            try{
                addressList = geocoder.getFromLocationName(location,1)
            }catch (e:IOException){
                e.printStackTrace()
            }
            val address:Address = addressList!!.get(0)
            val latLng = LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title("Marker"))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
    }
}
