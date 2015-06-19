package ro.sapientia2015.story.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.SprintService;

@Controller
public class SprintController {

	@Resource
	private SprintService service;
	
	public static final String VIEW_LIST = "sprint/list";
	public static final String VIEW_ADD = "sprint/add";

    protected static final String PARAMETER_ID = "id";
	
	private static final String MODEL_ATTRIBUTE = "sprint";

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
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) throws ParseException {
		if(result.hasErrors()){
			return VIEW_ADD;
		}
	
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy HH:mm:ss a");
        Date from = format.parse(dto.getFromDt());
        Date to = format.parse(dto.getToDt());
		
        if(from.after(to) || from.equals(to)) {
       	 return VIEW_ADD;
       }
        
		Sprint added = service.add(dto);
        attributes.addAttribute(PARAMETER_ID, added.getId());
		
		return createRedirectViewPath("/sprint/list");
	}
	
}
