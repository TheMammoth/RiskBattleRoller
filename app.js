// State management
let state = {
    attackerDiceCount: 2,
    defenderDiceCount: 2,
    attackerDiceValues: [],
    defenderDiceValues: [],
    attackerCasualties: 0,
    defenderCasualties: 0,
    battleHistory: [],
    battleNumber: 0,
    isAnimating: false
};

// DOM Elements
const attackerDiceContainer = document.getElementById('attackerDice');
const defenderDiceContainer = document.getElementById('defenderDice');
const rollBtn = document.getElementById('rollBtn');
const resetBtn = document.getElementById('resetBtn');
const historyBtn = document.getElementById('historyBtn');
const attackerCasualtiesEl = document.getElementById('attackerCasualties');
const defenderCasualtiesEl = document.getElementById('defenderCasualties');
const resultTextEl = document.getElementById('resultText');
const historyOverlay = document.getElementById('historyOverlay');
const closeHistoryBtn = document.getElementById('closeHistoryBtn');
const historyList = document.getElementById('historyList');
const attackerSection = document.getElementById('attackerSection');
const defenderSection = document.getElementById('defenderSection');

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    setupEventListeners();
    updateDiceDisplay();
    registerServiceWorker();
});

// Register Service Worker for PWA
function registerServiceWorker() {
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('service-worker.js')
            .then(registration => console.log('Service Worker registered'))
            .catch(error => console.log('Service Worker registration failed:', error));
    }
}

// Event Listeners
function setupEventListeners() {
    // Dice selection buttons
    document.querySelectorAll('.dice-btn').forEach(btn => {
        btn.addEventListener('click', handleDiceSelection);
    });

    // Roll button
    rollBtn.addEventListener('click', rollDice);

    // Reset button
    resetBtn.addEventListener('click', resetGame);

    // History button
    historyBtn.addEventListener('click', () => {
        historyOverlay.classList.remove('hidden');
        renderHistory();
    });

    // Close history
    closeHistoryBtn.addEventListener('click', () => {
        historyOverlay.classList.add('hidden');
    });

    // Close history on overlay click
    historyOverlay.addEventListener('click', (e) => {
        if (e.target === historyOverlay) {
            historyOverlay.classList.add('hidden');
        }
    });
}

// Handle dice count selection
function handleDiceSelection(e) {
    const btn = e.currentTarget;
    const side = btn.dataset.side;
    const count = parseInt(btn.dataset.count);

    // Update active button
    document.querySelectorAll(`.dice-btn[data-side="${side}"]`).forEach(b => {
        b.classList.remove('active');
    });
    btn.classList.add('active');

    // Update state
    if (side === 'attacker') {
        state.attackerDiceCount = count;
    } else {
        state.defenderDiceCount = count;
    }

    updateDiceDisplay();
}

// Update dice visibility
function updateDiceDisplay() {
    // Update attacker dice
    const attackerDice = attackerDiceContainer.querySelectorAll('.dice');
    attackerDice.forEach((dice, index) => {
        if (index < state.attackerDiceCount) {
            dice.classList.remove('hidden');
        } else {
            dice.classList.add('hidden');
        }
        dice.classList.remove('winner', 'loser');
    });

    // Update defender dice
    const defenderDice = defenderDiceContainer.querySelectorAll('.dice');
    defenderDice.forEach((dice, index) => {
        if (index < state.defenderDiceCount) {
            dice.classList.remove('hidden');
        } else {
            dice.classList.add('hidden');
        }
        dice.classList.remove('winner', 'loser');
    });
}

// Roll dice
function rollDice() {
    if (state.isAnimating) return;

    state.isAnimating = true;
    rollBtn.disabled = true;

    // Clear previous results
    clearResults();

    // Animate rolling
    animateRolling();

    // Generate random values after animation
    setTimeout(() => {
        // Generate dice values
        state.attackerDiceValues = generateDiceValues(state.attackerDiceCount);
        state.defenderDiceValues = generateDiceValues(state.defenderDiceCount);

        // Calculate battle result
        const result = calculateBattle(state.attackerDiceValues, state.defenderDiceValues);

        // Update casualties
        state.attackerCasualties += result.attackerLosses;
        state.defenderCasualties += result.defenderLosses;
        state.battleNumber++;

        // Add to history
        state.battleHistory.unshift({
            battleNumber: state.battleNumber,
            attackerValues: [...state.attackerDiceValues],
            defenderValues: [...state.defenderDiceValues],
            result: result.resultText
        });

        // Update UI
        updateDiceImages(result);
        updateCasualties();
        resultTextEl.textContent = result.resultText;

        // Flash effect
        flashWinner(result);

        state.isAnimating = false;
        rollBtn.disabled = false;
    }, 500);
}

// Generate random dice values
function generateDiceValues(count) {
    return Array.from({ length: count }, () => Math.floor(Math.random() * 6) + 1);
}

