import org.testng.Assert;
import org.testng.annotations.Test;

public class Tests {

    @Test
    public void testFalse() {
        Assert.assertEquals(10, 20);
    }

    @Test
    public void testTrue() {
        Assert.assertEquals(20, 20);
    }
}
