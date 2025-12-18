// Game state
let attackerDiceCount = 1;
let defenderDiceCount = 1;
let attackerCasualties = 0;
let defenderCasualties = 0;
let battleHistory = [];
let battleNumber = 0;
let isBattling = false;

// Sound
const diceSound = new Audio('dice_roll.mp3');

// DOM Elements
const attackerButtons = document.querySelectorAll('.attacker-btn');
const defenderButtons = document.querySelectorAll('.defender-btn');
const battleButton = document.getElementById('battleButton');
const resetButton = document.getElementById('resetButton');
const historyButton = document.getElementById('historyButton');
const closeHistoryButton = document.getElementById('closeHistoryButton');
const resultText = document.getElementById('resultText');
const casualtiesText = document.getElementById('casualtiesText');
const historyText = document.getElementById('historyText');
const historyOverlay = document.getElementById('historyOverlay');

// Dice elements
const attackerDice = [
    document.getElementById('attackerDice1'),
    document.getElementById('attackerDice2'),
    document.getElementById('attackerDice3')
];
const defenderDice = [
    document.getElementById('defenderDice1'),
    document.getElementById('defenderDice2')
];

// Initialize
function init() {
    // Attacker dice count buttons
    attackerButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            attackerDiceCount = parseInt(btn.dataset.count);
            updateAttackerButtons();
            updateDiceVisibility();
        });
    });

    // Defender dice count buttons
    defenderButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            defenderDiceCount = parseInt(btn.dataset.count);
            updateDefenderButtons();
            updateDiceVisibility();
        });
    });

    // Battle button
    battleButton.addEventListener('click', startBattle);

    // Reset button
    resetButton.addEventListener('click', resetCasualties);

    // History buttons
    historyButton.addEventListener('click', showHistory);
    closeHistoryButton.addEventListener('click', hideHistory);

    // Initial state
    updateAttackerButtons();
    updateDefenderButtons();
    updateDiceVisibility();
}

function updateAttackerButtons() {
    attackerButtons.forEach(btn => {
        const count = parseInt(btn.dataset.count);
        if (count === attackerDiceCount) {
            btn.classList.add('selected');
        } else {
            btn.classList.remove('selected');
        }
    });
}

function updateDefenderButtons() {
    defenderButtons.forEach(btn => {
        const count = parseInt(btn.dataset.count);
        if (count === defenderDiceCount) {
            btn.classList.add('selected');
        } else {
            btn.classList.remove('selected');
        }
    });
}

function updateDiceVisibility() {
    // Update attacker dice
    attackerDice.forEach((dice, index) => {
        if (index < attackerDiceCount) {
            dice.classList.remove('hidden');
        } else {
            dice.classList.add('hidden');
        }
    });

    // Update defender dice
    defenderDice.forEach((dice, index) => {
        if (index < defenderDiceCount) {
            dice.classList.remove('hidden');
        } else {
            dice.classList.add('hidden');
        }
    });
}

function startBattle() {
    if (isBattling) return;

    isBattling = true;
    battleButton.disabled = true;
    resultText.textContent = '';

    // Play sound
    playDiceSound();

    // Generate final values
    const finalAttackerValues = [];
    const finalDefenderValues = [];

    for (let i = 0; i < attackerDiceCount; i++) {
        finalAttackerValues.push(Math.floor(Math.random() * 6) + 1);
    }
    for (let i = 0; i < defenderDiceCount; i++) {
        finalDefenderValues.push(Math.floor(Math.random() * 6) + 1);
    }

    // Animate rolling
    const animationDuration = 750;
    const frameDelay = 100;
    const startTime = Date.now();
    let flashState = false;

    const animationInterval = setInterval(() => {
        const elapsed = Date.now() - startTime;

        if (elapsed < animationDuration) {
            // Show random values and flash
            updateDiceValues(attackerDice, getRandomValues(attackerDiceCount));
            updateDiceValues(defenderDice, getRandomValues(defenderDiceCount));

            // Add rolling animation
            attackerDice.forEach((dice, i) => {
                if (i < attackerDiceCount) dice.classList.add('rolling');
            });
            defenderDice.forEach((dice, i) => {
                if (i < defenderDiceCount) dice.classList.add('rolling');
            });

            // Flash background
            flashState = !flashState;
            const attackerSection = document.querySelector('.attacker-section');
            const defenderSection = document.querySelector('.defender-section');

            if (flashState) {
                attackerSection.style.backgroundColor = '#EF5350';
                defenderSection.style.backgroundColor = '#42A5F5';
            } else {
                attackerSection.style.backgroundColor = '#C62828';
                defenderSection.style.backgroundColor = '#1565C0';
            }
        } else {
            // Stop animation
            clearInterval(animationInterval);

            // Show final values
            updateDiceValues(attackerDice, finalAttackerValues);
            updateDiceValues(defenderDice, finalDefenderValues);

            // Remove rolling animation
            attackerDice.forEach(dice => dice.classList.remove('rolling'));
            defenderDice.forEach(dice => dice.classList.remove('rolling'));

            // Reset background
            document.querySelector('.attacker-section').style.backgroundColor = '#C62828';
            document.querySelector('.defender-section').style.backgroundColor = '#1565C0';

            // Calculate result
            calculateBattleResult(finalAttackerValues, finalDefenderValues);

            isBattling = false;
            battleButton.disabled = false;
        }
    }, frameDelay);
}

