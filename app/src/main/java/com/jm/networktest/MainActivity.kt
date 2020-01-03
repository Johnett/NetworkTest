package com.jm.networktest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    tvAddDetails.setOnClickListener {
        val addDetailsIntent = Intent(this,AddDetailsActivity::class.java)
        startActivity(addDetailsIntent)
    }

    tvSeeDetails.setOnClickListener {
        val viewDetailsIntent = Intent(this,ViewDetailsActivity::class.java)
        startActivity(viewDetailsIntent)
    }
  }
}
