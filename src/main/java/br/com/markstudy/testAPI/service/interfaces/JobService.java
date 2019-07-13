package br.com.markstudy.testAPI.service.interfaces;


import br.com.markstudy.testAPI.domain.dto.JobDTO;
import br.com.markstudy.testAPI.domain.entity.Job;

import java.util.List;

public interface JobService {

    List<JobDTO> findAllSortByTaskWeight();

    List<JobDTO> findAll();

    JobDTO findById(Long id);

    Job findByIdent(Long id);

    void save(Job job);

    void update(Job job, Long id);

    void deleteById(Long id);

    void deleteAll();

}
