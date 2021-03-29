package ru.vokazak.entity;

import lombok.*;

import javax.persistence.*;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "Category.delete", query = "DELETE FROM Category c where c.transType = :name"),
        @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c where c.transType = :name"),
        @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trans_type")
    private String transType;


}
