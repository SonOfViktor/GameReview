package com.fairycompany.reviewer.model.util;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;

import java.time.LocalDate;

import static com.fairycompany.reviewer.controller.command.RequestParameter.ACTUAL_PAGE;
import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceUtilTest {
    private AutoCloseable closeable;

    @Mock
    private SessionRequestContent content;

    @InjectMocks
    private ServiceUtil serviceUtil = ServiceUtil.getInstance();

    @BeforeClass
    public void initMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterClass
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @DataProvider (name = "actualPageProvider")
    public static Object[][] provideActualPage() {
        return new Object[][] {
                {"1", 1},
                {"5", 5},
                {"0", 1},
                {null, 1},
                {"-5", 1},
                {"9223372036854775807", 9223372036854775807L},
                {"9223372036854775808", 1},
                {"<script>alert(123)</script>", 1}
        };
    }

    @DataProvider (name = "dateProvider")
    public static Object[][] provideDate() {
        return new Object[][] {
                {"2020-08-09", LocalDate.of(2020, 8, 9)},
                {"2021-12-31",  LocalDate.of(2021, 12, 31)},
                {"2021-02-30", LocalDate.of(2021, 2, 28)},
                {"2021-13-20",  LocalDate.of(1, 1, 1)},
                {"2021-00-01", LocalDate.of(1, 1, 1)},
                {"2021-12-00", LocalDate.of(1, 1, 1)},
                {"2021-12-32", LocalDate.of(1, 1, 1)},
                {"<script>alert(123)</script>", LocalDate.of(1, 1, 1)},
                {"", LocalDate.of(1, 1, 1)},
                {null, LocalDate.of(1, 1, 1)}
        };
    }


    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test (dataProvider = "dateProvider")
    public void testTakeDateFromString(String dateString, LocalDate expected) {
        LocalDate actual = serviceUtil.getDateFromString(dateString);

        assertEquals(actual, expected);

    }

    @Test (dataProvider = "actualPageProvider")
    public void testTakeActualPage(String actualPage, long expected) {
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn(actualPage);

        long actual = serviceUtil.takeActualPage(content);

        assertEquals(actual, expected);
    }
}