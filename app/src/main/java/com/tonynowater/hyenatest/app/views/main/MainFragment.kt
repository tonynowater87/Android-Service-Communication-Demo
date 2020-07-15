package com.tonynowater.hyenatest.app.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tonynowater.hyenatest.R
import com.tonynowater.hyenatest.databinding.FragmentMainBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    private lateinit var bindings: FragmentMainBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        bindings = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        bindings.lifecycleOwner = viewLifecycleOwner
        return bindings.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindings.apply {
            vm = viewModel
        }
    }
}