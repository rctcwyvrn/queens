Queens
---

Inspired by the collection of various chess players in [this video](https://www.youtube.com/watch?v=DpXy041BIlA), 
I decided to try to replicate it with another game, the game of the amazons.

Game of the amazons is a fun mix between chess and go, taking the idea of pieces and movement from chess 
and the idea of territory and capture from go. [More about the game](https://www.youtube.com/watch?v=kjSOSeRZVNg)

Current players:
1. Random
    *  Plays random moves
2. xXx360noscopeheadshotsonlyxXx
    *  Moves to shoot arrows as close as possible to enemy queens
3. Assassin
    *  Very similar to the HeadshotsOnlyPlayer but chooses a single queen to target instead, switching to new targets 
    as they become unreachable
4. RMCQueenAI
    *  Uses my forked amazons ai from https://github.com/rctcwyvrn/game-of-the-amazons-ai
    *  Original AI author: https://github.com/rmcqueen
5. Invader
    *  Uses the Invader2.1 found here http://www.csun.edu/~lorentz/amazon.htm
6. Diluted AI players
    *  Play the move from an AI x% of the time, playing random moves the rest of the time
	*  Invader_50 and Invader_10
7. Straight
    *  Move and shoot straight only
8. Symmetry
9. Up close and personal
    *  Move each queen up to one of the enemy queens and follow it around
10. Mob
    *  Move all four queens up to a target and box it in
11. Corner (Everyone for themselves)
    *  Move the 4 queens to their own corner and try to stay there

Results:
```
Player			Elo
Corner			1014
Mob				1035
Straight		1189
Random			1245
HorizontalSym	1275
DiagonalSym		1289
VerticalSym		1345
UpClose			1355
Invader_10		1417
AlternateCorner	1429
Assasin			1694
Headshot		1762
Invader_50		2015
RMCQueenAI		2131
Invader			2297
```

Planned players:
1. Monte carlo based AI player
    *  https://github.com/EthanWelsh/Game-of-the-Amazons
2. Bishop
    *  Move and shot diagonally only
3. ScaredyCat
    *  Move all the queens to a corner and try to stay there
5. Evade like hell
    *  Try to keep each amazon in an open 3x3 square
    *  Other variants: Keep the diagonals as open as possible, keep the straights as open as possible
6. Firing squad
    *  Assemble into a line and attempt to "march" forward
7. Buddy system
    *  Use an AI for every other move, and just attempting to copy that move with a "buddy"
8. Neural network