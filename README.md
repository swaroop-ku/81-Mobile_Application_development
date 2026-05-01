# NewsWorld - Modern Android News App

NewsWorld is a sleek, offline-first Android application that delivers the latest news from around the globe. Built with modern Android development practices, it offers a seamless reading experience with features like category filtering, multi-language support, and a robust bookmarking system.

## ✨ Features

- **Global News Feed**: Access top headlines and breaking news from various categories (General, Technology, Sports, etc.).
- **Smart Search**: Find specific articles using the powerful search functionality.
- **Offline Bookmarks**: Save articles to read later, even without an internet connection, powered by Room Database.
- **Multi-Language Support**: Filter news by language and country to get the content most relevant to you.
- **Premium UI/UX**:
  - Shimmer loading effects for a smooth content loading experience.
  - Lottie animations for interactive feedback.
  - Clean Material Design 3 components.
  - Interactive news details with in-app web views or browser options.
- **User Profiles**: Simple session management to personalize the experience.

## 🛠 Tech Stack

- **Language**: Java
- **Networking**: Retrofit 2 & OkHttp 3
- **Database**: Room (SQLite abstraction)
- **Image Loading**: Glide 4
- **UI Components**:
  - RecyclerView with infinite scrolling
  - ViewPager2 & TabLayout
  - Shimmer Layout (Facebook)
  - Lottie Animations (Airbnb)
- **Architecture**: Repository Pattern with Singleton Clients

## 🚀 Getting Started

To get this project up and running on your local machine, follow these steps:

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/NewsApp.git
```

### 2. Configure API Key
This app uses [NewsAPI.org](https://newsapi.org/) for fetching news.
1. Sign up at NewsAPI.org and get your free API key.
2. Create a file named `local.properties` in the root directory (if it doesn't exist).
3. Add your API key to the file:
   ```properties
   NEWS_API_KEY=your_actual_api_key_here
   ```

### 3. Build and Run
Open the project in **Android Studio** and click the **Run** button (or use `Shift + F10`).

## 📁 Project Structure

- `activities/`: Main entry points and screen logic.
- `fragments/`: Reusable UI components for Feed, Bookmarks, and Settings.
- `adapters/`: RecyclerView adapters for news lists.
- `models/`: Data classes for API responses and Database entities.
- `database/`: Room database configuration and DAOs.
- `utils/`: Networking clients, constants, and session managers.



