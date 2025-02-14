WealthGrid - Portfolio tracker app.

All your assets in one place - stocks, crypto and more.

you can see live and historical stock prices for the US Stock exchange
you can see company data and fundamentals
you can track stock positions by adding trades
you can visualize your portfolio in different ways
read news for your portfolio's stocks

This project is being develop with the sole purpose to update and improve my technical skills, using the latest technologies, best practices and industry standards.
Using eodhd.com API
Some of the stack covered:
- Jetpack Compose (My first attempt ever, so don't judge too harsh)
- Kotlin Flows - Again my first attempt
- Material3 (Because if I don't hurry they will release 4)
- Hilt for dependency injection
- Compose Navigation (Because, why things should be easy when they can be hard)
- Websockets for live data with Ktor
- Ktorfit for Rest api
- Room for caching and user portfolio data
- Coil for images and image caching
- https://github.com/patrykandpatrick/vico - for Cartesian charts
- https://github.com/ehsannarmani/ComposeCharts - for Pie charts



- Trying to be as close as possible to Clean Architecture, with proper MVVM as the presentation layer

- Tests decorated with Thruth and Mockk

It's naive from business logic prespective, but is a fully production app in techincal aspect

// The project uses Explicit Backing Fields,
// so to build and run the project you should enable the K2 Mode from Settings -> Languages & Frameworks -> Kotlin: Check the "Enable K2 Kotlin Mode"


TODODO 
Refactor compose navigation with all the listeners and stuff
Add live price data to Markets screen


This is a naive app. From business logic perspective this is not a real app, but from technical standpoint it aims to be production ready.


Ideas for project improvement:
Portfolio Chart
Portfolio Stats


Ideas if this app will be a real world product:

BE service should be implemented for sync - Firebase or custom

Trade Imports should be implemented 
//If the costs of AI services continue to fall//
a simple screenshot to json conversion could work:
Gemini prompt - "This is a list of trades. Extract them from the image with
#id
asset and exchange
currency
amount of shares
fill price
time 

return as an answer only json with the following format: {"",""}

Before any of the above - better rewrite for KMP











