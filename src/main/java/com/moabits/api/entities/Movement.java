package com.moabits.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movements")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "movement_type_id")
    private Long movementTypeId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "created_at")
    private Date createdAt;
}
