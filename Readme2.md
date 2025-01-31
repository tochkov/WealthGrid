# WealthGrid

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/reference)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)

## Overview

WealthGrid is a modern Android portfolio tracking application that allows users to monitor and manage their investments across multiple asset classes in real-time. Built with the latest Android technologies and best practices, this project showcases modern Android development approaches and clean architecture principles.

## Features

- **Comprehensive Asset Tracking**
    - Real-time US stock market data
    - Historical price analysis
    - Company fundamentals and metrics
    - Portfolio position management
    - Custom trade tracking

- **Advanced Visualization**
    - Interactive price charts
    - Portfolio distribution analysis
    - Performance metrics visualization
    - Customizable dashboards

- **Market Intelligence**
    - Real-time stock news integration
    - Company fundamental analysis
    - Portfolio insights

## Tech Stack

### UI & Design
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit
- [Material Design 3](https://m3.material.io/) - Latest Material design implementation
- [Coil](https://coil-kt.github.io/coil/) - Image loading and caching
- [Vico](https://github.com/patrykandpatrick/vico) - Cartesian charts implementation
- [ComposeCharts](https://github.com/ehsannarmani/ComposeCharts) - Pie chart visualization

### Architecture & Data Flow
- Clean Architecture with MVVM presentation layer
- [Kotlin Flows](https://kotlinlang.org/docs/flow.html) - Reactive stream processing
- [Hilt](https://dagger.dev/hilt/) - Dependency injection
- [Room](https://developer.android.com/training/data-storage/room) - Local data persistence
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - In-app navigation

### Networking
- [Ktor](https://ktor.io/) - Websocket implementation for real-time data
- [Ktorfit](https://github.com/Foso/Ktorfit) - REST API client
- [EODHD API](https://eodhd.com/) - Financial data provider

### Testing
- [Truth](https://truth.dev/) - Fluent assertions framework
- [MockK](https://mockk.io/) - Mocking library for Kotlin

## Architecture

The application follows Clean Architecture principles, ensuring:
- Clear separation of concerns
- High testability
- Scalable and maintainable codebase
- Domain-driven design
- Dependency inversion

```
app/
├── data/           # Data layer: repositories, data sources
├── domain/         # Business logic and entities
├── presentation/   # UI layer: composables, view models
└── di/             # Dependency injection modules
```

## Getting Started

1. Clone the repository
```bash
git clone https://github.com/yourusername/wealthgrid.git
```

2. Add your EODHD API key to `local.properties`:
```properties
eodhd.api.key=your_api_key_here
```

3. Build and run the project using Android Studio

## Future Enhancements

Planned features and improvements:

1. **Technical Improvements**
    - Integration with [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for preferences
    - Implementation of [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for background tasks
    - [Compose Navigation Animation](https://google.github.io/accompanist/navigation-animation/) for smooth transitions
    - [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles) for performance optimization

2. **Feature Suggestions**
    - Cryptocurrency tracking integration
    - Portfolio rebalancing recommendations
    - Price alerts and notifications
    - Custom watchlists
    - Technical analysis indicators
    - Export/Import portfolio data
    - Widget support
    - Social features (sharing portfolios, following traders)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

- [EODHD](https://eodhd.com/) for providing financial data
- All the amazing open-source libraries that made this project possible

---

*Note: This project was developed as a portfolio piece to demonstrate proficiency with modern Android development technologies and best practices.*