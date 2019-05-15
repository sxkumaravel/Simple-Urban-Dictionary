package com.demo.simpleurbandictionary.data

import com.demo.simpleurbandictionary.model.SearchList
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

/**
 * Created on 2019-05-12.
 *
 * @author kumars
 */
class SpringRestInterface: RestInterface {

    private var restTemplate: RestTemplate = RestTemplate()

    init {
        restTemplate.messageConverters.clear()
        restTemplate.messageConverters.add(GsonHttpMessageConverter())
    }

    override fun searchDictionary(string: String): SearchList {
        val toUriString = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("api.urbandictionary.com")
            .path("/v0/define?term={string}")
            .build()
            .expand(string)
            .toUriString()

        return restTemplate.getForObject(toUriString, SearchList::class.java)
    }
}