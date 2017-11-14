package ru.shoppinglive.model.service.remote;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rkhabibullin on 13.09.2017.
 */
@Component
@ConfigurationProperties(prefix = "deploy.redmine")
public class RedmineService {
    @Setter
    private String url;
    @Setter
    private String login;
    @Setter
    private String token;

    @Autowired
    private RestTemplate restTemplate;

    public String getFeatureName(int id){
        String defaultName = "Задача "+id;

        ResponseEntity<String> page = restTemplate.getForEntity(url+"/login", String.class);
        Pattern patternToken = Pattern.compile("name=\"authenticity_token\" type=\"hidden\" value=\"(.+?)\"");
        Matcher matcher = patternToken.matcher(page.getBody());
        if(!matcher.find()){
            return defaultName;
        }
        String csrfToken = matcher.group(1);

        Pattern patternCookie = Pattern.compile("(_redmine_session=.+?;)");
        matcher = patternCookie.matcher(page.getHeaders().get("Set-Cookie").get(0));
        if(!matcher.find()){
            return defaultName;
        }
        String cookie = matcher.group(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", cookie);
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>(2);
        body.add("authenticity_token", csrfToken);
        body.add("username", login);
        body.add("password", token);
        page = restTemplate.postForEntity(url+"/login", new HttpEntity<>(body, headers), String.class);

        if(page.getStatusCode().is3xxRedirection()){
            matcher = patternCookie.matcher(page.getHeaders().get("Set-Cookie").get(0));
            if(!matcher.find()){
                return defaultName;
            }
            cookie = matcher.group(1);
        }else{
            return defaultName;
        }

        headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        page = restTemplate.exchange(url+"/issues/"+id, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if(page.getStatusCode().is2xxSuccessful()){
            Pattern patternTitle = Pattern.compile("<div class=\"subject\">\\s*<div>\\s*<h3>(.+?)<\\/h3>");
            matcher = patternTitle.matcher(page.getBody());
            if(matcher.find()){
                return HtmlUtils.htmlUnescape(matcher.group(1));
            }
        }
        return defaultName;
    }

}
