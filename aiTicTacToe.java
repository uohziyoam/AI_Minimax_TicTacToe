import java.util.*;
public class aiTicTacToe
{
    public int player; //1 for player 1 and 2 for player 2
    private List<List<positionTicTacToe>> lines;
    private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
    {
        //a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
        int index = position.x * 16 + position.y * 4 + position.z;
        return board.get(index).state;
    }
    public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
    {
        //TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
        // positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);

        // if (player == 1)
        // {
        //     do
        //     {
        //         Random rand = new Random();
        //         int x = rand.nextInt(4);
        //         int y = rand.nextInt(4);
        //         int z = rand.nextInt(4);
        //         myNextMove = new positionTicTacToe(x, y, z);
        //     }
        //     while(getStateOfPositionFromBoard(myNextMove, board) != 0);
        //     return myNextMove;
        // }


        return search(board, player);
    }

    public positionTicTacToe search(List<positionTicTacToe> board, int player)
    /* Function search() return the next move with highest score. It calls the function min_value
       to start the minimax algorithm. */
    {
        int best_score = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        positionTicTacToe best_action = null;
        for(positionTicTacToe i : getActions(board, player))
        {

                        int v;
            // if (player == 1)
            // {
            //     v = min_value(result(board, i), best_score, beta, 2);
            // }
            // else
            // {
                v = min_value(result(board, i), best_score, beta, 3);
            // }
            if(v > best_score)
            {
                best_score = v;
                best_action = i;
            }
        }


        // System.out.println(best_score);
        return best_action;
    }

