import org.junit.Test;

import static org.junit.Assert.*;

import iQRGenuine.util.DataConnection;

public class TestDataConnection
{
        @Test
        public void testinsertStatement()
        {
            String tests = DataConnection.insertStatement("md5ttttt", "pktttttt");
            System.out.println(tests);
            assertEquals("INSERT INTO public_keys(md5_info,public_key)VALUES('md5ttttt','pktttttt')",
                    tests);
        }
    @Test
    public void testselectStatement()
    {
        String tests = DataConnection.selectStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }

    @Test
    public void testverifyStatement()
    {
        String tests = DataConnection.verifyStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }

    @Test
    public void testloginStatement()
    {
        String tests = DataConnection.loginStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }
}
