package example.nebula;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class HelloTest {

    @Test
    @DisplayName("Returns hello")
    public void saysHello(TestInfo testInfo) {
        assertEquals("hello", /*thing*/"hello", "should return hello");
    }
}
