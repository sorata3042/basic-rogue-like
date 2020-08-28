/** Monster determines where the character should move
 */
public class Monster
{
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Monster(Game game) 
    {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
    }

    /** Returns a legal move for the monster
     *  @return move
     */
    public Site move()
    {
        Site monster = game.getMonsterSite();
        Site rogue   = game.getRogueSite();
        Site move    = null;

        // take random legal move
        int n = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Site site = new Site(i, j);
                if (dungeon.isLegalMove(monster, site)) {
                    n++;
                    if (Math.random() <= 1.0 / n) move = site;
                }
            }
        }
        return move;
    }
    }

}