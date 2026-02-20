package com.utc.bancario1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.utc.bancario1.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    boolean existsByCliente_Id(Long clienteId);
}
