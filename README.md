# Rhythm Chess Engine

## About
This is a bitboard based chess engine coded in Java using Apache Maven and JavaFX. A logic-only version of this will be used as part of the COSC3143 Pi and Python project.

## Progress
I have been working on and off on this project for close to 1 year. As of 9/17/2024 I am still working towards passing my PERFT tests. Once this milestone is reached creating an engine that plays random moves should be the work of a few days. Modifying that engine to play well will be a significantly harder task. Right now the next goal is to ensure checks, pins, castling, and all other king related features are working. Part of why I am taking so long to pass my PERFT tests is because I had to make the GUI from scratch and because I have invested in optimizing the engine at a low level. In particular, the implementation of bitboards and Magic Bitboards was particularly challenging (see CompactMagicBitboard.java for an in-depth description of my solution) and required many weeks of research, trial, and error. I am confident, however, that this investment will pay dividends in search speed and increase my lookahead, improving the skill ceiling of the engine.

## Notables
While documentation is scarce, I have taken the time to explain various key features of my engine. The most notable of these can be found in the following files: CompactMagicBitboard, Move, and the calculatePinnedPieces() method in MoveGeneration. Much of the rest of the code is self evident or not worth describing in detail. 

## Resources
[Chess Programming Wiki](https://www.chessprogramming.org/Main_Page)

[Lichess Board Editor](https://lichess.org/editor)

[Bitboard Visualization](https://gekomad.github.io/Cinnamon/BitboardCalculator/)

[Talk Chess](https://talkchess.com/)

[Web Perft](https://analog-hors.github.io/webperft/)


##
*"Rhythm, rhythm, the rhythm of my day."*
