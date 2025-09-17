# Smart Daily Expense Tracker Module

## 🎯 Overview
A comprehensive expense tracking module built with Jetpack Compose, following MVVM architecture with Room database for local storage. This module helps small business owners track their daily expenses efficiently.

## ✨ Features

### 📱 Screens
1. **Dashboard** - Main landing screen with quick actions and today's summary
2. **Expense Entry** - Add new expenses with validation and real-time feedback
3. **Expense List** - View, filter, and manage expenses with grouping options
4. **Expense Report** - Analytics and insights with export functionality

### 🛠️ Core Functionality
- ✅ Add expenses with title, amount, category, and notes
- ✅ Real-time validation and error handling
- ✅ Category-based organization (Staff, Travel, Food, Utility)
- ✅ Date-based filtering and grouping
- ✅ Expense analytics and reporting
- ✅ Export functionality (PDF/CSV with real data)
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

### Installation
1. The module is already integrated into the main app
2. All dependencies are configured in `gradle/libs.versions.toml`
3. Database is automatically initialized on first app launch

### Usage
1. Launch the app to see the dashboard
2. Tap "Add Expense" to create new expense entries
3. Use "View Expenses" to see all expenses with filtering options
4. Access "Reports" for analytics and export features

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

## 🤝 Contributing
1. Follow the existing code style and architecture
2. Add tests for new features
3. Update documentation as needed
4. Ensure all linting rules pass

## 📄 License
This module is part of the FlowBook application and follows the same licensing terms.
