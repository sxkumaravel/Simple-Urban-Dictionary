package com.demo.simpleurbandictionary.util

import com.demo.simpleurbandictionary.data.DictionaryDataSource
import com.demo.simpleurbandictionary.data.SpringRestInterface

/**
 * Created on 2019-05-13.
 *
 * @author kumars
 */
class DataSourceProvider private constructor() {

    companion object {
        @Volatile
        private var DICTIONARY_DATA_SOURCE: DictionaryDataSource? = null

        /**
         * Singleton pattern - get the same instance
         */
        fun getDictionaryInstance(): DictionaryDataSource {
            return DICTIONARY_DATA_SOURCE ?: synchronized(this) {
                DictionaryDataSource(SpringRestInterface()).also {
                    DICTIONARY_DATA_SOURCE = it
                }
            }
        }
    }
}