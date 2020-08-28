/** Rogue determines where the character should move
 */
public class Rogue
{
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Rogue(Game game) 
    {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
    }

    /** Returns a legal move for Rogue
     *  @return move
     */
    public Site move (String direction)
    {
        Site rogue   = game.getRogueSite();
        Site move    = null;

        while (move == null) //to make sure a move can be made
        {
            i = rogue.i();
            j = rogue.j();

            if ( direction = "Up" )
            {
                j--;
            }
            else if ( direction = "Down" )
            {
                j++;
            }
            else if ( direction = "Left" )
            {
                i--;
            }
            else if ( direction = "Right" )
            {
                i++;
            }

            Site site = new Site( i, j );

            if (dungeon.isLegalMove(rogue, site))
            {
                move = site;
            }
        }
        return move;

    }
}