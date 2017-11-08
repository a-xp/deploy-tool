package ru.shoppinglive.model.service.local;


import com.sun.tools.attach.VirtualMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.stereotype.Service;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.project.Instance;
import ru.shoppinglive.model.repository.ProjectRepository;
import ru.shoppinglive.model.service.remote.ActuatorService;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rkhabibullin on 24.03.2017.
 */
@Service
public class InstanceService {
    @Autowired
    private OsService osService;
    @Autowired
    private ActuatorService actuatorService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private InetUtils inetUtils;

    public List<Instance> getByProject(int id){
        Project project = projectRepository.getOne(id);
        Pattern pattern = Pattern.compile(project.getCode()+"-([0-9.a-z-]+)\\.jar$", Pattern.CASE_INSENSITIVE);
        String server = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        return VirtualMachine.list().stream()
        .map(vmd -> {
            Matcher m = pattern.matcher(vmd.displayName());
            if(m.find()) {
                String version = m.group(1);
                int pid = Integer.parseInt(vmd.id());
                int port = osService.getPortByPid(pid);
                int uptime = osService.getUptime(pid);
                return new Instance(version, server, pid, port, uptime);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Boolean refreshInstance(int pid){
        int port = osService.getPortByPid(pid);
        if(port!=0){
            actuatorService.refresh("127.0.0.1", port);
            return true;
        }else{
            return false;
        }
    }

    public Boolean killInstance(int pid){
        return VirtualMachine.list().stream().anyMatch(vmd->vmd.id().equals(String.valueOf(pid)))
                && osService.killProcess(pid);
    }

    public Boolean shutdownInstance(int pid){
        int port = osService.getPortByPid(pid);
        if(port!=0){
            actuatorService.stop("127.0.0.1", port);
            return true;
        }
        return false;
    }

}
