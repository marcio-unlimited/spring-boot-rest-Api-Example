package br.com.markstudy.testAPI.domain.repository;

import br.com.markstudy.testAPI.domain.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("select DISTINCT t FROM Task t where t.createdAt > :created")
    List<Task> findAllSortByCreatedAt(Date created);

    @Query("FROM Task t ")
    List<Task> findAll();

    @Query("SELECT t FROM Task t WHERE t.id = :id")
    Task findByIdent(@Param("id") Long id);


}
