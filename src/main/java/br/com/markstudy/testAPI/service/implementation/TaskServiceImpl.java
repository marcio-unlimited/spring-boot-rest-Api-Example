package br.com.markstudy.testAPI.service.implementation;

import br.com.markstudy.testAPI.domain.dto.TaskDTO;
import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.domain.entity.Task;
import br.com.markstudy.testAPI.domain.repository.JobRepository;
import br.com.markstudy.testAPI.domain.repository.TaskRepository;
import br.com.markstudy.testAPI.service.exceptions.InvalidDataPersistException;
import br.com.markstudy.testAPI.service.interfaces.TaskService;
import org.hsqldb.HsqlException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository repository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TaskDTO> findAllSortByCreatedAt(Date date) {
        List<Task> entities = repository.findAllSortByCreatedAt(date);
        return entities.stream().map(entity -> modelMapper.map(entity, TaskDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findAll() {
        List<Task> entities = repository.findAll();
        return entities.stream().map(entity -> modelMapper.map(entity, TaskDTO.class)).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = repository.findByIdent(id);
        if(task == null)
            throw new EntityNotFoundException();
        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public Task findByIdent(Long id) {
        return repository.findByIdent(id);
    }

    @Override
    public void save(Task task) {
        try{
            repository.save(task);
        } catch(DataIntegrityViolationException | HsqlException e){
            throw new InvalidDataPersistException();
        }
    }

    @Override
    public void update(Task task, Long id) {
        Task taskEntity = repository.findByIdent(id);
        task.setCod(taskEntity.getCod());
        this.save(task);
    }

    @Override
    public void deleteById(Long id) {
        Task taskEntity = repository.findByIdent(id);
        if(taskEntity == null)
            throw new EntityNotFoundException();
        List<Job> jobs = jobRepository.findByTask(taskEntity);
        jobs.stream().forEach(job -> {
            Iterator<Task> it = job.getTasks().iterator();
            while(it.hasNext()){
                if(it.next().getId() == taskEntity.getId()){
                    it.remove();
                }
            }
            jobRepository.save(job);
        });
        repository.delete(taskEntity);
    }

    @Override
    public void deleteAll() {
        List<Task> taskRemove = repository.findAll();
        for(Task task : taskRemove){
            deleteById(task.getId());
        }
    }
}
