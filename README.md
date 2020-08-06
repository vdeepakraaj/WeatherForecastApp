# WeatherForecastApp

Simple forecast app, which uses some APIs to fetch 5 day forecast data from the OpenWeatherMap.

1) Used FusedLocationProviderClient to reteieve user's location
2) Used In Memory database(Room) to cache and fetch forecast if the last fetch is below 5 hours
3) App refreshes automatically if user moves 1000meter


Libraries and tools 🛠

LiveData
Data Binding
RoomDB
Dagger 2
Retrofit
OkHttp
FusedLocationProviderClient
ViewModel

Instructions:
Pull the code from repository and install using Android studio

Output:

![Screenshot_20200806-190822_Weather Forecast](https://user-images.githubusercontent.com/17528632/89525978-9f807780-d819-11ea-8886-766ca30c8695.jpg)



Need to Do:
1) Allow user to search for a city and find the forecast
2) Need to alert the user if the forecast is to be raining within 2 hour
