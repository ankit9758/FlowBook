# FlowBook - Smart Daily Expense Tracker

## 🎯 Overview
A comprehensive expense tracking app built with Jetpack Compose, following MVVM architecture with Room database for local storage. This app helps small business owners track their daily expenses efficiently with real-time data, auto-navigation, and smart reporting features.

## ✨ Features

### 📱 Screens
1. **Dashboard** - Main landing screen with quick actions and today's summary
2. **Expense Entry** - Add new expenses with validation and real-time feedback
3. **Expense List** - View, filter, and manage expenses with grouping options
4. **Expense Report** - Analytics and insights with export functionality

### 🛠️ Core Functionality
- ✅ Add expenses with title, amount, category, and notes
- ✅ Real-time validation and error handling
- ✅ Category-based organization (Staff, Travel, Food, Utility, Other)
- ✅ Date-based filtering and grouping
- ✅ Expense analytics and reporting
- ✅ Export functionality (PDF/CSV with real data)
- ✅ Auto-navigation after expense addition
- ✅ Dashboard auto-refresh with latest data
- ✅ Smart reporting (recent expenses on top, no zero-expense days)
- ✅ Dark/Light theme support
- ✅ Smooth animations and transitions

### 🏗️ Architecture
- **MVVM Pattern** with ViewModels and StateFlow
- **Room Database** for local data persistence
- **Manual Dependency Injection** for ViewModels
- **Navigation Compose** for screen navigation
- **Material 3** design system

## 📦 Dependencies Added
- Navigation Compose
- ViewModel Compose
- Room Database
- Manual DI
- Material 3

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.8+
- Compose BOM 2024.09.00
- Android SDK 24+ (Android 7.0+)

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on emulator or device

### Usage
1. **Launch the app** to see the dashboard
2. **Add Expense** - Tap "Add Expense" to create new entries
   - Fill in title, amount, category, and optional notes
   - App automatically navigates back after successful addition
3. **View Expenses** - See all expenses with filtering and grouping options
4. **Reports** - Access analytics with smart reporting features
   - Recent expenses shown first
   - Zero-expense days are filtered out
   - Export to PDF/CSV with real data

## 🎨 UI Components

### Reusable Components
- `CategorySelector` - Interactive category selection with icons
- `AmountInput` - Formatted currency input with validation
- `ExpenseCard` - Display expense information with actions
- `TotalSummaryCard` - Summary statistics display

### Animations
- Smooth card animations for expense entries
- Loading states with animated indicators
- Success/error message animations
- Counter animations for statistics

## 📊 Data Models

### Expense
```kotlin
data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val notes: String? = null,
    val receiptImagePath: String? = null,
    val createdAt: Date = Date()
)
```

### Categories
- Staff (👥)
- Travel (✈️)
- Food (🍽️)
- Utility (⚡)

## 🔧 Configuration

### Database
- Database name: `expense_database`
- Version: 1
- Auto-migration enabled

### Theme
- Material 3 design system
- Green color scheme for financial app
- Dynamic color support for Android 12+

## 🧪 Testing
- Unit tests for ViewModels
- UI tests for critical user flows
- Database tests for data integrity

## 📱 Screenshots
*Screenshots would be added here showing the different screens and features*

## 🔮 Future Enhancements
- Receipt image capture and storage
- Cloud sync capabilities
- Advanced analytics with charts
- Budget tracking and alerts
- Multi-currency support
- Expense templates and recurring expenses
- Data backup and restore
- Expense sharing and collaboration

## 🛠️ Development

### Project Structure
```
app/src/main/java/com/example/flowbook/
├── data/
│   ├── model/          # Data models (Expense, ExpenseCategory)
│   ├── database/       # Room database setup
│   └── repository/     # Data access layer
├── ui/
│   ├── screens/        # Main UI screens
│   ├── components/     # Reusable UI components
│   ├── viewmodel/      # ViewModels for each screen
│   ├── animations/     # Custom animations
│   └── theme/          # App theming
└── navigation/         # Navigation setup
```

### Key Features Implemented
- ✅ **Real Data Only**: No mock data, everything from Room database
- ✅ **Auto-Navigation**: Seamless flow after expense addition
- ✅ **Smart Reporting**: Recent expenses first, filtered zero-days
- ✅ **Live Updates**: Dashboard refreshes with latest data
- ✅ **Export Functions**: PDF/CSV with actual expense data

## 🤝 Contributing
1. Follow the existing code style and architecture
2. Add tests for new features
3. Update documentation as needed
4. Ensure all linting rules pass
5. Test on multiple screen sizes

## 📄 License
This project is part of the FlowBook application and follows the same licensing terms.
