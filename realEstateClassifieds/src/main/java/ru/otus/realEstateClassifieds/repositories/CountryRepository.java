package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}