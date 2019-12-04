package com.example.amit.uniconnexample.Fragment.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel

/**
 * Created by Meera on 04,December,2019
 */
class HomeFragmentAdapter(var itemOptionsClickListener: ItemOptionsClickListener): RecyclerView.Adapter<HomeFragmentAdapter.HomeAdapterViewHolder>() {
     var postModels = ArrayList<PostModel>()
    fun setData(posts: List<PostModel>){
        val size = postModels.size
        postModels.addAll(posts)
        notifyItemRangeInserted(size, posts.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        return HomeAdapterViewHolder(view, itemOptionsClickListener)
    }

    override fun getItemCount(): Int {
        return postModels.size
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        val postModel = postModels[position]
        holder.setData(postModel)

        holder.likeButton.setOnClickListener {
            if(postModel.liked == 1){
                postModel.liked = 0
                itemOptionsClickListener.onPostUnlike(postModel.postId)
                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_UNLIKE)
            }else{
                postModel.liked = 1
                itemOptionsClickListener.onPostLike(postModel.postId)
                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_LIKE)
            }
        }
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.setData(postModels[position], (payloads[0] as String))
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class HomeAdapterViewHolder(itemView: View, var itemOptionsClickListener: ItemOptionsClickListener) : RecyclerView.ViewHolder(itemView){
        var pImage: ImageView = itemView.findViewById(R.id.pimage)
        var name: TextView = itemView.findViewById(R.id.bname)
        var image: ImageView = itemView.findViewById(R.id.postimage)
        var postDesc: TextView = itemView.findViewById(R.id.post_desc)
        var likeButton: ImageButton = itemView.findViewById(R.id.like)
        var unlikeButton: ImageButton = itemView.findViewById(R.id.unlike)

        fun setData(post: PostModel?){
            post?.let {postModel ->
                postDesc.text = postModel.desc
                postModel.date
                postModel.desc
                postModel.imageUrl
            }
        }

        fun setData(post: PostModel?, payload: String){
            if(payload == CommonString.PAYLOAD_ITEM_LIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Grenn))
            }else if(payload == CommonString.PAYLOAD_ITEM_UNLIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
            }
        }
    }
}