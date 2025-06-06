package com.example.risezonefitness.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.risezonefitness.R
import com.example.risezonefitness.model.Member
import com.example.risezonefitness.view.activity.ProfileActivity
import com.google.firebase.firestore.FirebaseFirestore

class MemberAdapter(private val members: List<Member>, private val context: Context) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

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
            holder.attendanceLine.background = attendanceColor
        }

        if (member.imageResource != null) {
            Glide.with(holder.itemView.context)
                .load(member.imageResource)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatar)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_person)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatar)
        }

        holder.itemView.setOnClickListener {
            db.collection("Members").document(member.username).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username")
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.putExtra("member_username", username)
                        intent.putExtra("document_id", member.username)
                        context.startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting document: $exception")
                }
        }

    }

    override fun getItemCount(): Int = members.size
}