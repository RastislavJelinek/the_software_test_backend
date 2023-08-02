package sk.rasto;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeRateTest {

    private ExchangeRate exchangeRate;

    @BeforeEach
    void setUp() {
        exchangeRate = new ExchangeRate();
    }

    @Test
    void testGetRates() throws Exception {
        // Prepare mock response
        String xmlResponse = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <gesmes:subject>Reference rates</gesmes:subject>
                    <gesmes:Sender>
                        <gesmes:name>European Central Bank</gesmes:name>
                    </gesmes:Sender>
                    <Cube>
                        <Cube time="2023-08-01">
                            <Cube currency="USD" rate="1.1877"/>
                            <Cube currency="JPY" rate="131.88"/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>""";



        // Mock currencies list
        List<String> currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("JPY");

        Method xmlToJsonMethod = ExchangeRate.class.getDeclaredMethod("xmlToJson", List.class, String.class);
        xmlToJsonMethod.setAccessible(true); // Make the private method accessible
        String jsonResult = (String) xmlToJsonMethod.invoke(exchangeRate, currencies, xmlResponse);



        // Verify the JSON result
        JSONObject resultJson = new JSONObject(jsonResult);
        JSONObject rateJson = resultJson.getJSONObject("rate");
        JSONArray ratesArray = resultJson.getJSONArray("rates");

        assertEquals("2023-08-01", resultJson.getString("date"));
        assertEquals("1.1877", rateJson.getString("USD"));
        assertEquals("131.88", rateJson.getString("JPY"));
        assertEquals(2, ratesArray.length());
    }
}
