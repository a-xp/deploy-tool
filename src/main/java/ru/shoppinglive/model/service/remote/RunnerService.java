package ru.shoppinglive.model.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.stereotype.Component;
import ru.shoppinglive.model.entity.project.Runner;

import java.util.Collections;
import java.util.List;

/**
 * Created by rkhabibullin on 15.09.2017.
 */
@Component
public class RunnerService {

    @Autowired
    private InetUtils inetUtils;

    public List<Runner> getRunners(){
        Runner local = new Runner(inetUtils.findFirstNonLoopbackHostInfo().getIpAddress(), inetUtils.findFirstNonLoopbackHostInfo().getHostname());
        return Collections.singletonList(local);
    }

}
