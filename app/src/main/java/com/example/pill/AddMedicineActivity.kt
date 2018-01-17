package com.example.pill

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_medicine.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddMedicineActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 0
    private var file: File? = null
    private var medicineDatabaseHelper = MedicineDatabaseHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine)



        photoImg.setOnClickListener(View.OnClickListener {
            if (nameEditTxt.length() != 0) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }else{
                Toast.makeText(applicationContext, "Enter the name first", Toast.LENGTH_SHORT).show()
            }
        })

        button.setOnClickListener(View.OnClickListener {
            var name = nameEditTxt.text.toString()
            var info = infoEditTxt.text.toString()
            var inst = instEditTxt.text.toString()
            var photoLink = name + ".jpg"

            if(nameEditTxt.length() != 0 && infoEditTxt.length() != 0 && instEditTxt.length() != 0 && file != null) {

                val insertData = medicineDatabaseHelper.addData(name, info, inst, photoLink)
                if (insertData) {
                    Toast.makeText(applicationContext, "Data Successfully Inserted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                }else {
                Toast.makeText(applicationContext, "Fill in all the fields and take a photo first", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    var photo = data.extras.get("data") as Bitmap
                    file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), nameEditTxt.text.toString() + ".jpg")
                    val outputStream = FileOutputStream(file)
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    photoImg.setImageBitmap(photo)
                }
            }
            else -> {
                Toast.makeText(applicationContext, "Unrecognized request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