function getRandomValues(count) {
    const values = [];
    for (let i = 0; i < count; i++) {
        values.push(Math.floor(Math.random() * 6) + 1);
    }
    return values;
}

function updateDiceValues(diceElements, values) {
    diceElements.forEach((dice, index) => {
        if (index < values.length) {
            dice.textContent = getDiceSymbol(values[index]);
        }
    });
}

function getDiceSymbol(value) {
    // Using Unicode dice symbols
    const symbols = ['⚀', '⚁', '⚂', '⚃', '⚄', '⚅'];
    return symbols[value - 1] || value;
}

function calculateBattleResult(attackerValues, defenderValues) {
    // Sort in descending order
    const attackerSorted = [...attackerValues].sort((a, b) => b - a);
    const defenderSorted = [...defenderValues].sort((a, b) => b - a);

    let attackerLosses = 0;
    let defenderLosses = 0;

    const comparisons = Math.min(attackerSorted.length, defenderSorted.length);

    for (let i = 0; i < comparisons; i++) {
        if (attackerSorted[i] > defenderSorted[i]) {
            defenderLosses++;
        } else {
            attackerLosses++;
        }
    }

    attackerCasualties += attackerLosses;
    defenderCasualties += defenderLosses;

    let result = '';
    if (attackerLosses > defenderLosses) {
        result = `Attacker loses ${attackerLosses} army!`;
    } else if (defenderLosses > attackerLosses) {
        result = `Defender loses ${defenderLosses} army!`;
    } else {
        result = `Both lose ${attackerLosses} army!`;
    }

    resultText.textContent = result;
    casualtiesText.textContent = `Total - Attacker: ${attackerCasualties} | Defender: ${defenderCasualties}`;

    // Add to history
    battleNumber++;
    const historyEntry = `Battle #${battleNumber}: ATK[${attackerSorted.join(',')}] vs DEF[${defenderSorted.join(',')}] → ${result}`;
    battleHistory.push(historyEntry);
}

function resetCasualties() {
    attackerCasualties = 0;
    defenderCasualties = 0;
    battleNumber = 0;
    battleHistory = [];
    casualtiesText.textContent = 'Total - Attacker: 0 | Defender: 0';
    resultText.textContent = '';
}

function showHistory() {
    if (battleHistory.length === 0) {
        historyText.textContent = 'No battles yet. Start rolling!';
    } else {
        historyText.textContent = battleHistory.slice().reverse().join('\n\n');
    }
    historyOverlay.classList.add('show');
}

function hideHistory() {
    historyOverlay.classList.remove('show');
}

function playDiceSound() {
    diceSound.currentTime = 0;
    diceSound.play().catch(err => {
        // Ignore errors (e.g., user hasn't interacted with page yet)
        console.log('Sound play failed:', err);
    });
}

// PWA Service Worker Registration
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('sw.js')
            .then(reg => console.log('Service Worker registered'))
            .catch(err => console.log('Service Worker registration failed:', err));
    });
}

// Initialize app
init();
