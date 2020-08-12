package com.fynzero.finalsubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setUser(users: ArrayList<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        ))
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userList[holder.adapterPosition])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            with(itemView) {
                Picasso.get().load(user.avatar_url).placeholder(R.drawable.person).into(img_avatar)
                txt_username.text = user.login
                txt_type.text = user.type
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}