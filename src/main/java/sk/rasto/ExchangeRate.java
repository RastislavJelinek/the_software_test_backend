package sk.rasto;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ExchangeRate {
    private String getRatesFromBank() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        if (response.statusCode() != 200) {
            return null;
        }
        return response.body();
    }

    private String xmlToJson(List<String> currencies, String xml){
        if(xml == null) return null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            Element cubeElement = (Element) document.getElementsByTagName("Cube").item(0);
            JSONObject resultJson = new JSONObject();
            JSONObject rateJson = new JSONObject();
            JSONArray ratesArray = new JSONArray();

            if (cubeElement != null) {
                NodeList currencyList = cubeElement.getElementsByTagName("Cube");
                resultJson.put("date",((Element) currencyList.item(0)).getAttribute("time"));

                for (int i = 1; i < currencyList.getLength(); i++) {
                    Element currencyElement = (Element) currencyList.item(i);
                    String currencyCode = currencyElement.getAttribute("currency");
                    String rate = currencyElement.getAttribute("rate");

                    if(currencies != null && !currencies.contains(currencyCode))continue;

                    rateJson.put(currencyCode,rate);
                    JSONObject rateObject = new JSONObject();
                    rateObject.put("name", currencyCode);
                    rateObject.put("rate", rate);
                    ratesArray.put(rateObject);
                }
            }
            resultJson.put("rate",rateJson);
            resultJson.put("rates",ratesArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRates(List<String> currencies){
        String ratesFromBank = getRatesFromBank();
        return xmlToJson(currencies,ratesFromBank);
    }
}
