package com.cml.defaultnominator.entity.internal.target;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "combinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "target"})
public class CheckedCombination {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckedCombination.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    private int version;
    private String state;

    @OneToOne(mappedBy = "combination")
    Target target;

    @Override
    public String toString() {
        return "CheckedCombination{" +
                "version=" + version +
                ", state='" + state + '\'' +
                '}';
    }
}
