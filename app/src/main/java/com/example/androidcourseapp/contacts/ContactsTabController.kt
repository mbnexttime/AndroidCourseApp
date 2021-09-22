package com.example.androidcourseapp.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.SharedPreferencesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourseapp.Controller
import com.example.androidcourseapp.R
import com.example.androidcourseapp.utils.Item
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Runnable

class ContactsTabController : Controller {
    private var viewHolder: ContactsTabViewHolder? = null

    private var initialized: Boolean = false

    private lateinit var contactsApi: ContactsApi

    private var context: Context? = null

    private var latestData: ArrayList<Item> = ArrayList()

    private val mainHandler: Handler
        get() {
            return Handler(Looper.getMainLooper())
        }

    private val preferences: SharedPreferences?
        get() {
            return context?.getSharedPreferences(CACHE_CONTACTS, MODE_PRIVATE)
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
        applyLoadedContacts(raiseCache(), false)
        contactsApi = provideApi()
        launchContactsRequest()
    }

    override fun invalidateContext(newContext: Context) {
        this.context = newContext
        Log.d(TAG, "invalidate context")
        val recyclerView =
            LayoutInflater.from(newContext)
                .inflate(R.layout.contacts_tab_layout, null) as RecyclerView
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
        context = null
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
        val handler =
            CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.d(
                    TAG,
                    "${throwable.stackTraceToString()} ${throwable.localizedMessage}"
                )
            }
        loadingJob = CoroutineScope(Dispatchers.Default).launch(handler) {
            val download = async {
                return@async contactsApi.getUsers().data
            }
            val data = download.await()
            synchronized(this) {
                removeCallback()
                if (isActive) {
                    onContactsLoadedCallback = Runnable {
                        applyLoadedContacts(data, true)
                    }
                    postCallback()
                    loadingJob = null
                }
            }
        }
    }

    private fun applyLoadedContacts(contacts: List<ContactItem>, updateCache: Boolean) {
        if (updateCache) {
            putCache(contacts)
        }
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

    private fun putCache(contacts: List<ContactItem>) {
        val preferences = preferences ?: return
        val adapterItem2Object = provideMoshi().adapter(ContactItem::class.java)
        val mapped = contacts.map { adapterItem2Object.toJson(it) }
        val wrapper = ContactItemsWrapper(mapped)
        val adapterArray2String = provideMoshi().adapter(ContactItemsWrapper::class.java)
        preferences.edit()
            .putString(CACHE_CONTACTS_USERS_DATA_V1, adapterArray2String.toJson(wrapper)).apply()
    }

    private fun raiseCache(): List<ContactItem> {
        val preferences = preferences ?: return emptyList()
        val adapterArray2String = provideMoshi().adapter(ContactItemsWrapper::class.java)
        val resource = preferences.getString(CACHE_CONTACTS_USERS_DATA_V1, "") ?: return emptyList()
        if (resource.isEmpty()) {
            return emptyList()
        }
        val wrapper = adapterArray2String.fromJson(resource) ?: return emptyList()
        val result = ArrayList<ContactItem>()
        val adapterItem2Object = provideMoshi().adapter(ContactItem::class.java)
        for (item in wrapper.contacts) {
            val parsed = adapterItem2Object.fromJson(item) ?: continue
            result.add(parsed)
        }
        Log.d(TAG, "raised ${result.size} items from cache")
        return result
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
        private val CACHE_CONTACTS = "cache_contacts"
        private val CACHE_CONTACTS_USERS_DATA_V1 = "cache_contacts_user_data_v1"
    }
}