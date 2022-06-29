package com.example.keepit

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepit.enums.Language
import com.example.keepit.room.DictEntry

class DictEntryRecyclerViewAdapter(var context: Context,
                                   val list: List<DictEntry>) : RecyclerView.Adapter<DictEntryRecyclerViewAdapter.EntryViewHolder>() {

    class EntryViewHolder(itemView: View,
                          var targetTextView: TextView = itemView.findViewById(R.id.targetTextView),
                          var srcTextView: TextView = itemView.findViewById(R.id.srcTextView)) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dictentry_list_item, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val item = list[position]

        holder.targetTextView.text = targetTextViewText(item)
        holder.srcTextView.text = srcTextViewText(item)
    }

    //TODO will I have to distinguish between src and target ind? src and target phon? ...

    fun srcTextViewText(item: DictEntry): Spannable {
        val s = SpannableString(item.sourceWord + " " + (item.ind ?: ""))

        if (item.ind != null) {
            s.setSpan(
                StyleSpan(Typeface.ITALIC),
                item.sourceWord.length,
                s.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            s.setSpan(
                RelativeSizeSpan(0.9f),
                item.sourceWord.length,
                s.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return s
    }

    fun targetTextViewText(item: DictEntry): Spannable {
        val phon = if (item.phon == "null") null else item.phon

        val s = if (phon != null) {
            SpannableString(phon + " " + item.targetWord)
        } else {
            SpannableString(item.targetWord)
        }

        s.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.dark_red)),
            0,
            phon?.length ?: 0,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        s.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.frog_green)),
            phon?.length ?: 0,
            s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (item.targetLang == Language.AR) //conditional text size
            s.setSpan(
                RelativeSizeSpan(1.8f),
                phon?.length ?: 0,
                s.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        return s
    }

    override fun getItemCount(): Int {
        return list.size
    }

}


//https://www.youtube.com/watch?v=18VcnYN5_LM
//spannables: https://blog.mindorks.com/spannable-string-text-styling-with-spans
