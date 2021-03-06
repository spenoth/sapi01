package ro.sapientia2015.story.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.controller.SprintTestUtil;
import ro.sapientia2015.story.controller.SprintController;
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.model.Sprint;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

/**
 * This test uses the annotation based application context configuration.
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("sprintData.xml")
public class ITSprintControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";
    private static final String FORM_FIELD_FROM = "fromDt";
    private static final String FORM_FIELD_TO = "toDt";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("sprintData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/sprint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(SprintController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/sprint/add.jsp"))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
        		.andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("fromDt", isEmptyOrNullString())))
        		.andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("toDt", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("sprintData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/sprint/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(StoryController.MODEL_ATTRIBUTE, new SprintDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(SprintController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/sprint/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(SprintController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
        		.andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("fromDt", isEmptyOrNullString())))
        		.andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("fromDt", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("sprintData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = SprintTestUtil.createStringWithLength(Sprint.MAX_LENGTH_TITLE + 1);
        String description = SprintTestUtil.createStringWithLength(Sprint.MAX_LENGTH_DESCRIPTION + 1);

        String d1 = "05/29/2015 09:05:34 PM";
        String d2 = "05/30/2015 09:05:34 PM";
        
        
        mockMvc.perform(post("/sprint/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_TITLE, title)
                .param(FORM_FIELD_FROM, d1)
                .param(FORM_FIELD_TO, d2)
                .sessionAttr(SprintController.MODEL_ATTRIBUTE, new SprintDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(SprintController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/sprint/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(SprintController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(SprintController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("title", is(title))))
		        .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("fromDt", is(d1))))
		        .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("toDt",   is(d2))));
    }
//
    @Test
    @ExpectedDatabase(value="oneSprint.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = SprintTestUtil.createRedirectViewPath("/sprint/list");

        String d1 = "05/29/2015 09:05:34 PM";
        String d2 = "05/30/2015 09:05:34 PM";
                
        mockMvc.perform(post("/sprint/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_TITLE, "title")
                .param(FORM_FIELD_FROM, d1)
                .param(FORM_FIELD_TO, d2)
                .sessionAttr(SprintController.MODEL_ATTRIBUTE, new SprintDTO())
        )
                .andExpect(status().isOk());
    }
//
    @Test
    @ExpectedDatabase("sprintData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/sprint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name(SprintController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/sprint/list.jsp"))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE_LIST, hasSize(1)))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Bar")),
                                hasProperty("fromDt", is("06/26/2015 09:05:34 PM")),
                                hasProperty("toDt", is("06/29/2015 09:05:34 PM"))
                        )
                )));
    }
//
    @Test
    @ExpectedDatabase("sprintData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("sprint/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(SprintController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/sprint/view.jsp"))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(SprintController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }
//
    @Test
    @ExpectedDatabase("sprintData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/sprint/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

