package br.com.markstudy.testAPI.controller;

import br.com.markstudy.testAPI.domain.dto.JobDTO;
import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.service.interfaces.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping(value="/jobs/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public JobDTO getJobs(@PathVariable Long id){
        return jobService.findById(id);
    }

    @RequestMapping(value="/jobs/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public void deleteJob(@PathVariable Long id){
        jobService.deleteById(id);
    }

    @RequestMapping(value="/jobs/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public void updateJob(@PathVariable Long id, @RequestBody Job job){
        jobService.update(job, id);
    }

    @RequestMapping(value="/jobs", method = RequestMethod.POST, headers = "Accept=application/json")
    public void save(@RequestBody Job entity){
        jobService.save(entity);
    }

    @RequestMapping(value="/jobs", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<JobDTO> getJobsList(@RequestParam("sortByWeight") boolean sortByWeight){
        return sortByWeight ? jobService.findAllSortByTaskWeight() : jobService.findAll();
    }

}
