package ru.shoppinglive.model.entity.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by rkhabibullin on 13.09.2017.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="soa_tasks")
public class Task {
    @Id
    @GeneratedValue
    private int id;
    private String name;
}
