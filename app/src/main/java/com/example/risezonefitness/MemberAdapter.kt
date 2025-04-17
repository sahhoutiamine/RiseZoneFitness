package com.example.risezonefitness

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val members: List<Member>) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tvMemberName)
        val subscriptionStatusText: TextView = itemView.findViewById(R.id.tvSubscriptionStatus)
        val avatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val attendanceLine: View = itemView.findViewById(R.id.attendanceLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member_card, parent, false)
        return MemberViewHolder(view)
    }


    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.nameText.text = member.fullName
        holder.subscriptionStatusText.text = if (member.isPaid) "Subscribed" else "Not Subscribed"


        if (member.isInGym) {
            val attendanceColor = ContextCompat.getDrawable(holder.itemView.context, R.drawable.in_gym_color)
            holder.attendanceLine.setBackgroundDrawable(attendanceColor)
        }


        holder.avatar.setImageResource(member.imageResource)
    }

    override fun getItemCount(): Int = members.size
}
