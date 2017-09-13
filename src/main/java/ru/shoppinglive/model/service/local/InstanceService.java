package ru.shoppinglive.model.service.local;


import com.sun.tools.attach.VirtualMachine;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Instance> getByProject(String code){
        Pattern pattern = Pattern.compile(code+"-([0-9.-]+)\\.jar$");
        Project project = projectRepository.findFirstByCode(code);
        return VirtualMachine.list().stream()
        .map(vmd -> {
            Matcher m = pattern.matcher(vmd.displayName());
            if(m.find()) {
                String version = m.group(1);
                int pid = Integer.parseInt(vmd.id());
                if(project.getType().equals(Project.Type.service)) {
                    int port = osService.getPortByPid(pid);
                    return new Instance(version, pid, port, port == 0 ? null : actuatorService.getHealth("127.0.0.1", port));
                }else{
                    return new Instance(version, pid, 0, null);
                }
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
