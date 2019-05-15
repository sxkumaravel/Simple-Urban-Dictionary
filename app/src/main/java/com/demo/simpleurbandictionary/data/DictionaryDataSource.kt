package com.demo.simpleurbandictionary.data

import com.demo.simpleurbandictionary.model.Search

/**
 * Created on 2019-05-13.
 *
 * @author kumars
 */
class DictionaryDataSource(private val restInterface: RestInterface) {

    fun searchDictionary(string: String): ArrayList<Search>? {
        try {
            return restInterface.searchDictionary(string)?.list
        } catch (e: Exception) {
            // NOP
        }

        return null
    }
}