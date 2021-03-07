package com.mtg.counters.modules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.ActivityMainBinding
import com.mtg.counters.modules.fr_create_item.CreateItemFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}