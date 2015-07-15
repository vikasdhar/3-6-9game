# Quick start #

A simple game to test your wits, this app is. Use the menu bar to start a new one-player or two-player game. Then two players take turn marking one open square on a 9-by-9 board. Whoever make joining three, six, or nine squares scores those points, hence the name of the game. One may think of this game as a generalized tic-tac-toe. Obviously the player with the higher score wins.


# Details #

I would suggest taking a look at the "settings" of the game, accessible from the menu bar before playing for the first time.
In the settings, one may customize the names for player 1 and player 2. Only the first nine characters will be shown on the screen though.
More importantly,
there are options to choose a difficulty level for single player games. Level 2 would be suitable for a casual game. Level 4 can be hard as I am not sure if I can beat "the Logic" consistently. There is nothing intelligent about the computer play, just being thorough as an algorithm should be.

There are actually two sets of scoring rules, therefore two different games:
  * if the "original game rules" is checked, players can only score when exactly three, six, or nine squares are connected either vertically or horizontally. The player who scores continue until the move does not make a score. This is the fun part of the game as one wrong move can decide who wins. Incidentally, this app started as a remake of an old computer project during my high school years. The original algorithm are ported as level 3 "5th Grader" under this original game rules.
  * if the "original game rules" is unchecked , which is the default, players can score vertically, horizontally, and diagonally. However, players take turn after each move no matter the move scores or not. The game play seems easier to comprehend, especially to kids. Note that player 1 will have an advantage of about 18 points. I think the game play strategy are so different that it should be regarded as a separate game.


To start a new game, click the corresponding buttons on the menu bar.
There is also a resume game button in case the last game was interrupted and the app has to be restarted. Starting a new game also abort the on-going game. Changes in the options do not take effect until the next new game.


# More info #
As mentioned, this app is a remake of some really old codes. It is also a way to teach my kid programming skills.
The [project wiki](Project369.md) includes the origin and history of the project.
For fun, a half finished game is posted in the wiki as a [puzzle](PuzzleA.md).

Enjoy!

