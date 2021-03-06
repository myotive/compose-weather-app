# Android Dev Challenge #4 - Weather App

<!--- Replace <OWNER> with your Github Username and <REPOSITORY> with the name of your repository. -->
<!--- You can find both of these in the url bar when you open your repository in github. -->
![Workflow result](https://github.com/myotive/compose-weather-app/workflows/Check/badge.svg)


## :scroll: Description
<!--- Describe your app in one or two sentences -->
For the fourth Android developer challenge with compose, we were tasked with creating a weather app. My sample pulls data from various APIs and utilizes compose to show the current weather for a user's location, as well as the projected forecast for several days.


## :bulb: Motivation and Context
<!--- Optionally point readers to interesting parts of your submission. -->
<!--- What are you especially proud of? -->

I really wanted to see how a bunch of android framework and library tools work in conjunction with compose, so I attempted to build a full feature app (at probably the sacrifice to pretty UI).

I used Dagger HILT for dependency injection, as well as Retrofit + coroutines for network operations.

This app takes the users last known address and pulls weather data for the location using [OpenWeatherMaps](https://openweathermap.org/api). Because we know the user's location, the app then attempts to make the UI more personable by fetching a random image from Unsplash using the user's City and State as search parameters.

It's important to note that you will need an API key from both unsplash and openweathermap to be able to run the code sample. Please create an account at the following locations and update the [corresponding API keys in the secrets.properties](https://github.com/myotive/compose-weather-app/blob/dev/secrets.properties) file.
* https://unsplash.com/developers

* https://openweathermap.org/api

## :camera_flash: Screenshots
<!-- You can add more screenshots here if you like -->
<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">&emsp;<img src="/results/screenshot_3.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```