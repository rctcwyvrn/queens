Queens
---

Inspired by the collection of various chess players in [this video](https://www.youtube.com/watch?v=DpXy041BIlA), 
I decided to try to replicate it with another game, the game of the amazons.

Game of the amazons is a fun mix between chess and go, taking the idea of pieces and movement from chess 
and the idea of territory and capture from go.

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
Planned players:
1. Monte carlo based AI player
2. Straight
    *  Move and shoot straight only
3. Bishop
    *  Move and shot diagonally only
4. ScaredyCat
    *  Move all the queens to a corner and try to stay there
5. Everyone for themselves
    *  Move the 4 queens to their own corner and try to stay there
6. Evade like hell
    *  Try to keep each amazon in an open 3x3 square
    *  Other variants: Keep the diagonals as open as possible, keep the straights as open as possible
7. Symmetry
8. Firing squad
    *  Assemble into a line and attempt to "march" forward
9. Up close and personal
    *  Move each queen up to one of the enemy queens and follow it around
10.  Mob
    *  Move all four queens up to a target and box it in
11. Buddy system
    *  Use an AI for every other move, and just attempting to copy that move with a "buddy"
12. Neural network