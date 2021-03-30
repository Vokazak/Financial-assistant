package ru.vokazak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = "Account.delete", query = "DELETE FROM Account a where a.name = :name and a.userId = :userId"),
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a where a.userId = :userId"),
        @NamedQuery(name = "Account.find", query = "SELECT a FROM Account a where a.name = :name and a.userId = :userId"),
})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "accFrom", fetch = FetchType.LAZY)
    private List<Transaction> transOut;

    @OneToMany(mappedBy = "accTo", fetch = FetchType.LAZY)
    private List<Transaction> transIn;


}
