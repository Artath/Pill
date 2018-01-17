package com.example.pill

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item.view.*
import java.io.File


class MedicineAdapter(var data:ArrayList<Medicine>, var context: Context):RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder =
        MedicineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.nameMed.text = data[position].nameMed
        holder.infoMed.text = data[position].infoMed
        holder.inst.text = data[position].instruction
        holder.img.setImageBitmap(BitmapFactory.decodeFile(
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        data[position].photoLink).absolutePath))
        holder.remindBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, ReminderActivity::class.java)
            intent.putExtra("name", data[position].nameMed)
            startActivity(context, intent, Bundle.EMPTY)
        })

    }

    class MedicineViewHolder(view: View):RecyclerView.ViewHolder(view){
        var img: ImageView = view.photoImg
        var nameMed: TextView = view.nameTxt
        var infoMed: TextView = view.infoTxt
        var inst: TextView = view.instTxt
        var remindBtn: Button = view.remindBtn
    }
}