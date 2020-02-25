package com.example.sampleapp.ui

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var adapter:GalleryAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this
        // Dagger injection
        AndroidInjection.inject(this)

        //placing toolbar in place of actionbar
        setSupportActionBar(mainBinding.toolBar)

        initSearchInputListener()
        adapter = GalleryAdapter()
        mainBinding.rvImages.adapter =adapter
        mainBinding.rvImages.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        //binding result for recycler view
        mainBinding.searchResult = viewModel.listImage

        //binding title based on n/w response
        viewModel.listImage.observe(this, Observer { listImages ->
            listImages?.data?.let { adapter.updateList(it, viewModel.isFilterEnabled) }
        })

        //binding for reload button and swipeRefresh
        mainBinding.callback = object : TryAgainCallBack {
            override fun tryAgain() {
                doSearch(mainBinding.input)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)

        val item:MenuItem =  menu.findItem(R.id.switchId)
        item.setActionView(R.layout.switch_layout)
        val switchFilter = item.actionView.findViewById<Switch>(R.id.switchFilter)
        switchFilter.isChecked = viewModel.isFilterEnabled
        switchFilter.setOnCheckedChangeListener{ view, isChecked ->
            if(isChecked) adapter.filter.filter("filter")
            else adapter.filter.filter("")
            viewModel.isFilterEnabled = isChecked
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_clear -> {
                viewModel.clearImages()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSearchInputListener() {
        mainBinding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        mainBinding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = mainBinding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        viewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}