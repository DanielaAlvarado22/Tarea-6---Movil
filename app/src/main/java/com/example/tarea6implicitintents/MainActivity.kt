package com.example.tarea6implicitintents

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    private lateinit var btn_camera : Button
    private lateinit var iv_image : ImageView



    //los códigos para activar los permisos de la cámara
    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_camera = findViewById(R.id.btn_camera)

        var number : EditText = findViewById(R.id.number)
        var msg : EditText = findViewById(R.id.msg)
        var btn : Button = findViewById(R.id.sendbtn)

        //esto es para que cheque si tenemos permiso para usar la cámara

        btn_camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                          this,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,CAMERA_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }

        btn.setOnClickListener{
            if(number.text.isNotEmpty() && msg.text.isNotEmpty()){
                val packageManager : PackageManager = packageManager
                val i = Intent(Intent.ACTION_VIEW)
                val url :String = "https://api.whatsapp.com/send?phone=" + number.text.toString() + "&text=" + URLEncoder.encode(msg.text.toString(), "UTF-8")
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if(i.resolveActivity(packageManager) != null){
                    startActivity(i)
                }
            }else{
                Toast.makeText(this,"por favor ingresa el número o mensaje", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(
                    this,
                    "El acceso ha sido denegado",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                val thumBNail : Bitmap = data!!.extras!!.get("data") as Bitmap
                iv_image = findViewById(R.id.iv_image)
                iv_image.setImageBitmap(thumBNail)
            }
        }
    }


}