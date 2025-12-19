# Risk Battle Roller - Progressive Web App (PWA)

A Progressive Web App version of the Risk Battle Dice Roller that can be installed on any device.

## Files Created

- `index.html` - Main HTML structure
- `styles.css` - Styling matching the Android app theme
- `app.js` - JavaScript logic for dice rolling (same logic as Android app)
- `manifest.json` - PWA manifest for installation
- `service-worker.js` - Service worker for offline support
- `dice/` - Folder containing dice SVG images (dice1.svg to dice6.svg)
- `icon.svg` - App icon (needs to be converted to PNG)

## Features

- Fullscreen experience (hides browser navigation on mobile)
- Works offline after first load
- Installable on any device (mobile, tablet, desktop)
- Same dice rolling logic as the Android app
- Battle history tracking
- Visual feedback with animations and flashing
- Responsive design

## How to Deploy

### Option 1: Simple Local Server (for testing)

1. Install a simple HTTP server:
   ```bash
   npm install -g http-server
   ```

2. Navigate to the DiceRoller directory and run:
   ```bash
   http-server -p 8080
   ```

3. Open browser to `http://localhost:8080`

### Option 2: Deploy to GitHub Pages (Free)

1. Create a new repository on GitHub
2. Push all PWA files to the repository
3. Go to repository Settings > Pages
4. Select main branch as source
5. Your app will be available at `https://yourusername.github.io/repository-name`

### Option 3: Deploy to Netlify (Free)

1. Create account at netlify.com
2. Drag and drop the folder to Netlify
3. Your app will be live instantly with HTTPS

### Option 4: Deploy to Vercel (Free)

1. Create account at vercel.com
2. Install Vercel CLI: `npm i -g vercel`
3. Run `vercel` in the project directory
4. Follow the prompts

## Creating App Icons

You need to create PNG icons from the `icon.svg` file:

### Using Online Tools:
1. Go to https://realfavicongenerator.net/ or https://www.pwabuilder.com/imageGenerator
2. Upload `icon.svg`
3. Generate icons and download
4. Replace `icon-192.png` and `icon-512.png`

### Using ImageMagick (command line):
```bash
convert -background none icon.svg -resize 192x192 icon-192.png
convert -background none icon.svg -resize 512x512 icon-512.png
```

## Installation on Devices

### Mobile (Android/iOS):
1. Open the web app in browser
2. Tap the "Add to Home Screen" option
3. The app will install like a native app

### Desktop (Chrome/Edge):
1. Open the web app
2. Click the install icon in the address bar
3. Click "Install"

## Differences from Android App

The PWA version has the same functionality as the Android app:
- Dice rolling with proper win/loss highlighting
- Battle history
- Casualty tracking
- Visual feedback and animations
- Fullscreen mode

## Browser Support

- Chrome/Edge: Full support
- Safari: Full support (iOS 11.3+)
- Firefox: Full support
- Opera: Full support

## Offline Support

After the first visit, the app works completely offline thanks to the service worker caching all necessary files.

## Updating the App

When you make changes:
1. Update the `CACHE_NAME` version in `service-worker.js`
2. Redeploy the files
3. Users will get the update automatically on next visit
