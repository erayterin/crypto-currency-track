package com.erayterin.cryptocurrencytracker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erayterin.cryptocurrencytracker.R
import com.erayterin.cryptocurrencytracker.adapter.RecyclerViewAdapter
import com.erayterin.cryptocurrencytracker.model.CryptoModel
import com.erayterin.cryptocurrencytracker.service.CryptoAPI
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private val BASE_URL = "https://api.nomics.com/v1/"

    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var recyclerViewAdapter : RecyclerViewAdapter?= null

    // Disposable
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //https://api.nomics.com/v1/prices?key=fa2e8bba9d5ace3ad04efd7dba499d0973c714af
        //fa2e8bba9d5ace3ad04efd7dba499d0973c714af
        compositeDisposable = CompositeDisposable()
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadData()


    }

    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)


        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(cryptoList : List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter
        }

    }
    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked : ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }
}