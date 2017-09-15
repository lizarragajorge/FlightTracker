package airportfetch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class AirportInfoExtractorTest
{
    AirportInfoExtractor airportInfoExtractor;
    AirportInfoService serviceMockForOneCode;
    AirportInfo hyphenAirport2;
    AirportInfo hyphenAirport1;
    AirportInfo errorNetworkAirport;
    AirportInfo iahHoustonTX;
    AirportInfo houHoustonTX;
    AirportInfo dfwDallasTX;
    AirportInfo jfkNewYorkNY;
    AirportInfo lgaNewYorkNY;
    AirportInfo laxLosAngelesCA;
    AirportInfo sgfSpringFieldMI;
    AirportInfo spiSpringFieldIL;
    AirportInfo cwiClintonAR;
    AirportInfo ctzClintonNC;
    AirportInfo emptyStringAirport1;
    AirportInfo emptyStringAirport2;

    @Before
    public void setUp()
    {
        airportInfoExtractor = new AirportInfoExtractor();
        serviceMockForOneCode = Mockito.mock(AirportInfoService.class);
        airportInfoExtractor.setAirportInfoService(serviceMockForOneCode);

        dfwDallasTX = new AirportInfo("Dallas-Ft Worth", "Texas", "DFW", "Dallas/Ft Worth International", "64.0 F (18.8 C)", false);
        iahHoustonTX = new AirportInfo("Houston", "Texas", "IAH", "George Bush Intercontinental Airport", "73.0 F (22.8 C)", true);
        houHoustonTX = new AirportInfo("Houston", "Texas", "HOU", "William P. Hobby Airport", "73.0 F (22.8 C)", false);
        jfkNewYorkNY = new AirportInfo("New York", "New York", "JFK", "John F. Kennedy International Airport", "73.0 F (22.8 C)", true);
        lgaNewYorkNY = new AirportInfo("New York", "New York", "LGA", "LaGuardia Airport", "73.0 F (22.8 C)", false);
        laxLosAngelesCA = new AirportInfo("Los Angeles", "California", "LAX", "Los Angeles International Airport", "73.0 F (22.8 C)", true);
        sgfSpringFieldMI = new AirportInfo("SpringField", "Missouri", "SGF", "Springfield-Branson National Airport", "73.0 F (22.8 C)", false);
        spiSpringFieldIL = new AirportInfo("SpringField", "Illinois", "SPI", "Abraham Lincoln Capital Airport", "73.0 F (22.8 C)", true);
        cwiClintonAR = new AirportInfo("Clinton", "Arkansas", "CWI", "Clinton Municipal Airport", "73.0 F (22.8 C)", false);
        ctzClintonNC = new AirportInfo("Clinton", "North Carolina", "CTZ", "Clinton–Sampson County Airport", "73.0 F (22.8 C)", true);
        hyphenAirport1 = new AirportInfo("---", "---", "---", "---", "---", false);
        hyphenAirport2 = new AirportInfo("---", "---", "---", "---", "---", true);
        errorNetworkAirport = new AirportInfo("---", "---", "---", "---", "---", false);
        emptyStringAirport1 = new AirportInfo("", "", "---", "---", "---", true);
        emptyStringAirport2 = new AirportInfo("", "", "---", "---", "---", false);
    }

    @Test
    public void canary (){
        assertTrue(true);
    }

    @Test
    public void sortingPresortedAirportDataListReturnsSortedList(){
        List<AirportInfo> testList = Arrays.asList(dfwDallasTX, iahHoustonTX);

        assertEquals(testList, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void airportDataIsSortedByCityName(){
        List<AirportInfo> testList = Arrays.asList(iahHoustonTX, dfwDallasTX);
        List<AirportInfo> sortedTestList = Arrays.asList(dfwDallasTX, iahHoustonTX);

        assertEquals(sortedTestList, airportInfoExtractor.sortAirportData(testList));
    }


    @Test
    public void airportDataIsSortedByStateWhenCitiesHaveSameName(){
        List<AirportInfo> testList = Arrays.asList(ctzClintonNC, cwiClintonAR, sgfSpringFieldMI, spiSpringFieldIL);
        List<AirportInfo> sortedTestList = Arrays.asList(cwiClintonAR, ctzClintonNC, spiSpringFieldIL, sgfSpringFieldMI);

        assertEquals(sortedTestList, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void airportDataIsSortedByStateNameForSeveralCitiesWithSameName(){
        List<AirportInfo> testList = Arrays.asList(ctzClintonNC, jfkNewYorkNY, cwiClintonAR, sgfSpringFieldMI, spiSpringFieldIL, iahHoustonTX, houHoustonTX, laxLosAngelesCA);
        List<AirportInfo> testListSorted = Arrays.asList(cwiClintonAR, ctzClintonNC, houHoustonTX, iahHoustonTX, laxLosAngelesCA, jfkNewYorkNY, spiSpringFieldIL, sgfSpringFieldMI);

        assertEquals(testListSorted, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void sortAirportDataWithEmptyStringsInCityAndState(){
        List<AirportInfo> testList = Arrays.asList(jfkNewYorkNY, dfwDallasTX, emptyStringAirport2, emptyStringAirport1);
        List<AirportInfo> sortedTestList = Arrays.asList(emptyStringAirport2, emptyStringAirport1, dfwDallasTX, jfkNewYorkNY);

        assertEquals(sortedTestList, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void sortAirportDataWithThreeHyphensForCity() {
        List<AirportInfo> testList = Arrays.asList(jfkNewYorkNY, dfwDallasTX, hyphenAirport1, hyphenAirport2);
        List<AirportInfo> sortedTestList = Arrays.asList(hyphenAirport1, hyphenAirport2, dfwDallasTX, jfkNewYorkNY);

        assertEquals(sortedTestList, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void sortAirportDataByCityThenStateThenIATAForAirportsWithSameCityAndState()
    {
        List<AirportInfo> testList = Arrays.asList(lgaNewYorkNY, jfkNewYorkNY, iahHoustonTX, houHoustonTX);
        List<AirportInfo> sortedTestList = Arrays.asList(houHoustonTX, iahHoustonTX, jfkNewYorkNY, lgaNewYorkNY);

        assertEquals(sortedTestList, airportInfoExtractor.sortAirportData(testList));
    }

    @Test
    public void getAirportInfoListWhenNoAirportCodesSuppliedReturnsEmptyAirportInfoList()
    {
        AirportInfoService serviceMockForEmptyListOfAirportCodes = Mockito.mock(AirportInfoService.class);
        airportInfoExtractor.setAirportInfoService(serviceMockForEmptyListOfAirportCodes);

        assertEquals(emptyList(), airportInfoExtractor.getAirportInfoList(emptyList()));
    }

    @Test
    public void getAirportInfoListWhenOneAirportCodeIsSupplied(){
        when(serviceMockForOneCode.getAirportInfo("DFW")).thenReturn(dfwDallasTX);
        List<String> airportCode = Arrays.asList("DFW");
        List<AirportInfo> airportInfoListExpected = Arrays.asList(dfwDallasTX);
                                                                 
        assertEquals(airportInfoListExpected, airportInfoExtractor.getAirportInfoList(airportCode));
    }

    @Test
    public void getAirportInfoListWhenTwoAirportCodesAreSupplied(){
        when(serviceMockForOneCode.getAirportInfo("DFW")).thenReturn(dfwDallasTX);
        when(serviceMockForOneCode.getAirportInfo("IAH")).thenReturn(iahHoustonTX);
        List<String> airportCodes = Arrays.asList("DFW", "IAH");
        List<AirportInfo> airportInfoListExpected = Arrays.asList(dfwDallasTX, iahHoustonTX);

        assertEquals(airportInfoListExpected, airportInfoExtractor.getAirportInfoList(airportCodes));
    }

    @Test
    public void getAirportInfoListWhenInvalidAirportCodeIsSupplied(){
        List<String> airportCodes = Arrays.asList("@@@");
        AirportInfoService serviceMockForInvalidCode = Mockito.mock(AirportInfoService.class);
        when(serviceMockForInvalidCode.getAirportInfo("@@@")).thenReturn(emptyStringAirport1);
        airportInfoExtractor.setAirportInfoService(serviceMockForInvalidCode);

        assertEquals(Arrays.asList(emptyStringAirport1), airportInfoExtractor.getAirportInfoList(airportCodes));
    }

    @Test
    public void getAirportInfoListWhenThereIsANetworkError(){
        List<String> airportCodes = Arrays.asList("@@@");
        AirportInfoService serviceMockForNetworkError = Mockito.mock(AirportInfoService.class);
        when(serviceMockForNetworkError.getAirportInfo("@@@")).thenReturn(errorNetworkAirport);
        airportInfoExtractor.setAirportInfoService(serviceMockForNetworkError);

        assertEquals(Arrays.asList(errorNetworkAirport), airportInfoExtractor.getAirportInfoList(airportCodes));
    }

    @Test
    public void numberOfDelaysReturnsNumberOfDelays() {
        int expectedNumberOfDelays = 2;
        List<AirportInfo> airportInfoList = Arrays.asList(dfwDallasTX, iahHoustonTX, houHoustonTX, jfkNewYorkNY, lgaNewYorkNY);
        assertEquals(expectedNumberOfDelays, airportInfoExtractor.numberOfDelays(airportInfoList));
    }

    @Test
    public void temperatureIsRetrievedByAirportInfoExtractor()
    {
        when(serviceMockForOneCode.getAirportInfo("DFW")).thenReturn(dfwDallasTX);
        assertEquals(dfwDallasTX.getTemperature(), airportInfoExtractor.getAirportInfoList(Arrays.asList("DFW")).get(0).getTemperature());
    }

    @Test
    public void airportNameIsRetrievedByAirportInfoExtractor()
    {
        when(serviceMockForOneCode.getAirportInfo("DFW")).thenReturn(dfwDallasTX);
        assertEquals(dfwDallasTX.getAirportName(), airportInfoExtractor.getAirportInfoList(Arrays.asList("DFW")).get(0).getAirportName());
    }
}