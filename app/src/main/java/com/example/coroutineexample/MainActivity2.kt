package com.example.coroutineexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity2 : AppCompatActivity() {

    private val IMAGE_URL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap()
            }

            val originalBitmap = originalDeferred.await()
            val filteredDeferred =  coroutineScope.async(Dispatchers.Default) {
                applyFilter(originalBitmap)
            }

            val filterBitmap = filteredDeferred.await()
            loadImage(filterBitmap)
        }
    }

    private fun getOriginalBitmap(): Bitmap =
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun applyFilter(bmp: Bitmap): Bitmap =
        Filter.apply(bmp)

    private fun loadImage(bmp: Bitmap) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val imageView = findViewById<ImageView>(R.id.imageView)
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bmp)
        imageView.visibility = View.VISIBLE
    }
}