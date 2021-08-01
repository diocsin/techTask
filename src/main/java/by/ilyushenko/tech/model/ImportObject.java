package by.ilyushenko.tech.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ImportObject {
    @Id
    @GenericGenerator(
            name = "ID_GENERATOR_POOLED",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "JPWH_SEQUENCE"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "increment_size",
                            value = "1000"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "optimizer",
                            value = "pooled-lo"
                    )
            }
    )
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long pid;

    private String eventName;

    private String formName;

    private String variableName;

    private String value;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImportObject)) return false;
        ImportObject that = (ImportObject) o;
        return Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }

    @Override
    public String toString() {
        return "ImportObject{" +
                "pid=" + pid +
                ", eventName='" + eventName + '\'' +
                ", formName='" + formName + '\'' +
                ", variableName='" + variableName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
