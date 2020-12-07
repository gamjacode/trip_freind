package com.example.trip_freind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.trip_freind.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, HomeFragment()).commit()
        //해당 fragment를 쓴다는 것을 설정해줌

        //bottom_navigation 연결
        bottom_navigation_bar.selectedItemId = R.id.bottom_menu_home
        bottom_navigation_bar.setOnNavigationItemSelectedListener {
            val transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.bottom_menu_home -> {
                    transaction.replace(    //fragment가 해당 layout으로 대체됨
                        R.id.fragment_layout,
                        HomeFragment()
                    ).commit()
                    false
                }
                R.id.bottom_menu_setting -> {
                    transaction.replace(    //fragment가 해당 layout으로 대체됨
                        R.id.fragment_layout,
                        SettingFragment()
                    ).commit()
                    false
                }
                else->{
                    false
                }
            }
        }
    }
}
