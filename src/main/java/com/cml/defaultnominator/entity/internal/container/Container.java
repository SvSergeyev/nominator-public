package com.cml.defaultnominator.entity.internal.container;

import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.cml.defaultnominator.service.namedobject.AbstractNamedObjectService.DEFAULT_PARENT_ID;

@Entity
@Table(name = "container")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Container extends AbstractNamedObjectEntity {

    /**
     * Код подразделения
     */
    @Column(name = "division_code")
    String divisionCode;

    /**
     * Тип анализа
     */
    @Column(name = "analysis_type")
    String analysisType;

    /**
     * Номер версии (начинается с 001)
     */
    @Column(name = "version")
    String version;

    /**
     * Определяет, в каком дереве находится объект - приватном или общем(проектном)
     */
    @Column(name = "is_project_tree_object")
    boolean isProjectTreeObject = false;

    /**
     * objectId корневого проекта.
     * Указывается только в том случае, если объект находится не в приватном дереве
     */
    @Column(name = "root_project_id")
    Integer rootProjectId;

    @Override
    public String toString() {
        return "Container{" +
                "id='" + getId() + '\'' +
                ", objectId='" + getObjectId() + '\'' +
                ", productIndex='" + getProductIndex() + '\'' +
                ", numEntity='" + getNumEntity() + '\'' +
                ", pid='" + getPid() + '\'' +
                ", type='" + getType() + '\'' +
                ", divisionCode='" + divisionCode + '\'' +
                ", analysisType='" + analysisType + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public boolean isInitial() {
        return getPid() == DEFAULT_PARENT_ID;
    }
}
