package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.RentalRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_placa", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate returnDate = null;

    private Double price;

    private boolean returned = false;

    public Rental(Vehicle vehicle, Customer customer, RentalRequest request){
        this.vehicle = vehicle;
        this.customer = customer;
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.price = request.price();
    }
}
