package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.domain.enums.RentalStatus;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private BigDecimal price;
    private boolean returned = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fine> fines = new ArrayList<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inspection> inspections = new ArrayList<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Toll> tolls = new ArrayList<>();

    public Rental(Vehicle vehicle, Customer customer, RentalRequest request){
        this.vehicle = vehicle;
        this.customer = customer;
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.price = BigDecimal.ZERO;
        updateStatus();
    }

    public void updateStatus() {
        LocalDate hoje = LocalDate.now();

        if (returned || returnDate != null) {
            this.status = RentalStatus.DEVOLVIDA;
            return;
        }

        if (startDate.isAfter(hoje)) {
            this.status = RentalStatus.NAO_INICIADA;
            return;
        }

        if (endDate.isBefore(hoje)) {
            this.status = RentalStatus.ATRASADA;
            return;
        }

        this.status = RentalStatus.ATIVA;
    }



    @PrePersist
    @PreUpdate
    public void preSave() {
        updateStatus();
    }
}
