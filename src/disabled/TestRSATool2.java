import org.junit.Test;

import iQRGenuine.util.RSATool2;

public class TestRSATool2
{
    @Test
    public void testEncrypt()
    {
        RSATool2 rt = new RSATool2();
        try
        {
            String se= rt.encrypt("hdhdhdhhdhd");
            System.out.println(se);
            String de=rt.decrypt(se);
            System.out.println(de);
            System.out.println(rt.getPrivateKey());
            System.out.println(rt.getPublicKey());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
