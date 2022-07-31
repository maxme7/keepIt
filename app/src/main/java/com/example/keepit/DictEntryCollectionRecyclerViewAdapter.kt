package com.example.keepit

import android.content.ClipData.Item
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepit.room.entities.DictEntry


class ItemViewHolder(var itemView: View,
                     var targetTextView: TextView = itemView.findViewById(R.id.targetTextView),
                     var srcTextView: TextView = itemView.findViewById(R.id.srcTextView),
                     var checkbox: CheckBox = itemView.findViewById(R.id.checkBox)) : RecyclerView.ViewHolder(itemView) {

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        itemView.setOnClickListener(onClickListener);
    }
}

//from: https://stackoverflow.com/questions/33434626/get-list-of-checked-checkboxes-from-recyclerview-android
open interface OnItemCheckListener {
    fun onItemCheck(item: DictEntry?)
    fun onItemUncheck(item: DictEntry?)
}

class DictEntryCollectionRecyclerViewAdapter(var context: Context,
                                             val list: List<DictEntry>,
                                             var onItemCheckListener: OnItemCheckListener) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.checkbox_select_item, parent, false)
        Log.i("POSITION", "here")
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.srcTextView.text = item.sourceWord
        holder.targetTextView.text = item.targetWord
//        holder.targetTextView.text = targetTextViewText(item)
//        holder.srcTextView.text = srcTextViewText(item)

        val currentItem: DictEntry = list.get(position)

        holder.setOnClickListener() {
            holder.checkbox.setChecked(!holder.checkbox.isChecked())

            if (holder.checkbox.isChecked()) {
                onItemCheckListener.onItemCheck(currentItem)
            } else {
                onItemCheckListener.onItemUncheck(currentItem)
            }
        }
    }


    //TODO will I have to distinguish between src and target ind? src and target phon? ...

//    fun srcTextViewText(item: DictEntry): Spannable {
//        val s = SpannableString(item.sourceWord + " " + (if (item.ind == "null") "" else item.ind))

//        if (item.ind != "null") {
//            s.setSpan(
//                StyleSpan(Typeface.ITALIC),
//                item.sourceWord.length,
//                s.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//
//            s.setSpan(
//                RelativeSizeSpan(0.9f),
//                item.sourceWord.length,
//                s.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }

//        return s
//    }

//    fun targetTextViewText(item: DictEntry): Spannable {
//        val phon = if (item.phon == "null") null else item.phon
//
//        val s = if (phon != null) {
//            SpannableString(phon + " " + item.targetWord)
//        } else {
//            SpannableString(item.targetWord)
//        }
//
//        s.setSpan(
//            ForegroundColorSpan(context.resources.getColor(R.color.dark_red)),
//            0,
//            phon?.length ?: 0,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        s.setSpan(
//            ForegroundColorSpan(context.resources.getColor(R.color.frog_green)),
//            phon?.length ?: 0,
//            s.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        if (item.targetLang == Language.AR) //conditional text size
//            s.setSpan(
//                RelativeSizeSpan(1.8f),
//                phon?.length ?: 0,
//                s.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//
//        return s
//    }

    override fun getItemCount(): Int {
        return list.size
    }

}


//https://www.youtube.com/watch?v=18VcnYN5_LM
//spannables: https://blog.mindorks.com/spannable-string-text-styling-with-spans
