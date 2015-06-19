package ro.sapientia2015.story.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;

/**
 * @author Kiss Tibor
 */
public class SprintDTO {

    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;

    private Sprint.Builder builder = new Sprint.Builder();
    
    @NotEmpty
    @Length(max = 20)
    private String title;

    private String fromDt;
    private String toDt;
    
    private List<Story> stories;
    
    public SprintDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    
    public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public Sprint.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Sprint.Builder builder) {
		this.builder = builder;
	}
}
