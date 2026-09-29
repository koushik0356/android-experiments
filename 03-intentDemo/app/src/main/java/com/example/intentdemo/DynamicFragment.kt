package com.example.intentdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DynamicFragment : Fragment() {

    companion object {
        fun newInstance(fragmentNumber: Int): DynamicFragment {
            val fragment = DynamicFragment()
            val args = Bundle()
            args.putInt("fragment_number", fragmentNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dynamic, container, false)
        val tv = view.findViewById<TextView>(R.id.tv_fragment_text)
        val num = arguments?.getInt("fragment_number") ?: 0
        tv.text = getString(R.string.fragment_text_placeholder, num)
        return view
    }
}