// Calculate battle using same logic as Android app
function calculateBattle(attackerValues, defenderValues) {
    // Create pairs of (value, originalIndex) and sort by value descending
    const attackerIndexed = attackerValues
        .map((value, index) => ({ value, index }))
        .sort((a, b) => b.value - a.value);

    const defenderIndexed = defenderValues
        .map((value, index) => ({ value, index }))
        .sort((a, b) => b.value - a.value);

    const attackerWins = Array(attackerValues.length).fill(false);
    const defenderWins = Array(defenderValues.length).fill(false);

    let attackerLosses = 0;
    let defenderLosses = 0;

    const comparisons = Math.min(attackerIndexed.length, defenderIndexed.length);

    for (let i = 0; i < comparisons; i++) {
        const attackerValue = attackerIndexed[i].value;
        const attackerOriginalIndex = attackerIndexed[i].index;
        const defenderValue = defenderIndexed[i].value;
        const defenderOriginalIndex = defenderIndexed[i].index;

        if (attackerValue > defenderValue) {
            defenderLosses++;
            attackerWins[attackerOriginalIndex] = true;
            defenderWins[defenderOriginalIndex] = false;
        } else {
            // Ties favor defender in Risk
            attackerLosses++;
            attackerWins[attackerOriginalIndex] = false;
            defenderWins[defenderOriginalIndex] = true;
        }
    }

    const resultText = attackerLosses > defenderLosses
        ? `Attacker loses ${attackerLosses} army!`
        : defenderLosses > attackerLosses
            ? `Defender loses ${defenderLosses} army!`
            : `Both lose ${attackerLosses} army!`;

    return {
        attackerLosses,
        defenderLosses,
        attackerWins,
        defenderWins,
        resultText
    };
}

// Animate rolling
function animateRolling() {
    const attackerDice = attackerDiceContainer.querySelectorAll('.dice:not(.hidden)');
    const defenderDice = defenderDiceContainer.querySelectorAll('.dice:not(.hidden)');

    attackerDice.forEach(dice => dice.classList.add('rolling'));
    defenderDice.forEach(dice => dice.classList.add('rolling'));

    setTimeout(() => {
        attackerDice.forEach(dice => dice.classList.remove('rolling'));
        defenderDice.forEach(dice => dice.classList.remove('rolling'));
    }, 500);
}

// Update dice images and highlight winners/losers
function updateDiceImages(result) {
    // Update attacker dice
    const attackerDice = attackerDiceContainer.querySelectorAll('.dice');
    state.attackerDiceValues.forEach((value, index) => {
        const dice = attackerDice[index];
        const img = dice.querySelector('img');
        img.src = `dice/dice${value}.svg`;
        img.alt = `Dice ${value}`;

        dice.classList.remove('winner', 'loser');
        if (result.attackerWins[index]) {
            dice.classList.add('winner');
        } else if (index < result.attackerWins.length) {
            dice.classList.add('loser');
        }
    });

    // Update defender dice
    const defenderDice = defenderDiceContainer.querySelectorAll('.dice');
    state.defenderDiceValues.forEach((value, index) => {
        const dice = defenderDice[index];
        const img = dice.querySelector('img');
        img.src = `dice/dice${value}.svg`;
        img.alt = `Dice ${value}`;

        dice.classList.remove('winner', 'loser');
        if (result.defenderWins[index]) {
            dice.classList.add('winner');
        } else if (index < result.defenderWins.length) {
            dice.classList.add('loser');
        }
    });
}

// Flash winner section
function flashWinner(result) {
    if (result.attackerLosses < result.defenderLosses) {
        // Attacker wins
        flashSection(attackerSection);
    } else if (result.defenderLosses < result.attackerLosses) {
        // Defender wins
        flashSection(defenderSection);
    }
}

function flashSection(section) {
    let flashCount = 0;
    const flashInterval = setInterval(() => {
        section.classList.toggle('flash');
        flashCount++;
        if (flashCount >= 6) {
            clearInterval(flashInterval);
            section.classList.remove('flash');
        }
    }, 100);
}

// Update casualties display
function updateCasualties() {
    attackerCasualtiesEl.textContent = state.attackerCasualties;
    defenderCasualtiesEl.textContent = state.defenderCasualties;
}

// Clear results
function clearResults() {
    resultTextEl.textContent = '';
    const allDice = document.querySelectorAll('.dice');
    allDice.forEach(dice => {
        dice.classList.remove('winner', 'loser');
    });
}

// Reset game
function resetGame() {
    state.attackerCasualties = 0;
    state.defenderCasualties = 0;
    state.battleHistory = [];
    state.battleNumber = 0;
    state.attackerDiceValues = [];
    state.defenderDiceValues = [];

    updateCasualties();
    resultTextEl.textContent = '';
    clearResults();

    // Reset dice images to 1
    const allDice = document.querySelectorAll('.dice img');
    allDice.forEach(img => {
        img.src = 'dice/dice1.svg';
        img.alt = 'Dice 1';
    });
}

// Render battle history
function renderHistory() {
    if (state.battleHistory.length === 0) {
        historyList.innerHTML = '<p style="text-align: center; color: #999;">No battles yet</p>';
        return;
    }

    historyList.innerHTML = state.battleHistory.map(entry => `
        <div class="history-item">
            <div class="history-item-header">Battle #${entry.battleNumber}</div>
            <div class="history-dice-row">
                <div class="history-dice-group">
                    <div class="history-dice-label" style="color: #d32f2f;">Attacker</div>
                    <div class="history-dice-values">
                        ${entry.attackerValues.map(v => `<div class="history-dice-value" style="color: #d32f2f;">${v}</div>`).join('')}
                    </div>
                </div>
                <div class="history-dice-group">
                    <div class="history-dice-label" style="color: #1976d2;">Defender</div>
                    <div class="history-dice-values">
                        ${entry.defenderValues.map(v => `<div class="history-dice-value" style="color: #1976d2;">${v}</div>`).join('')}
                    </div>
                </div>
            </div>
            <div class="history-result">${entry.result}</div>
        </div>
    `).join('');
}
