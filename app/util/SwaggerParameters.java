package util;

public class SwaggerParameters {
    private String name;

    private String type;

    private boolean required;

    private String in;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    @Override
    public String toString() {
        return "ClassPojo [name = " + name + ", type = " + type + ", required = " + required + ", in = " + in + "]";
    }
}
