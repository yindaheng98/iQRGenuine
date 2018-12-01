import org.junit.Test;

import static org.junit.Assert.*;

import iQRGenuine.util.DataTool;

public class TestDataTool
{
    @Test
    public void testinitStatement()
    {
        String tests = DataTool.initStatement(50);
        System.out.println(tests);
    }

    @Test
    public void testinsertStatement()
    {
        String tests = DataTool.insertStatement("md5ttttt", "pktttttt");
        System.out.println(tests);
    }

    @Test
    public void testselectStatement()
    {
        String tests = DataTool.selectStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }

    @Test
    public void testverifyStatement()
    {
        String tests = DataTool.verifyStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }

    @Test
    public void testloginStatement()
    {
        String tests = DataTool.loginStatement("cdkttttt", "md5tttttt");
        System.out.println(tests);
    }
}
