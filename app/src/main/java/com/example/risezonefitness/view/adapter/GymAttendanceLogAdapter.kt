package com.example.risezonefitness.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.risezonefitness.R
import com.example.risezonefitness.model.LogData

class GymAttendanceLogAdapter(private var logs: List<LogData>) :
    RecyclerView.Adapter<GymAttendanceLogAdapter.LogViewHolder>() {

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val entriesTextView: TextView = itemView.findViewById(R.id.entriesTextView)
        val exitsTextView: TextView = itemView.findViewById(R.id.exitsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gym_attendance_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        holder.dateTextView.text = "📅 ${log.date}"

        val entriesText = log.entries.joinToString(separator = "\n") { "Entry: $it" }
        val exitsText = log.exits.joinToString(separator = "\n") { "Exit: $it" }

        holder.entriesTextView.text = entriesText.ifEmpty { "No Entries" }
        holder.exitsTextView.text = exitsText.ifEmpty { "No Exits" }


        holder.itemView.animation = android.view.animation.AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.item_animation_fade_in
        )
    }

    override fun getItemCount(): Int = logs.size

    fun updateLogs(newLogs: List<LogData>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}
