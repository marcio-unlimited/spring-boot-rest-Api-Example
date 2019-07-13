package br.com.markstudy.testAPI.service.implementation;

import br.com.markstudy.testAPI.domain.dto.JobDTO;
import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.domain.entity.Task;
import br.com.markstudy.testAPI.domain.repository.JobRepository;
import br.com.markstudy.testAPI.service.exceptions.InvalidDataPersistException;
import br.com.markstudy.testAPI.service.exceptions.EntityNotFoundException;
import br.com.markstudy.testAPI.service.interfaces.JobService;
import br.com.markstudy.testAPI.service.interfaces.TaskService;
import org.hsqldb.HsqlException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<JobDTO> findAllSortByTaskWeight() {
        List<Job> entities = jobRepository.findAllSortByTaskWeight();
        return entities.stream().map(entity -> modelMapper.map(entity, JobDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> findAll() {
        List<Job> entities = jobRepository.findAll();
        return entities.stream().map(entity -> modelMapper.map(entity, JobDTO.class)).collect(Collectors.toList());
    }

    @Override
    public JobDTO findById(Long id) {
        Job job = jobRepository.findByIdJob(id);
        if(job == null)
            throw new EntityNotFoundException();
        return modelMapper.map(job, JobDTO.class);
    }

    @Override
    public Job findByIdent(Long id) {
        return jobRepository.findByIdJob(id);
    }

    @Override
    @Transactional
    public void save(Job job) {
        if(alreadyInTheHierarchy(job))
            throw new InvalidDataPersistException();
        try{
            if(job.getParentJob() != null){
                Job parent = jobRepository.findByIdJob(job.getParentJob().getId());
                job.setParentJob(parent != null ? parent : job.getParentJob());
            }
            jobRepository.save(job);
        } catch(DataIntegrityViolationException | HsqlException e){
            throw new InvalidDataPersistException();
        }
    }

    @Override
    public void update(Job job, Long id) {
        Job jobEntity = jobRepository.findByIdJob(id);
        job.setCod(jobEntity.getCod());
        for(Task task : job.getTasks()){
            if (task.getCod() == null){
                Task taskEntity = taskService.findByIdent(task.getId());
                task.setCod(taskEntity.getCod());
            }
        }
        try{
            this.save(job);
        } catch(DataIntegrityViolationException | HsqlException e){
            throw new InvalidDataPersistException();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Job jobEntity = jobRepository.findByIdJob(id);
        if(jobEntity == null)
            throw new EntityNotFoundException();

        Job parent = jobRepository.findByParentId(jobEntity.getId());
        if(parent != null){
            parent.setParentJob(jobEntity.getParentJob());
            jobRepository.save(parent);
        } else {
            jobEntity.setParentJob(null);
        }
        jobRepository.delete(jobEntity);
    }

    @Override
    public void deleteAll() {
        jobRepository.deleteAll();;
    }

    private boolean alreadyInTheHierarchy(Job job){
        Job parent = job.getParentJob();
        while(parent != null && !parent.getName().equals(job.getName())){
            parent = jobRepository.findByName(parent.getName());
            if(parent != null && (parent.getParentJob() == null || parent.getParentJob().getName().equals(job.getName()))){
                parent = parent.getParentJob();
                break;
            } else {
                parent = parent == null ? parent : parent.getParentJob();
            }
        }
        return parent == null ? false : parent.getName().equals(job.getName());
    }
}
