package util;

public class SwaggerPojo {


    private String summary;

    private String[] tags;

    private String description;

    private String[] produces;
    private String[] consumes;
    private SwaggerParameters[] parameters;

    /**
     * @return the consumes
     */
    public String[] getConsumes() {
        return consumes;
    }

    /**
     * @param consumes the consumes to set
     */
    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public SwaggerParameters[] getParameters() {
        return parameters;
    }

    public void setParameters(SwaggerParameters[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ClassPojo [summary = " + summary + ", tags = " + tags + ", description = " + description + ", produces = " + produces + ", parameters = " + parameters + "]";
    }

}
