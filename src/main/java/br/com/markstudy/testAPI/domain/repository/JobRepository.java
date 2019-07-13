package br.com.markstudy.testAPI.domain.repository;

import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.domain.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends CrudRepository<Job, Long> {

    @Query("select DISTINCT j FROM Job j left join fetch j.tasks t order by t.weight")
    List<Job> findAllSortByTaskWeight();

    @Query("FROM Job j left join fetch j.tasks t ")
    List<Job> findAll();

    @Query("FROM Job j left join fetch j.parentJob WHERE j.name = :name")
    Job findByName(@Param("name") String name);

    @Query("SELECT j FROM Job j left join fetch j.tasks WHERE j.id = :id")
    Job findByIdJob(@Param("id") Long id);

    @Query("SELECT j FROM Job j left join fetch j.tasks WHERE j.parentJob.id = :id")
    Job findByParentId(@Param("id") Long id);

    @Query("SELECT DISTINCT j FROM Job j left join fetch j.tasks WHERE :task MEMBER OF j.tasks")
    List<Job> findByTask(Task task);
}
