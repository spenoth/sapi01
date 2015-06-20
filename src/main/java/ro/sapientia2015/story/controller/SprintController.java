package ro.sapientia2015.story.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.SprintService;

@Controller
public class SprintController {

	@Resource
	private SprintService service;
	
    @Resource
    private MessageSource messageSource;
	
    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.sprint.added";
    
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
	
    protected static final String REQUEST_MAPPING_VIEW = "sprint/{id}";
    
	public static final String VIEW_LIST = "sprint/list";
	public static final String VIEW_ADD = "sprint/add";

    protected static final String PARAMETER_ID = "id";
	
	protected static final String MODEL_ATTRIBUTE = "sprint";
	
    protected static final String MODEL_ATTRIBUTE_LIST = "sprints";
    
    protected static final String VIEW_VIEW = "sprint/view";
    
    protected static final String REQUEST_MAPPING_LIST = "s/";
    
    protected static final String VIEW_UPDATE = "sprint/update";

	@RequestMapping(value = "/sprint/list", method = RequestMethod.GET)
	public String listSprints(Model model) {

		List<Sprint> sprints = service.findAll();
		model.addAttribute("sprints", sprints);
		return VIEW_LIST;
	}
	
    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
    

    
	@RequestMapping(value = "/sprint/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		SprintDTO sprint = new SprintDTO();
		model.addAttribute("sprint", sprint);
		return VIEW_ADD;
	}

	@RequestMapping(value = "/sprint/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) throws ParseException
	{
		if(result.hasErrors()){
			return VIEW_ADD;
		}
	
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
        Date from = format.parse(dto.getFromDt());
        Date to = format.parse(dto.getToDt());
		
        if(from.after(to) || from.equals(to)) {
       	 return VIEW_ADD;
       }
        
		Sprint added = service.add(dto);
        attributes.addAttribute(PARAMETER_ID, added.getId());
		
		return createRedirectViewPath("/sprint/list");
	}
	
    private SprintDTO constructFormObjectForUpdateForm(Sprint updated) {
        SprintDTO dto = new SprintDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());
        dto.setFromDt(updated.getFromDt());
        dto.setToDt(updated.getToDt());

        return dto;
    }
	
    @RequestMapping(value = "/sprint/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Sprint updated = service.findById(id);
        SprintDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }
	
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Sprint> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
    
    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, messageParameters, current);
    }
	
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Sprint found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
}
