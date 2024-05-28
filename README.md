
# Summary
This app let customers search the weather data of a U.S city by names.

# Tech Stacks
Architecture Pattern: MVVM
Dependency Injection: Hilt
Network Library: Retrofit
Image Library: Glide
Concurrency: Kotlin Coroutine, Kotlin Flow
Unit Test: JUnit, Mockk

# Implemented Features
1. When a customer enters the name of a city and presses the Search button, the 5 day weather information will be displayed with a 3-hour step.
2. When a customer clicks on the icon to weather data for the current location, a UI will be displayed to require location permissions.
3. If a customer enters a city that cannot be found by the backend service, an error message will be displayed. 
4. When the app is launched, the last searched city and its weather data will be displayed.

# Screenshots
TODO: 

# Future Improvements
1. Given more time, we can implement autocomplete when a customer enters the city name for a better user experience.
2. Given more time, we can support search by zipcode.
3. Given more time, we can let users choose between metrics and imperial units.

# openweathermap APIs
- [5 day weather forecast](https://openweathermap.org/forecast5#builtin)
- [Weather Icons](https://openweathermap.org/weather-conditions#How-to-get-icon-URL)