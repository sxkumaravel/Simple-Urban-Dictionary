package com.demo.simpleurbandictionary.data

import com.demo.simpleurbandictionary.model.SearchList

/**
 * Created on 2019-05-13.
 *
 * @author kumars
 */
interface RestInterface {
    fun searchDictionary(string: String): SearchList?
}