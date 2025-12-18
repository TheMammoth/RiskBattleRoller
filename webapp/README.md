# Risk Battle Roller - Web App

A Progressive Web App (PWA) version of the Risk Battle Roller that works on **all devices** - iPhone, Android, Windows, Mac, and more!

## Features

- 🎲 Roll 1-3 attacker dice and 1-2 defender dice
- 🔊 Realistic dice rolling sound effects
- ✨ Animated dice rolls with visual feedback
- 📜 Battle history tracking
- 💀 Casualty counter
- 📱 Works offline once installed
- 🍎 Install on iPhone home screen
- 🤖 Install on Android home screen
- 💻 Works in any browser

## How to Use

### Option 1: Open in Browser
Simply open `index.html` in any web browser!

### Option 2: Deploy Online (Recommended for Sharing)

Deploy to **GitHub Pages** (Free):

1. Copy all files from the `webapp` folder to your repository
2. Go to GitHub repository Settings > Pages
3. Select branch `main` and folder `/webapp` (or root if files are in root)
4. Click Save
5. Your app will be live at: `https://yourusername.github.io/RiskBattleRoller/`

**Other free hosting options:**
- [Netlify](https://www.netlify.com/) - Drag & drop deploy
- [Vercel](https://vercel.com/) - Free hosting
- [GitHub Pages](https://pages.github.com/) - GitHub integration

### Option 3: Install as App on Phone

**On iPhone:**
1. Open the web app in Safari
2. Tap the Share button (square with arrow)
3. Scroll down and tap "Add to Home Screen"
4. Tap "Add"
5. The app icon will appear on your home screen!

**On Android:**
1. Open the web app in Chrome
2. Tap the menu (three dots)
3. Tap "Add to Home Screen" or "Install App"
4. Tap "Add"
5. The app icon will appear on your home screen!

## Files

- `index.html` - Main HTML structure
- `styles.css` - All styling and animations
- `app.js` - Game logic and functionality
- `dice_roll.mp3` - Sound effect
- `manifest.json` - PWA configuration
- `sw.js` - Service worker for offline support
- `icon-192.png` - App icon (192x192)
- `icon-512.png` - App icon (512x512)

## Development

To test locally:
1. Use a local web server (don't just open the HTML file)
2. Python: `python -m http.server 8000`
3. Or use VS Code Live Server extension
4. Open `http://localhost:8000`

## Browser Support

Works on all modern browsers:
- ✅ Chrome (Desktop & Mobile)
- ✅ Safari (Desktop & Mobile)
- ✅ Firefox
- ✅ Edge
- ✅ Opera

## License

Free to use and modify!
