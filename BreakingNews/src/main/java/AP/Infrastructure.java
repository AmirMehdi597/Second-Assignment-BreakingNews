package AP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

public class Infrastructure {

    private final String URL;
    private String APIKEY;
    private final String JSONRESULT;
    private ArrayList<News> newsList;

    public Infrastructure(String APIKEY) {
        this.APIKEY = APIKEY;
        this.URL = "https://newsapi.org/v2/everything?q=tesla&from=" + LocalDate.now().minusDays(1) + "&sortBy=publishedAt&apiKey=";
        this.JSONRESULT = getInformation();
        this.newsList = new ArrayList<>();
        if (this.JSONRESULT != null) {
            parseInformation();
        }
    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    private String getInformation() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + APIKEY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("HTTP error code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("!!Exception : " + e.getMessage());
        }
        return null;
    }

    private void parseInformation() {
        try {
            JSONObject jsonObject = new JSONObject(JSONRESULT);
            JSONArray articles = jsonObject.getJSONArray("articles");

            for (int i = 0; i < 20 && i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                String title = article.getString("title");
                String description = article.getString("description");
                String sourceName = article.getJSONObject("source").getString("name");
                String author = article.optString("author", "Unknown");
                String url = article.getString("url");
                String publishedAt = article.getString("publishedAt");

                News news = new News(title, description, sourceName, author, url, publishedAt);
                newsList.add(news);
            }
        } catch (Exception e) {
            System.out.println("Error parsing the JSON result: " + e.getMessage());
        }
    }

    public void displayNewsList() {
        if (newsList.isEmpty()) {
            System.out.println("No news available.");
            return;
        }

        System.out.println("Select a news article to view details:");
        for (int i = 0; i < newsList.size(); i++) {
            System.out.println((i + 1) + ". " + newsList.get(i).getTitle());
        }
    }

    public void displayFullNews(int index) {
        if (index < 0 || index >= newsList.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        News selectedNews = newsList.get(index);
        selectedNews.displayNews();
    }
}


