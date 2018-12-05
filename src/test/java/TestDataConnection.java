import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;
import iQRGenuine.util.DataConnection;

public class TestDataConnection
{
    @Test
    public void testlettuce()
    {
        try
        {
            RedisClient redisClient = RedisClient.create("redis://localhost:6379/");
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            RedisCommands<String, String> syncCommands = connection.sync();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }
    }

    @Test
    public void testinsertInfo()
    {
        try
        {
            DataConnection dc=new DataConnection();
            System.out.println(dc.insertInfo("testmd5","testpk"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public void testverifyInfo()
    {
        try
        {
            DataConnection dc=new DataConnection();
            System.out.println(dc.verifyInfo("1","123456"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testloginPass()
    {
        try
        {
            DataConnection dc=new DataConnection();
            System.out.println(dc.loginPass("123456","6543210"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
