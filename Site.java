/** Stores the coordinate pair of an object on a 2D grid
 */
public class Site
{
    private int row;
    private int col;

    /** Constructor for input
     *  @param row the row coordinate on the grid
     *  @param col the col coordinate on the grid
     */
    public Site(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** Returns row from a Site object
     *  @return row from this
     */
    public int row()
    {
        return row;
    }

    /** Returns col from a Site object
     *  @return col from this
     */
    public int col()
    {
        return col;
    }

    /** Gives the distance between this Site and that
     *  @return distanceNumber int
     */
    public int distance( Site that )
    {
        int distance = Math.abs( this.row() - that.row() ) + Math.abs( this.col() - that.col());
        return distance;
    }

    /** Checks if this and that Site is the same
     *  @return the boolean if they are the same or not
     */
    public boolean equals( Site that )
    {
        return ( distance( that ) == 0 );
    }

    public String toString()
    {
        return "Site: " + this.row() + ", " + this.col();
    }
}
