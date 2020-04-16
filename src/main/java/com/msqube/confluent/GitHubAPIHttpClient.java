package com.msqube.confluent;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
// GitHubHttpAPIClient used to launch HTTP Get requests
public class GitHubAPIHttpClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubAPIHttpClient.class);

    // for efficient http requests
    private Integer XRateLimit = 9999;
    private Integer XRateRemaining = 9999;
    private long XRateReset = Instant.MAX.getEpochSecond();

    protected JsonNode getNextItems( String url) throws InterruptedException {

        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = getNextItemsAPI(url);

            // deal with headers in any case
            Headers headers = jsonResponse.getHeaders();
//            XRateLimit = Integer.valueOf(headers.getFirst("X-RateLimit-Limit"));
//            XRateRemaining = Integer.valueOf(headers.getFirst("X-RateLimit-Remaining"));
//            XRateReset = Integer.valueOf(headers.getFirst("X-RateLimit-Reset"));
            switch (jsonResponse.getStatus()){
                case 200:
                    return jsonResponse.getBody();
                case 401:
                    throw new InterruptedException("Bad GitHub credentials provided, please edit your config");
                case 403:
                    // we have issues too many requests.
                    log.info(jsonResponse.getBody().getObject().getString("message"));
                    log.info(String.format("Your rate limit is %s", XRateLimit));
                    log.info(String.format("Your remaining calls is %s", XRateRemaining));
                    log.info(String.format("The limit will reset at %s",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(XRateReset), ZoneOffset.systemDefault())));
                    long sleepTime = XRateReset - Instant.now().getEpochSecond();
                    log.info(String.format("Sleeping for %s seconds", sleepTime ));
                    Thread.sleep(1000 * sleepTime);
                    return getNextItems( url);
                default:
                    log.error(String.valueOf(jsonResponse.getStatus()));
                    log.error(jsonResponse.getBody().toString());
                    log.error(jsonResponse.getHeaders().toString());
                    log.error("Unknown error: Sleeping 5 seconds " +
                            "before re-trying");
                    Thread.sleep(5000L);
                    return getNextItems(url);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            Thread.sleep(5000L);
            return new JsonNode(null);
        }
    }

    protected HttpResponse<JsonNode> getNextItemsAPI( String url) throws UnirestException {
        GetRequest unirest = Unirest.get(url);
        log.debug(String.format("GET %s", unirest.getUrl()));
        return unirest.asJson();
    }

    /*public void sleep() throws InterruptedException {
        long sleepTime = (long) Math.ceil(
                (double) (XRateReset - Instant.now().getEpochSecond()) / XRateRemaining);
        log.debug(String.format("Sleeping for %s seconds", sleepTime ));
        Thread.sleep(1000 * sleepTime);
    }

    public void sleepIfNeed() throws InterruptedException {
        // Sleep if needed
        if (XRateRemaining <= 10 && XRateRemaining > 0) {
            log.info(String.format("Approaching limit soon, you have %s requests left", XRateRemaining));
            sleep();
        }
    }*/
}