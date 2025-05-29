package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}

