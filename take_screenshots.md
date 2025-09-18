# How to Take Screenshots for FlowBook README

## 📱 Taking Screenshots

### Method 1: Android Studio Emulator
1. Open Android Studio
2. Run the FlowBook app on the emulator
3. Navigate to each screen you want to capture
4. Use the camera icon in the emulator toolbar to take screenshots
5. Save them in the `screenshots/` directory

### Method 2: Physical Device
1. Install the app on your Android device
2. Navigate to each screen
3. Take screenshots using your device's screenshot function
4. Transfer the images to your computer
5. Save them in the `screenshots/` directory

### Method 3: ADB Command (if using emulator)
```bash
# Take screenshot of current screen
adb exec-out screencap -p > screenshots/current_screen.png

# Take multiple screenshots
adb exec-out screencap -p > screenshots/dashboard.png
# Navigate to add expense screen, then:
adb exec-out screencap -p > screenshots/add_expense.png
# Navigate to expense list screen, then:
adb exec-out screencap -p > screenshots/expense_list.png
```

## 📸 Required Screenshots

1. **dashboard.png** - Main dashboard with today's summary
2. **add_expense.png** - Add expense screen with gradient button
3. **expense_list.png** - Expense list with gradient cards
4. **app_icon.png** - App icon on home screen

## 🎯 Screenshot Tips

- Use high resolution (at least 1080x1920)
- Ensure good lighting and clear visibility
- Show the app's gradient theme and animations
- Capture the most important features of each screen
- Make sure text is readable in the screenshots

## 📁 File Structure
```
screenshots/
├── dashboard.png
├── add_expense.png
├── expense_list.png
└── app_icon.png
```

Once you add these images, they will automatically appear in the README!
