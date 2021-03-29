package ru.vokazak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trans_date")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "trans_money")
    private BigDecimal money;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acc_from_id")
    private Account accFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acc_to_id")
    private Account accTo;

    @ManyToMany
    @JoinTable(
        name = "transaction_to_category",
        joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", money=" + money +
                '}';
    }
}
