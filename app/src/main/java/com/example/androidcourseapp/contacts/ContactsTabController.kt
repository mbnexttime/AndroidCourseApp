package com.example.androidcourseapp.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.Controller
import com.example.androidcourseapp.R
import com.example.androidcourseapp.utils.Item
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Runnable

class ContactsTabController : Controller {
    private var viewHolder: ContactsTabViewHolder? = null

    private var initialized: Boolean = false

    private lateinit var contactsApi: ContactsApi

    private var latestData: ArrayList<Item> = ArrayList()

    private val mainHandler: Handler
        get() {
            return Handler(Looper.getMainLooper())
        }

    private var onContactsLoadedCallback: Runnable? = null
    private var loadingJob: Job? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun initialize(context: Context) {
        Log.d(TAG, "full initialization")
        if (initialized) {
            return
        }
        initialized = true
        invalidateContext(context)
        contactsApi = provideApi()
        launchContactsRequest()
    }

    override fun invalidateContext(newContext: Context) {
        Log.d(TAG, "invalidate context")
        val recyclerView =
            LayoutInflater.from(newContext).inflate(R.layout.contacts_tab_layout, null) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(newContext)
        viewHolder = ContactsTabViewHolder(recyclerView)
        viewHolder?.setNewData(latestData)
    }

    override fun getView(context: Context): View {
        Log.d(TAG, "asked view")
        initialize(context)
        if (viewHolder == null) {
            throw RuntimeException("view holder is null after initialization in ContactsTabController")
        }
        return viewHolder!!.getView()
    }

    override fun onCreate() = Unit

    override fun onDestroy() {
        viewHolder = null
        removeCallback()
        loadingJob?.cancel()
        loadingJob = null
    }

    private fun removeCallback() {
        onContactsLoadedCallback?.let { mainHandler.removeCallbacks(it) }
        onContactsLoadedCallback = null
    }

    private fun postCallback() {
        onContactsLoadedCallback?.let { mainHandler.post(it) }
    }

    private fun launchContactsRequest() {
        Log.d(TAG, "launched contacts request")
        loadingJob = CoroutineScope(Dispatchers.Default).launch {
            val download = async {
                return@async contactsApi.getUsers().data
            }
            val data = download.await()
            synchronized(this) {
                removeCallback()
                if (isActive) {
                    onContactsLoadedCallback = Runnable {
                        applyLoadedContacts(data)
                    }
                    postCallback()
                    loadingJob = null
                }
            }
        }
    }

    private fun applyLoadedContacts(contacts: List<ContactItem>) {
        val resultList = ArrayList<Item>()
        for (i in contacts.indices) {
            resultList.add(contacts[i])
            if (i != contacts.size) {
                resultList.add(DelimItem())
            }
        }
        latestData = resultList
        viewHolder?.setNewData(resultList)
    }

    private fun provideApi(): ContactsApi {
        return Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .build()
            .create(ContactsApi::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    companion object {
        private val TAG = "ContactsTabController"
    }
}