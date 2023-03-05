package com.cml.defaultnominator.entity.internal.target;

import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;
import com.cml.defaultnominator.service.target.FunctionalNumberField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static com.cml.defaultnominator.service.namedobject.AbstractNamedObjectService.DEFAULT_PARENT_ID;

@Entity
@Table(name = "target")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Target extends AbstractNamedObjectEntity {

    /**
     * Хэш функционального номера
     * Упрощает сравнение функциональных номеров двух сущностей
     */
    int fnHash;

    /**
     * Номер ревизии
     * Пересчитывается только для объектов с type=target. Для targetgroup всегда null
     */
    String revision;

    /**
     * Имя объекта
     * Задается пользователем при создании в дополнение к генерируемому коду
     */
    String name;

    /**
     * Поле функционального номера
     */
    String aa;

    /**
     * Поле функционального номера
     */
    String bb;

    /**
     * Поле функционального номера
     */
    String cc;

    /**
     * Поле функционального номера
     */
    String dd;

    /**
     * Уровень вложенности
     * Необходим для получения наследуемых уровней функционального номера
     */
    int nestingLevel;

    /**
     * Последняя проверенная комбинация, состоящая из номера версии объекта с type=target и состояния этой версии
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_checked_combination_id")
    CheckedCombination combination;

    @Transient
    Map<String, String> functionalNumber = new HashMap<>();

    public Map<String, String> collectFn() {
        functionalNumber.put(FunctionalNumberField.AA, aa);
        functionalNumber.put(FunctionalNumberField.BB, bb);
        functionalNumber.put(FunctionalNumberField.CC, cc);
        functionalNumber.put(FunctionalNumberField.DD, dd);
        return functionalNumber;
    }

    public void extractAndSetFnFields(Map<String, String> fn) {
        aa = fn.get(FunctionalNumberField.AA);
        bb = fn.get(FunctionalNumberField.BB);
        cc = fn.get(FunctionalNumberField.CC);
        dd = fn.get(FunctionalNumberField.DD);
    }

    public final String getFormattedName() {
        StringBuilder out = new StringBuilder();
        out.append(this.getProductIndex()).append("-")
                .append(aa);
        if (bb != null) {
            out.append("-").append(bb);
        }
        if (cc != null) {
            out.append(cc);
        }
        if (dd != null) {
            out.append(dd);
        }
        out.append("-").append(this.getNumEntity());
        if (revision != null) {
            out.append("_").append(revision);
        }
        out.append(" ").append(name);
        return out.toString();
    }

    @Override
    public String toString() {
        return "Target{" +
                "id='" + this.getId() + '\'' +
                ", pid='" + this.getPid() + '\'' +
                ", productIndex='" + this.getProductIndex() + '\'' +
                ", revision='" + revision + '\'' +
                ", name='" + name + '\'' +
                ", functionalNumber=" + collectFn() +
                ", type=" + this.getType() +
                '}';
    }

    public boolean isInitial() {
        return getPid() == DEFAULT_PARENT_ID;
    }
}
