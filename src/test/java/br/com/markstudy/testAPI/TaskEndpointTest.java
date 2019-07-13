package br.com.markstudy.testAPI;

import br.com.markstudy.testAPI.controller.JobController;
import br.com.markstudy.testAPI.controller.TaskController;
import br.com.markstudy.testAPI.domain.entity.Job;
import br.com.markstudy.testAPI.domain.entity.Task;
import br.com.markstudy.testAPI.service.interfaces.TaskService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TaskEndpointTest {

    private MockMvc mockMvcJob;

    private MockMvc mockMvcTask;

    @Autowired
    private TaskController controller;

    @Autowired
    private JobController jobController;

    @Autowired
    private TaskService service;

    @Before
    public void setUp(){
        this.mockMvcJob = MockMvcBuilders.standaloneSetup(jobController).build();
        this.mockMvcTask = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @After
    public void clean(){
       service.deleteAll();
    }

    @Test
    public void testPostValidTask() throws Exception {
        String jsonTask = createMockTaskAsJson(1L, "Task 1", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testPostInvalidTask() throws Exception {
        String jsonTask = createMockTaskAsJson(null, null, true, 1, null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.PRECONDITION_FAILED.value()));
    }

    @Test
    public void testPutValidTask() throws Exception {
        String jsonTask = createMockTaskAsJson(1L, "Task 1", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        jsonTask = createMockTaskAsJson(1L, "Task 1-1", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.put("/tasks/1")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testPutInvalidTask() throws Exception {
        String jsonTask = createMockTaskAsJson(1L, "Task 1", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        jsonTask = createMockTaskAsJson(1L, null, true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.put("/tasks/1")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.PRECONDITION_FAILED.value()));
    }

    @Test
    public void testDeleteAssociatedToJob() throws Exception{
        List<Task> tasks = Arrays.asList(new Task(1L, "task1", 1, false, new Date()), new Task(2L, "task2", 1, false, new Date()));
        String jsonJob = createMockJobAsJson(1L, "job 1", true, null, tasks);
        this.mockMvcJob.perform(MockMvcRequestBuilders.post("/jobs")
                .content(jsonJob).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        Job job = new Job(1L, "job 1", true, null,
                Arrays.asList(new Task(2L,"task 3", 1, false, new Date()), new Task(3L, "task 4", 1, false, new Date())));
        this.mockMvcTask.perform(MockMvcRequestBuilders.delete("/tasks/2")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testGetList() throws Exception {
        String jsonTask = createMockTaskAsJson(1L, "Task 1", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

        jsonTask = createMockTaskAsJson(2L, "Task 2", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        jsonTask = createMockTaskAsJson(3L, "Task 3", true,1,  null);
        this.mockMvcTask.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonTask).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        this.mockMvcTask.perform(MockMvcRequestBuilders.get("/tasks")
                .param("createdAt", "05-05-2019").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

    }

    private String createMockJobAsJson(Long id, String name, boolean active, Job parent, List<Task> tasks) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Job job = new Job(id, name, active, parent, tasks);
        return mapper.writeValueAsString(job);
    }

    private String createMockTaskAsJson(Long id, String name, boolean completed, Integer height, Job parent) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Task task = new Task(id, name, height, completed, new Date(), parent);
        return mapper.writeValueAsString(task);
    }

}
