## HTML, CSS, JS

This folder contains small frontend projects built with plain HTML, CSS, and JavaScript.

### Rock-Paper-Scissors
A simple browser game where you play Rock-Paper-Scissors against the computer. The game tracks your wins, losses, and ties using `localStorage` so your score persists across page refreshes.

- **Entry file**: `Rock-Paper-Scissor/Poject-01-rock-paper-scissor.html`
- **Styles**: `Rock-Paper-Scissor/styles/01-rock-paper-scissor.css`
- **Logic**: `Rock-Paper-Scissor/scripts/01-rock-paper-scissor.js`
- **Assets**: `Rock-Paper-Scissor/images/`

#### How to run
1. Open `Rock-Paper-Scissor/Poject-01-rock-paper-scissor.html` directly in any modern web browser, or
2. Use a local server (e.g., VS Code Live Server) and navigate to the HTML file.

No build step is required.

#### How to play
- Click a move button (rock, paper, or scissors).
- The computer picks a random move.
- The result and both moves are displayed on the page.
- The score is updated and saved in `localStorage`.
- Click "Reset score" to clear your persistent score.

#### Key JavaScript functions
- `playGame(playerMove)`: Runs a round, determines the result, updates score, and updates the UI.
- `pickComputerMove()`: Randomly selects the computer's move.
- `updateScoreElement()`: Renders the current score to the page.

#### Project structure
- `images/`: Emoji icons for the moves.
- `styles/01-rock-paper-scissor.css`: Basic styling and layout.
- `scripts/01-rock-paper-scissor.js`: Game logic and DOM updates.
- `Poject-01-rock-paper-scissor.html`: Wires up the buttons, result elements, and includes CSS/JS.

#### Notes
- The score is stored under the `score` key in `localStorage`.
- If you rename or move files, update the paths in the HTML accordingly.

#### Ideas for improvement
- Add keyboard controls for moves (R/P/S).
- Animate button clicks and results.
- Show a round history.
- Add tests and linting for the JavaScript.
