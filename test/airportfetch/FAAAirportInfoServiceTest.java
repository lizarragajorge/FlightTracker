package airportfetch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FAAAirportInfoServiceTest
{                                          

    AirportInfoExtractor airportInfoExtractor;
    FAAAirportInfoService infoService;
    AirportInfo hyphenAirport1;
    AirportInfo dfwDallasTX;
    AirportInfo lgaNewYorkNY;
    FAAAirportInfoService fAAAirportInfoServiceWithConnectionFunctionMocked;
    IOException ioException;

    @Before
    public void setUp()
    {
        airportInfoExtractor = new AirportInfoExtractor();
        infoService = new FAAAirportInfoService();
        airportInfoExtractor.setAirportInfoService(infoService);
        fAAAirportInfoServiceWithConnectionFunctionMocked = Mockito.spy(FAAAirportInfoService.class);
        ioException = new IOException();

        dfwDallasTX = new AirportInfo("Dallas-Ft Worth", "Texas", "DFW", "Dallas/Ft Worth International", "64.0 F (18.8 C)", false);
        lgaNewYorkNY = new AirportInfo("New York", "New York", "LGA", "LaGuardia Airport", "73.0 F (22.8 C)", false);
        hyphenAirport1 = new AirportInfo("---", "---", "---", "---", "---", false);

    }

    @Test
    public void getURLForAirportCodeReturnsExpectedURL()
    {
        assertEquals("http://services.faa.gov/airport/status/DFW?format=application/json", infoService.getURLForAirportCode("DFW"));
    }

    @Test
    public void airportInfoServiceReturnsCorrectIATAForSampleAirportCode()
    {
        String expectedIATA = dfwDallasTX.getIATA();

        assertEquals(expectedIATA, infoService.getAirportInfo("DFW").getIATA());
    }

    @Test
    public void airportInfoServiceReturnsHyphensWhenNetworkError()
    {
        assertEquals(hyphenAirport1.getIATA(), infoService.getAirportInfo("ZZZ").getIATA());
    }

    @Test
    public void airportInfoServiceReturnsHyphensWhenMalformedURL()
    {
        assertEquals(hyphenAirport1.getIATA(), infoService.getAirportInfo(".com/").getIATA());
    }

    @Test
    public void airportInfoServiceReturnsCorrectAirportInfoWhenCalledFromAirportInfoExtractor()
    {
        List<String> testList = Arrays.asList("LGA");

        assertEquals(lgaNewYorkNY.getIATA(), airportInfoExtractor.getAirportInfoList(testList).get(0).getIATA());
    }

    @Test
    public void airportInfoServiceReturnsHyphensWhenServerIsDown() throws IOException {
        airportInfoExtractor.setAirportInfoService(fAAAirportInfoServiceWithConnectionFunctionMocked);
        when(fAAAirportInfoServiceWithConnectionFunctionMocked.connectToFAAService("LGA")).thenThrow(ioException);
        assertEquals(hyphenAirport1.getIATA(), fAAAirportInfoServiceWithConnectionFunctionMocked.getAirportInfo("LGA").getIATA());
    }
}