    public List<positionTicTacToe> getActions(List<positionTicTacToe> board, int player)
    /* Function getActions() can compute all the possible future moves on the current board */
    {
        List<positionTicTacToe> results = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    positionTicTacToe p = new positionTicTacToe(i, j, k, player);
                    if(getStateOfPositionFromBoard(p, board) == 0)
                    {
                        results.add(p);
                    }
                }
            }
        }
        return results;
    }

    private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
    /* Function deepCopyATicTacToeBoard */
    {
        //deep copy of game boards
        List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
        for(int i = 0; i < board.size(); i++)
        {
            copiedBoard.add(new positionTicTacToe(board.get(i).x, board.get(i).y, board.get(i).z, board.get(i).state));
        }
        return copiedBoard;
    }

    private int oppo(int player)
    /* Function oppo() return the opposite player */
    {
        return player == 1 ? 2 : 1;
    }

    public List<positionTicTacToe> result(List<positionTicTacToe> board, positionTicTacToe action)
    /* Function result() return the newboard after a move has been placed on the board. */
    {
        List<positionTicTacToe> new_borad = deepCopyATicTacToeBoard(board);
        int index = action.x * 16 + action.y * 4 + action.z;
        new_borad.get(index).state = action.state;
        return new_borad;
    }

    public boolean terminal(List<positionTicTacToe> state)
    /* Function terminal() returns True once a player at that state wins. */
    {
        for (List<positionTicTacToe> line : lines)
        {
            if(one_win(line, state))
            {
                return true;
            }
        }
        return false;
    }

    private boolean one_win(List<positionTicTacToe> line, List<positionTicTacToe> state)
    /* Function one_win goes through every positions on the board, so */

    {
        int s = 0;
        positionTicTacToe position = line.get(0);
        int index = position.x * 16 + position.y * 4 + position.z;
        s = state.get(index).state;
        for (positionTicTacToe p : line)
        {
            index = p.x * 16 + p.y * 4 + p.z;
            if (s != state.get(index).state)
            {
                return false;
            }
        }
        return s != 0;
    }

    public int utility(List<positionTicTacToe> state, int player)
    /* Function utility() returns the score of the current node */
    {
        int a = count(state, player);
        int b = count(state, oppo(player));
        int score = a - b;
        // System.out.println(score);

        // int score = 0;
        // score = threeInRowCount(state,player);
        // score = score + twoInRowCount(state,player);
        return score;
    }

    public int utility2(List<positionTicTacToe> state, int player)
    /* Function utility2() returns the score of the current node */
    {
        int a = count(state, player);
        int b = count(state, oppo(player));
        int score = a - b;

        // int score = 0;
        score = score + threeInRowCount(state, player);
        score = score + twoInRowCount(state, player);
        return score;
    }

    private int count(List<positionTicTacToe> state, int player)
    /* Function count() returns counts of possible winning situations */
    {
        int count = 0;
        for (List<positionTicTacToe> line : lines)
        {
            if(possible(line, state, player))
            {
                count += 1;
            }
        }
        return count;
    }


    private boolean possible(List<positionTicTacToe> line, List<positionTicTacToe> state, int player)
    /* Function possible() returns if the current move can win or not */
    {

        boolean hasPlayer = false;
        for (positionTicTacToe p : line)
        {
            int index = p.x * 16 + p.y * 4 + p.z;
            int s = state.get(index).state; // current board
            if(s != 0 && s != player)
            {
                return false;
            }
            if(s != 0)
            {
                hasPlayer = true;
            }
        }
        return hasPlayer;
    }


    public int min_value(List<positionTicTacToe> state, int alpha, int beta, int depth)
    /* Function min_value is min part of minimax algorithm */
    {
        if(terminal(state) || depth == 0)
        {
            return utility2(state, player);
        }
        int oppo = oppo(player);
        int v = Integer.MAX_VALUE;
        for(positionTicTacToe action : getActions(state, oppo))
        {
            int nv = max_value(result(state, action), alpha, beta, depth - 1);
            v = nv < v ? nv : v;
            if(v <= alpha)
            {
                return v;
            }
            beta = v < beta ? v : beta;
        }
        return v;
    }

    public int max_value(List<positionTicTacToe> state, int alpha, int beta, int depth)
    /* Function max_value is max part of minimax algorithm */
    {
        if(terminal(state) || depth == 0)
        {
            return utility2(state, player);
        }
        int v = Integer.MIN_VALUE;
        for(positionTicTacToe action : getActions(state, player))
        {
            int nv = min_value(result(state, action), alpha, beta, depth - 1);
            v = nv > v ? nv : v;
            if(v >= beta)
            {
                return v;
            }
            alpha = alpha > v ? alpha : v;
        }
        return v;
    }

    public int twoInRowCount(List<positionTicTacToe> state, int player)
    {
        int count = 0;
        for (List<positionTicTacToe> line : lines)
        {
            count = count + twoInRow(line, state, player);
        }
        return count;

    }

    public int twoInRow(List<positionTicTacToe> line, List<positionTicTacToe> state, int player)
    /* Function twoInRow() returns the score of the current node under two in one row */

    {
        int score = 0;
        int add = 10;
        int half = 5;

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                for(int k = 0; k < 4; k++)
                {
                    int indexcur = i * 16 + k * 4 + k;
                    int indexnext = (i + 1) * 16 + k * 4 + k;

                    int curstate = state.get(indexcur).state;
                    int nextstate = state.get(indexnext).state;

                    if(curstate == nextstate)
                    {
                        if(curstate == 0)
                        {
                            score = score + 0;
                        }
                        else if(curstate == player)
                        {
                            score = score + add;
                        }
                        else
                        {
                            score = score - add;
                        }

                    }
                    else
                    {
                        if(curstate == player)
                        {
                            if(nextstate == 0)
                            {
                                score = score + add;
                            }
                        }
                        else if (curstate != 0 && curstate != player)
                        {
                            score = score - add;
                        }
                    }

                }
            }
        }

        return score;
    }

    public int threeInRowCount(List<positionTicTacToe> state, int player)
    {
        int count = 0;
        for (List<positionTicTacToe> line : lines)
        {
            count = count + twoInRow(line, state, player);
        }
        return count;

    }

    public int threeInRow(List<positionTicTacToe> line, List<positionTicTacToe> state, int player)
    /* Function threeInRow() returns the score of the current node under three in one row */

    {
        int score = 0;
        int add = 20;
        int half = 10;

        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                for(int k = 0; k < 4; k++)
                {
                    int indexcur = i * 16 + k * 4 + k;
                    int indexmiddle = (i + 1) * 16 + k * 4 + k;
                    int indexnext = (i + 2) * 16 + k * 4 + k;


                    int curstate = state.get(indexcur).state;
                    int middlestate = state.get(indexmiddle).state;
                    int nextstate = state.get(indexnext).state;

                    if(middlestate == player)
                    {
                        if(curstate == nextstate && curstate == player)
                        {
                            score = score  + add;
                        }
                        else if (curstate == nextstate && curstate == 0)
                        {
                            score = score + half;
                        }
                    }
                    else if(middlestate == oppo(player))
                    {
                        if(curstate == nextstate && curstate == oppo(player))
                        {
                            score = score  - add;
                        }
                        else
                        {
                            if(curstate == oppo(player) || nextstate == oppo(player))
                            {
                                score = score - half;
                            }
                        }


                    }


                }
            }
        }

        return score;
    }






    public void printBoardTicTacToe(List<positionTicTacToe> targetBoard)
    {
        //print each position on the board, uncomment this for debugging if necessary
        /*
        System.out.println("board:");
        System.out.println("board slots: "+board.size());
        for (int i=0;i<board.size();i++)
        {
         board.get(i).printPosition();
        }
        */

        //print in "graphical" display
        for (int i = 0; i < 4; i++)
        {
            System.out.println("level(z) " + i);
            for(int j = 0; j < 4; j++)
            {
                System.out.print("["); // boundary
                for(int k = 0; k < 4; k++)
                {
                    if (getStateOfPositionFromBoard(new positionTicTacToe(j, k, i), targetBoard) == 1)
                    {
                        System.out.print("X"); //print cross "X" for position marked by player 1
                    }
                    else if(getStateOfPositionFromBoard(new positionTicTacToe(j, k, i), targetBoard) == 2)
                    {
                        System.out.print("O"); //print cross "O" for position marked by player 2
                    }
                    else if(getStateOfPositionFromBoard(new positionTicTacToe(j, k, i), targetBoard) == 0)
                    {
                        System.out.print("_"); //print "_" if the position is not marked
                    }
                    if(k == 3)
                    {
                        System.out.print("]"); // boundary
                        System.out.println();





                    }


                }

            }
            System.out.println();
        }
    }



    private List<List<positionTicTacToe>> initializeWinningLines()
    {
        //create a list of winning line so that the game will "brute-force" check if a player satisfied any  winning condition(s).
        List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();

        //48 straight winning lines
        //z axis winning lines
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
            {
                List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
                oneWinCondtion.add(new positionTicTacToe(i, j, 0, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 1, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 2, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 3, -1));
                winningLines.add(oneWinCondtion);
            }
        //y axis winning lines
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
            {
                List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
                oneWinCondtion.add(new positionTicTacToe(i, 0, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 1, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 2, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 3, j, -1));
                winningLines.add(oneWinCondtion);
            }
        //x axis winning lines
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
            {
                List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
                oneWinCondtion.add(new positionTicTacToe(0, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(1, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(2, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(3, i, j, -1));
                winningLines.add(oneWinCondtion);
            }

        //12 main diagonal winning lines
        //xz plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(0, i, 0, -1));
            oneWinCondtion.add(new positionTicTacToe(1, i, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(2, i, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(3, i, 3, -1));
            winningLines.add(oneWinCondtion);
        }
        //yz plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(i, 0, 0, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 1, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 2, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 3, 3, -1));
            winningLines.add(oneWinCondtion);
        }
        //xy plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(0, 0, i, -1));
            oneWinCondtion.add(new positionTicTacToe(1, 1, i, -1));
            oneWinCondtion.add(new positionTicTacToe(2, 2, i, -1));
            oneWinCondtion.add(new positionTicTacToe(3, 3, i, -1));
            winningLines.add(oneWinCondtion);
        }

        //12 anti diagonal winning lines
        //xz plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(0, i, 3, -1));
            oneWinCondtion.add(new positionTicTacToe(1, i, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(2, i, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(3, i, 0, -1));
            winningLines.add(oneWinCondtion);
        }
        //yz plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(i, 0, 3, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 1, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 2, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 3, 0, -1));
            winningLines.add(oneWinCondtion);
        }
        //xy plane-4
        for(int i = 0; i < 4; i++)
        {
            List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
            oneWinCondtion.add(new positionTicTacToe(0, 3, i, -1));
            oneWinCondtion.add(new positionTicTacToe(1, 2, i, -1));
            oneWinCondtion.add(new positionTicTacToe(2, 1, i, -1));
            oneWinCondtion.add(new positionTicTacToe(3, 0, i, -1));
            winningLines.add(oneWinCondtion);
        }

        //4 additional diagonal winning lines
        List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
        oneWinCondtion.add(new positionTicTacToe(0, 0, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 1, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 2, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 3, 3, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList<positionTicTacToe>();
        oneWinCondtion.add(new positionTicTacToe(0, 0, 3, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 1, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 2, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 3, 0, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList<positionTicTacToe>();
        oneWinCondtion.add(new positionTicTacToe(3, 0, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 1, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 2, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(0, 3, 3, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList<positionTicTacToe>();
        oneWinCondtion.add(new positionTicTacToe(0, 3, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 2, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 1, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 0, 3, -1));
        winningLines.add(oneWinCondtion);

        return winningLines;

    }
    public aiTicTacToe(int setPlayer)
    {
        player = setPlayer;
        lines = initializeWinningLines();
    }
}