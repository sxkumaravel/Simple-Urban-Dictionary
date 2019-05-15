# Simple Urban Dictionary

1. Developed using Kotlin.
2. Used Android architectural components such as ViewModel, LiveData, DataBinding by following MVVM.
3. This app uses the open API https://api.urbandictionary.com to search the given word in the remote Urban Dictionary.

App flow:
1. Enter the word to search in Search View.
2. If found in the remote dictionary, it will display the list of results in Recycler View.
3. With the use of ViewModel, the application is safe with the configuration change. Since ViewModels are designed to hold and manage UI-related data in a life-cycle conscious way and allows data to survive configuration changes such as screen rotations.

Sort Function:
1. Sort by NONE (most relevant), Thumbs Up, Thumbs Down. 
2. Every time a word is searched it follows the chosen sorting type. If nothing is chosen it defaults to NONE.
3. Sort Type can be even changed after the search. In order to handle that case, the ViewModel is designed to keep the original copy unmodified so, that Sort can be set to NONE. 

Test Coverage:
1. Few JUnit test cases for ViewModel including for LiveData events.
2. Android Instrumentation test case for simple happy path.
