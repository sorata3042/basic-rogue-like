import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/** I/O. Takes the dungeon text file as input and reads its contents
 */
class Input
{
    private BufferedReader br;

    /** Constructor for input
     *  @param file the file being used 
     */
    public Input( String file )
    {
        try
        {
            File input = new File ( file );
            // System.out.println ( input );
            FileReader inputReader = new FileReader ( input );
            // System.out.println ( inputReader );
            br = new BufferedReader ( inputReader );
            // System.out.println( br );
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }

    /** Reads a line
     *  @return str a String line from the BufferedReader br
     */
    public String readLine()
    {
        String str = null;
        try
        {
            str = br.readLine();
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
        return str;
    }

    /** Closes the buffered reader
     */
    public void close()
    {
        try
        {
            br.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
    public static void main (String args[])
    {
        Input in = new Input ( "dungeonA.txt" );
        System.out.println( in );
        String s;

        for ( int row = 0; row < 10; row++ )
        {
            System.out.println( in.readLine() );
            for ( int col = 0; col < 10; col++ )
            {
                //System.out.println( str.charAt( col ) );
            }
        }
        in.close();
    }
    */
}
