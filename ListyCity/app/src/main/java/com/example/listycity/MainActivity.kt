package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {

    @Composable
    fun CityListScreen(
        cities: List<String>,
        onAddCity: (String) -> Unit,
        onRemoveCity: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var newCityName by remember {mutableStateOf("")}
        var selectedCity by remember {mutableStateOf<String?>(null)}
        // <String?> means that selectedCity can be of type String or null; (null) means that the variable is initialized as null (because we don't have a city selected at the start)

        Column(modifier = modifier.fillMaxSize()) {
            Row(modifier = modifier.padding(all = 12.dp)) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = {newCityName = it},
                    label = {Text("City name")},
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // add city button functionality
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                        }
                    }
                ) {
                    Text("Add city")
                }

                // remove city button functionality
                Button(
                    onClick = {
                        selectedCity?.let {city ->
                            onRemoveCity(city)
                            selectedCity = null
                        }
                    }
                ) {
                    Text("Remove city")
                }
            }

            // displays city in a row element on screen
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(cities) { city ->
                    CityRow(
                        city = city,
                        selected = selectedCity == city,
                        onClick = {
                            selectedCity = city
                        })
                }
            }
        }
    }

    @Composable
    fun CityRow(
        city: String,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selected,
                    onClick = onClick
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)

        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository() //create an instance of cityRepository
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = {cityRepository.addCity(it)},
                        onRemoveCity = {cityRepository.removeCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun removeCity(city: String) {
        _cities.remove(city)
    }
}