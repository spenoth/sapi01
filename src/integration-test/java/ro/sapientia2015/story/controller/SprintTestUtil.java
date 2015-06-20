package ro.sapientia2015.story.controller;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.model.Sprint;

public class SprintTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE2 = "title2";
    public static final String TITLE_UPDATED = "updatedTitle";
    
    public static final String FROM_DATE = "06/26/2015 09:05:34 PM";
    public static final String TO_DATE = "06/30/2015 09:05:34 PM";

    private static final String CHARACTER = "a";

    public static SprintDTO createFormObject(Long id, String description, String title, String from, String to) {
        SprintDTO dto = new SprintDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        dto.setFromDt(from);
        dto.setToDt(to);

        return dto;
    }

    public static Sprint createModel(Long id, String description, String title, String from, String to) {
        Sprint model = Sprint.getBuilder(title)
                .description(description)
                .setFromDt(from)
                .setToDt(to)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
