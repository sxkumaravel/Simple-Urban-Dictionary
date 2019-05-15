package com.demo.simpleurbandictionary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.simpleurbandictionary.data.DictionaryDataSource
import com.demo.simpleurbandictionary.model.Search
import com.demo.simpleurbandictionary.util.singleArgViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * Created on 2019-05-12.
 *
 * @author kumars
 */
class DictionaryViewModel(private val dataSource: DictionaryDataSource) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DictionaryViewModel)
    }

    enum class Sort {
        NONE, THUMBS_UP, THUMBS_DOWN
    }

    private var sort: Sort = Sort.NONE
    private var originalList: ArrayList<Search>? = null

    private val viewModelJob = Job()
    private val bgViewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val _errorEmptySearchLiveData = MutableLiveData<Boolean>()
    private val _searchResultLiveDate = MutableLiveData<ArrayList<Search>?>()
    private val _isLoadingLiveData = MutableLiveData<Boolean>()

    val errorEmptySearchLiveData: LiveData<Boolean>
        get() = _errorEmptySearchLiveData

    val searchResultLiveDate: LiveData<ArrayList<Search>?>
        get() = _searchResultLiveDate

    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Search the dictionary for the given word.
     */
    fun searchDictionary(string: String) {
        if (string.isEmpty()) {
            _errorEmptySearchLiveData.value = true
            return
        }

        _isLoadingLiveData.value = true
        bgViewModelScope.launch {
            val searchDictionary = dataSource.searchDictionary(string)
            originalList = searchDictionary
            _isLoadingLiveData.postValue(false)
            sortDictionaryResults(sort)
        }
    }

    /**
     * Logic to sort the {@link #searchList}. This function is UI (will always update the live data in UI thread).
     * And this function will not modify the existing list -> will create a new instance of the list.
     */
    fun sortDictionaryResults(sort: Sort, searchList: ArrayList<Search>? = originalList, forceSort: Boolean = false) {
        // to avoid executing sorting for the same type
        if (this.sort == sort && forceSort) {
            return
        }

        this.sort = sort

        searchList?.let { it ->
            when (sort) {
                Sort.NONE -> { // if none use the original list
                    _searchResultLiveDate.postValue(searchList)
                }
                Sort.THUMBS_UP -> {
                    val arrayList = ArrayList(it) // creating a new list to avoiding modifying th original
                    arrayList.sortByDescending {
                        it.thumbsUp
                    }
                    _searchResultLiveDate.postValue(arrayList)
                }
                Sort.THUMBS_DOWN -> {
                    val arrayList = ArrayList(it)
                    arrayList.sortByDescending {
                        it.thumbDown
                    }
                    _searchResultLiveDate.postValue(arrayList)
                }
            }
        }
    }
}