package com.example.trip_friend.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_comment.view.*


class CommentAdapter(private val comments: MutableCollection<Map<String, String>>, val context: Context) :
    RecyclerView.Adapter<Holder1>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder1 {    //어떤 data와 xml을 bind 시켜주는 역할(어떤 item을 쓰는지 정해줄 수 있음)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return Holder1(view)
    }
    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: Holder1, position: Int) {
        val comment = comments.elementAt(position).values.toTypedArray()[0]
        val uid = comments.elementAt(position).keys.toTypedArray()[0]
        FirebaseDatabase.getInstance().reference.child("User").child(uid).child("image")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    holder.commentText.text = comment
                    val imageUrl = snapshot.value as String
                    Glide.with(context).load(imageUrl).circleCrop().into(holder.imageGet)
                }

            })
    }

}

class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val commentText: TextView = itemView.c_text
    val imageGet: ImageView = itemView.c_image
}