package com.example.weather

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.illabo.dadatasuggestions.DadataSuggestions
import app.illabo.dadatasuggestions.model.AddressSuggestionRequest
import app.illabo.dadatasuggestions.model.SuggestionData
import java.util.*

class CityFragment(val city: MutableLiveData<SuggestionData>) : Fragment() {
    lateinit var recViewCityAdapter: CityAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.city_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun closeKeyboard() {
            val view = this.view
            if (view != null) {
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        var data: MutableLiveData<SuggestionData> = MutableLiveData()
        val recView = view.findViewById<RecyclerView>(R.id.recView)
        recView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            recViewCityAdapter = CityAdapter(data)
            adapter = recViewCityAdapter
        }
        data.observe(viewLifecycleOwner) {
            closeKeyboard()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            city.value = it
        }
        view.findViewById<ImageButton>(R.id.backCityBtn).setOnClickListener {
            closeKeyboard()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("kek", "вернулся")
                    activity?.supportFragmentManager?.beginTransaction()?.remove(this@CityFragment)
                        ?.commit()
                }
            }
        activity?.onBackPressedDispatcher?.addCallback(callback)
        val dadataClient = DadataSuggestions("98a76ee3a6f6b81206b42f056fc7261872f69acc")
        val handler = Handler()
        val edit = view.findViewById<EditText>(R.id.edit)
        edit.addTextChangedListener {
            val text = edit.text.toString()
            if (text != "") {
                dadataClient.suggest(
                    AddressSuggestionRequest(edit.text.toString(), 5, "en")
                ) {
                    val sug = it.suggestions
                    val list = mutableListOf<SuggestionData>()
                    for (item in sug) {
                        if (item.data.city != null) {
                            list.add(item.data)
                        }
                    }
                    handler.post {
                        recViewCityAdapter.setListData(list)
                        recViewCityAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }
}