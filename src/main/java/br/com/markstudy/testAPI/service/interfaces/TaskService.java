package br.com.markstudy.testAPI.service.interfaces;

import br.com.markstudy.testAPI.domain.dto.TaskDTO;
import br.com.markstudy.testAPI.domain.entity.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    List<TaskDTO> findAllSortByCreatedAt(Date date);

    List<TaskDTO> findAll();

    TaskDTO findById(Long id);

    Task findByIdent(Long id);

    void save(Task task);

    void update(Task task, Long id);

    void deleteById(Long id);

    void deleteAll();
}
