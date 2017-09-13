package ru.shoppinglive.model.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shoppinglive.model.entity.actuator.Metrics;

import java.net.URI;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@Service
public class ActuatorService {
    @Autowired
    private RestTemplate restTemplate;

    public void stop(String ip, int port){
        try {
            URI uri = new URI("http://"+ip+":" + port + "/shutdown");
            restTemplate.exchange(uri, HttpMethod.POST, null, Object.class);
        }catch (Exception e){ }
    }

    public void refresh(String ip, int port){
        try {
            URI uri = new URI("http://"+ip+":" + port + "/refresh");
            restTemplate.exchange(uri, HttpMethod.POST, null, Object.class);
        }catch (Exception e){ }
    }

    public Metrics getHealth(String ip, int port){
        try{
            URI uri = new URI("http://"+ip+":" + port + "/metrics");
            ResponseEntity<Metrics> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, Metrics.class);
            return responseEntity.getBody();
        } catch (Exception e){}
        return null;
    }
}
