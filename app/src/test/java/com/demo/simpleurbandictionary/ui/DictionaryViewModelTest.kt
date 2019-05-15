package com.demo.simpleurbandictionary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.simpleurbandictionary.data.DictionaryDataSource
import com.demo.simpleurbandictionary.data.RestInterface
import com.demo.simpleurbandictionary.data.SpringRestInterface
import com.demo.simpleurbandictionary.model.SearchList
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.Mockito
import java.io.File
import java.nio.file.Files

/**
 * Created on 2019-05-14.
 *
 * @author kumars
 */
class DictionaryViewModelTest {

    /**
     * In this test, LiveData will immediately post values without switching threads.
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    companion object {
        private const val GRADLE_STATIC_RELATIVE_PATH = "src/test/"
    }

    private lateinit var viewModel: DictionaryViewModel
    private lateinit var restInterface: RestInterface

    @Before
    fun setUp() {
        restInterface = Mockito.mock(SpringRestInterface::class.java)
        val dataSource = DictionaryDataSource(restInterface)
        viewModel = DictionaryViewModel(dataSource)
    }

    @Test
    fun sortDictionaryResultsSortByThumbsUp() {
        val configJSON = parseLocalFile("resources/valid-response.json")
        val fromJson = Gson().fromJson(configJSON, SearchList::class.java)

        viewModel.sortDictionaryResults(DictionaryViewModel.Sort.THUMBS_UP, fromJson.list)

        viewModel.searchResultLiveDate.observeForever {
            Assert.assertTrue(it?.size == 10)
        }

        val arrayList = viewModel.searchResultLiveDate.value

        var previous: Int = arrayList!![0].thumbsUp
        for (position in 1 until arrayList.size) {
            if (arrayList[position].thumbsUp > previous) {
                Assert.fail()
            }
            previous = arrayList[position].thumbsUp
        }
        // hurray - everything is sorted in order by thumbs up
    }

    @Test
    fun sortDictionaryResultsInValid() {
        val configJSON = parseLocalFile("resources/invalid-response.json")
        val fromJson = Gson().fromJson(configJSON, SearchList::class.java)

        viewModel.sortDictionaryResults(DictionaryViewModel.Sort.NONE, fromJson.list)

        viewModel.searchResultLiveDate.observeForever {
            Assert.assertTrue(it?.size == 0)
        }
    }

    @Test
    fun searchDictionary() {
        val configJSON = parseLocalFile("resources/valid-response.json")
        val fromJson = Gson().fromJson(configJSON, SearchList::class.java)

        val string = "world"

        Mockito.`when`(restInterface.searchDictionary(string)).thenReturn(fromJson)

        runBlocking {
            viewModel.searchDictionary(string)
        }
        // there is an issue with coroutines unit testing - need to fix it
        viewModel.searchResultLiveDate.observeForever {
            Assert.assertTrue(it?.size == 10)
        }
    }

    private fun parseLocalFile(fileName: String): String {
        val classLoader = this.javaClass.classLoader
        val resource = classLoader!!.getResource(fileName)
        val file = File(if (resource != null) resource.path else GRADLE_STATIC_RELATIVE_PATH + fileName)
        return String(Files.readAllBytes(file.toPath()), Charsets.UTF_8)
    }
}