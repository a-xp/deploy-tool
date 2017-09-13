package ru.shoppinglive.model.service.remote;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.shoppinglive.model.entity.gitlab.Branch;
import ru.shoppinglive.model.entity.gitlab.Pipeline;
import ru.shoppinglive.model.entity.gitlab.Project;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rkhabibullin on 12.09.2017.
 */
@Component
@ConfigurationProperties(prefix = "deploy.gitlab")
public class GitlabService {
    @Setter
    private String token;
    @Setter
    private String url;
    @Setter
    private String group;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable("gl-env-branches")
    public Set<String> listEnvBranches (int id, String env){
        return restTemplate.exchange(url + "/projects/" + id + "/repository/branches", HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Branch>>() {
                }).getBody().stream().map(Branch::getName).filter(n->n.startsWith(env)).collect(Collectors.toSet());
    }

    @Cacheable("gl-projects")
    public Project getProject(String code){
        try {
            URI reqUrl = new URI(url + "/projects/" + group + "%2F" + code);
            ResponseEntity<Project> responseEntity = restTemplate.exchange(reqUrl, HttpMethod.GET, new HttpEntity<String>(headers), Project.class);
            return responseEntity.getBody();
        }catch (URISyntaxException e){
            return null;
        }
    }

    @Cacheable("gl-pipelines")
    public List<Integer> getPipelines(int id, String ref, int cnt){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url+"/projects/"+id+"/pipelines").queryParam("ref", ref).queryParam("per_page", cnt).queryParam("status","success");
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<Void>(headers), new ParameterizedTypeReference<List<Pipeline>>() {}).getBody()
            .stream().map(Pipeline::getId).collect(Collectors.toList());
    }

    @Cacheable("gl-versions")
    public String getVersion(int projectId, int buildId){
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/projects/" + projectId + "/builds/"+buildId+"/trace", HttpMethod.GET,
                new HttpEntity<String>(headers), String.class);
        Pattern p = Pattern.compile("Detected version: '([0-9\\.\\-]+)'");
        String body = responseEntity.getBody();
        if(body==null)return null;
        Matcher m = p.matcher(body);
        return m.find()?m.group(1):null;
    }

    @PostConstruct
    public void init(){
        headers.put("PRIVATE-TOKEN", Collections.singletonList(token));
    }

}
