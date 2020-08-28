/** Stores the locations on the 2D grid
 */
public class Site {

    private int i;
    private int j;

    public Site( int i, int j ) 
    {
        this.i = i;
        this.j = j;
    }

    /** Returns i from a Site object
     *  @return i
     */
    public int i() 
    {
        return i;
    }

    /** Returns j from a Site object
     *  @return j
     */   
    public int j() 
    {
        return j;
    }

    /** Gives the distance between this Site and that
     *  @return distanceNumber int
     */
    public int distance( Site that ) 
    {
        return Math.abs( this.i() - that.i() ) + Math.abs( this.j() - that.j())
    }

    /** Checks if this and that Site is the same
     */
    public boolean equals( Site that )
    {
        return ( distance( that ) == 0 )
    }
}
