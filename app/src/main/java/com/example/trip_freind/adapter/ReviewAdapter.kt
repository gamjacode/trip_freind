package com.example.trip_friend.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trip_freind.CommentActivity
import com.example.trip_freind.R
import com.example.trip_friend.database.model.Review
import com.example.trip_friend.util.LikeCompare
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.item_home.view.*
import kotlin.reflect.KFunction1


class ReviewAdapter(
    var reviews: List<Review>,
    val likeClick: KFunction1<@ParameterName(name = "review") Review, Unit>,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReviewViewHolder).bind(reviews[position])
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.item_home_image_carousel_view
        private val like = itemView.item_home_like_radio_button
        private val commentImg = itemView.item_home_comment_image
        private val commentText = itemView.item_home_comment_text

        fun bind(review: Review) {
            val imageListener =
                ImageListener { position, imageView ->
                    //ImageListener : 여러개 등록된 사진을 swipe할 수 있게 해주는 listener
                    val image: Array<String> =
                        review.image.values.toTypedArray()   //image에 해당하는 value들이 뽑혀서 array로 저장된다.
                    Glide.with(context).load(image[position])
                        .into(imageView)   //glide를 활용해 array에 등록된 image를 보여줌
                }

            image.pageCount = review.image.size //swipe를 해주기 위해
            image.setImageListener(imageListener)

            like.isChecked = false
            if (LikeCompare().likeCompare(review)) {
                like.isChecked = true
            }

            if(review.comment.isNotEmpty()){
                commentText.text = review.comment.values.toTypedArray()[review.comment.size - 1].values.toTypedArray()[0]
            }else{
                commentText.text = ""
            }

            like.setOnClickListener(View.OnClickListener { likeClick(review) })
            commentImg.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("reviewData", review) //review data 하나를 넘겨줌
                context.startActivity(intent)   //adapter이기 때문에 context가 없어 이렇게 해줘야함
            })
            commentText.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("reviewData", review)
                context.startActivity(intent)
            })
        }
    }
}