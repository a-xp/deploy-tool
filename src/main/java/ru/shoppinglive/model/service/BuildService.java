package ru.shoppinglive.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.shoppinglive.model.entity.filesystem.ScriptMeta;
import ru.shoppinglive.model.entity.gitlab.Commit;
import ru.shoppinglive.model.entity.gitlab.Job;
import ru.shoppinglive.model.entity.gitlab.Pipeline;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.jpa.Task;
import ru.shoppinglive.model.entity.project.Build;
import ru.shoppinglive.model.repository.ProjectRepository;
import ru.shoppinglive.model.service.local.OsService;
import ru.shoppinglive.model.service.local.ScriptService;
import ru.shoppinglive.model.service.remote.GitlabService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rkhabibullin on 13.09.2017.
 */
@Component
public class BuildService {

    @Autowired
    private GitlabService gitlabService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private OsService osService;
    @Autowired
    Environment settings;


    @Cacheable("builds")
    public List<Build> getMasterBuilds(int id){
        Project project = projectRepository.findOne(id);
        ScriptMeta scriptMeta = scriptService.getScript(project.getCode(), project.getType());
        Set<String> logged = osService.getLoggedVersions(project.getCode());
        Set<String> loaded = osService.getDownloadedVersions(project.getCode());
        return gitlabService.getPipelines(project.getGitlabId(), "master", 5).stream().map(p->{
           Job job = gitlabService.getPipelineJobs(project.getGitlabId(), p.getId()).stream().filter(j->j.getName().equals("build")).findFirst().get();
            String version = gitlabService.getVersion(project.getGitlabId(), job.getId());
            Build build =  new Build(version, p.getId(), "master", job.getFinishedAt(), job.getUser().getName(), job.getCommit().getTitle(), tasksFromComment(job.getCommit().getTitle()), null);
            if(scriptMeta!=null && scriptMeta.getDefaultVersion().equals(version))build.addFlag(Build.Flag.AUTOSTART);
            if(logged.contains(version))build.addFlag(Build.Flag.HAS_LOG);
            if(loaded.contains(version))build.addFlag(Build.Flag.HAS_JAR);
            return build;
        }).collect(Collectors.toList());
    }

    @Cacheable("qa-builds")
    public List<Build> getQABuilds(int id){
        Project project = projectRepository.findOne(id);
        ScriptMeta scriptMeta = scriptService.getScript(project.getCode(), project.getType());
        Set<String> logged = osService.getLoggedVersions(project.getCode());
        Set<String> loaded = osService.getDownloadedVersions(project.getCode());
        String profile = settings.getActiveProfiles()[0];
        if(profile.equals("local"))profile = "dev";

        return gitlabService.listEnvBranches(project.getGitlabId(), profile).stream().map(env->{
            Pipeline pipeline = gitlabService.getPipelines(project.getGitlabId(), env, 1).get(0);
            Job job = gitlabService.getPipelineJobs(project.getGitlabId(), pipeline.getId()).stream().filter(j->j.getName().equals("build_env")).findFirst().orElse(null);
            String version = gitlabService.getVersion(project.getGitlabId(), job.getId());
            Build build =  new Build(version, pipeline.getId(), env, job.getFinishedAt(), job.getUser().getName(), "", null, null);
            List<Commit> newCommits = gitlabService.findNewCommits(project.getGitlabId(), env);
            if(newCommits!=null){
                build.setFeatures(newCommits.stream().map(Commit::getTitle).flatMap(t->tasksFromComment(t).stream()).distinct().collect(Collectors.toList()));
            }else{
                build.addFlag(Build.Flag.INVALID);
            }
            if(scriptMeta!=null && scriptMeta.getDefaultVersion().equals(version))build.addFlag(Build.Flag.AUTOSTART);
            if(logged.contains(version))build.addFlag(Build.Flag.HAS_LOG);
            if(loaded.contains(version))build.addFlag(Build.Flag.HAS_JAR);
            return build;
        }).collect(Collectors.toList());
    }

    private List<Task> tasksFromComment(String comment){
        Pattern pattern = Pattern.compile("\\b(feature_|f|#)?(\\d{4,5})\\b");
        List<Task> result = new ArrayList<>(1);
        Matcher matcher = pattern.matcher(comment);
        while (matcher.find()){
            Task task = taskService.find(Integer.parseInt(matcher.group(2)));
            if(task!=null)result.add(task);
        }
        return result;
    }

}
