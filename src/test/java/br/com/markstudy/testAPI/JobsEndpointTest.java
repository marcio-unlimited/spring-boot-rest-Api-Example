package br.com.markstudy.testAPI;

import br.com.markstudy.testAPI.controller.JobController;
import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.domain.entity.Task;
import br.com.markstudy.testAPI.service.exceptions.EntityNotFoundException;
import br.com.markstudy.testAPI.service.interfaces.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class JobsEndpointTest {

    private MockMvc mockMvc;

    @Autowired
    private JobController controller;

    @Autowired
    private JobService service;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @After
    public void clean(){
        service.deleteAll();
    }



    @Test
    public void testGetListOrdered() throws Exception{

        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 4, false, new Date()), new Task(2L, "task 2", 5, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        Job parent = new Job( 1L, "job 1", true, null,
                Arrays.asList(new Task(3L, "task 3", 1, false, new Date()), new Task(4L, "task 4", 2, false, new Date())));
        jsonJob = createMockJobAsJson(2L, "job 2", true, parent,
                Arrays.asList(new Task(5L, "task 5", 4, false, new Date()), new Task(6L, "task 6", 5, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/jobs")
                .param("sortByWeight", "true").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }


    @Test
    public void testPostIamMyFather() throws Exception{
        Job parent = new Job( 1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task1", 1, false, new Date()), new Task(2L, "task2", 1, false, new Date())));
        String jsonJob = createMockJobAsJson(1L, "job 1", true, parent,
                Arrays.asList(new Task(1L, "task1", 1, false, new Date()), new Task(2L, "task2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.PRECONDITION_FAILED.value()));
    }

    @Test
    public void testSaveValidJobHierarchy()  throws Exception{

        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Job job = new Job(1L, "job 1", true, null,
                Arrays.asList(new Task(3L,"task 3", 1, false, new Date()), new Task(4L, "task 4", 1, false, new Date())));
        jsonJob = createMockJobAsJson(2L, "job 2", true, job,
                Arrays.asList(new Task(5L,"task 5", 1, false, new Date()), new Task(6L, "task 6", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Job job2 = new Job(2L, "job 2", true, job,
                Arrays.asList(new Task(7L,"task 7", 1, false, new Date()), new Task(8L, "task 8", 1, false, new Date())));

        jsonJob = createMockJobAsJson(3L, "job 3", true, job2,
                Arrays.asList(new Task(9L,"task 9", 1, false, new Date()), new Task(10L, "task 10", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testSaveInvalidJobHierarchy() throws Exception{

        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Job job = new Job(1L, "job 1", true, null,
                Arrays.asList(new Task(1L,"task1", 1, false, new Date()), new Task(2L, "task2", 1, false, new Date())));
        jsonJob = createMockJobAsJson(2L, "job 2", true, job,
                Arrays.asList(new Task(3L, "task 3", 1, false, new Date()), new Task(4L, "task 4", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Job job2 = new Job(2L, "job 2", true, job,
                Arrays.asList(new Task(3L,"task1", 1, false, new Date()), new Task(4L, "task2", 1, false, new Date())));

        jsonJob = createMockJobAsJson(3L, "job 3", true, job2,
                Arrays.asList(new Task(5L, "task 5", 1, false, new Date()), new Task(6L, "task 6", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testJobNotFound(){
        controller.getJobs(15L);
    }

    @Test
    public void testValidUpdateJob() throws Exception{
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        jsonJob = createMockJobAsJson(1L, "job 1-2", false, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/jobs/1")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }


    @Test
    public void testInvalidUpdateJob() throws Exception{
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        jsonJob = createMockJobAsJson(1L, null, false, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/jobs/1")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.PRECONDITION_FAILED.value()));
    }

    @Test
    public void testValidDeleteJob() throws Exception{
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/1")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testDeleteInnerHierarchyJob() throws Exception{
        List<Task> tasks = Arrays.asList(new Task(1L, "task1", 1, false, new Date()), new Task(2L, "task2", 1, false, new Date()));
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null, tasks);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        Job job = new Job(1L, "job 1", true, null,
                Arrays.asList(new Task(2L,"task 3", 1, false, new Date()), new Task(3L, "task 4", 1, false, new Date())));
        jsonJob = createMockJobAsJson(2L, "job 2", true, job,
                Arrays.asList(new Task(5L,"task 5", 1, false, new Date()), new Task(6L, "task 6", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        Job job2 = new Job(2L, "job 2", true, job,
                Arrays.asList(new Task(7L,"task 7", 1, false, new Date()), new Task(8L, "task 8", 1, false, new Date())));
        jsonJob = createMockJobAsJson(3L, "job 3", true, job2,
                Arrays.asList(new Task(9L,"task 9", 1, false, new Date()), new Task(10L, "task 10", 1, false, new Date())));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/2")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        assertTrue("job 1".equals(service.findByIdent(3L).getParentJob().getName()));

    }

    @Test
    public void testInvalidDeleteJob() throws Exception{
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null,
                Arrays.asList(new Task(1L, "task 1", 1, false, new Date()), new Task(2L, "task 2", 1, false, new Date())));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/1")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
    }

    private String createMockJobAsJson(Long id, String name, boolean active, Job parent, List<Task> tasks) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Job job = new Job(id, name, active, parent, tasks);
        return mapper.writeValueAsString(job);
    }

    private boolean isTaskOrdered(List<Task> tasks){
        boolean isOrdered = true;
        if(CollectionUtils.isEmpty(tasks)){
            Integer currentHeight = tasks.iterator().next().getWeight();
            for(Task task : tasks){
                if (currentHeight > task.getWeight()){
                    isOrdered = false;
                    break;
                } else{
                    currentHeight = task.getWeight();
                }
            }
        }
        return isOrdered;
    }
}
