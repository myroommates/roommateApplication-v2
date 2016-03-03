package be.roommate.app.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 18/03/15.
 */
public class ChoreDTO extends DTO implements Comparable<ChoreDTO> {

    private Long id;

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Size(min = 1, max = 255, message = "VALIDATION_SIZE")
    private String name;

    @Size(min = 0, max = 2000, message = "VALIDATION_SIZE")
    private String description;

    private Long creatorId;

    private Long currentExecutorId;

    private Integer order;

    public ChoreDTO() {
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getCurrentExecutorId() {
        return currentExecutorId;
    }

    public void setCurrentExecutorId(Long currentExecutorId) {
        this.currentExecutorId = currentExecutorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(ChoreDTO another) {
        return this.order.compareTo(another.order);
    }
}
