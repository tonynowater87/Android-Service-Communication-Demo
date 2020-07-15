package com.tonynowater.hyenatest.app.views.main

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tonynowater.hyenatest.R
import com.tonynowater.hyenatest.app.service.BikeService
import com.tonynowater.hyenatest.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bikeBinder = service as BikeService.BikeBinder
            viewModel.enableForeground.postValue(bikeBinder.enableForeground())
            viewModel.connected.postValue(bikeBinder.isConnected())
            viewModel.listenEvents(bikeBinder.getBikeConnectedEvents())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {

                bindBikeService()

                lifecycleOwner = this@MainActivity

                lifecycle.addObserver(viewModel)

                vm = viewModel
            }
    }

    private fun bindBikeService() {
        bindService(
            Intent(this, BikeService::class.java),
            serviceConnection,
            Service.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
