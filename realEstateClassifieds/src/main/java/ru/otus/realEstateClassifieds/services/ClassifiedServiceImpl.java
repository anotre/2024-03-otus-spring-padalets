package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.ClassifiedDto;
import ru.otus.realEstateClassifieds.dto.ClassifiedSearchDto;
import ru.otus.realEstateClassifieds.dto.mappers.ClassifiedMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.models.Classified;
import ru.otus.realEstateClassifieds.repositories.ClassifiedRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.balconyTypeAnyOf;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.ceilingHeightBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.constructionMethodAnyOf;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.countryExactly;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.floorNumberBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.kitchenAreaBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.livingAreaBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.priceBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.regionExactly;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.residentialFloorAreaBetween;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.roomNumberAnyOf;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.settlementExactly;
import static ru.otus.realEstateClassifieds.repositories.specs.ClassifiedSpecs.totalAreaBetween;

@Service
@RequiredArgsConstructor
public class ClassifiedServiceImpl implements ClassifiedService {
    private final ClassifiedRepository repository;

    private final ClassifiedMapper mapper;

    private final ConstructionMethodService constructionMethodService;

    private final BalconyTypeService balconyTypeService;

    private final FurnishedTypeService furnishedTypeService;

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassifiedDto> findById(long id) {
        return this.repository.findById(id).map(this.mapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassifiedDto> findByParams(ClassifiedSearchDto searchRequest) {
        constructionMethodService.findAll();
        balconyTypeService.findAll();
        furnishedTypeService.findAll();

        var specification = this.makeSpecification(searchRequest);
        return specification.map(repository::findAll)
                .orElseGet(repository::findAll).stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public ClassifiedDto create(ClassifiedDto classified) {
        if (classified.getId() != 0) {
            throw new EntityAlreadyExistsException("The entity to be created must not exist");
        }

        return this.save(classified);
    }

    @Override
    @Transactional
    public ClassifiedDto update(ClassifiedDto classified) {
        if (classified.getId() == 0) {
            throw new EntityNonExistsException("The entity to be created must exist");
        }

        return this.save(classified);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    private ClassifiedDto save(ClassifiedDto classified) {
        var savedEntity = this.repository.save(this.mapper.dtoToEntity(classified));
        return this.mapper.entityToDto(savedEntity);
    }

    private Optional<Specification<Classified>> makeSpecification(ClassifiedSearchDto searchRequest) {
        var specificationList = this.buildSpecificationList(searchRequest);

        if (specificationList.isEmpty()) {
            return Optional.empty();
        }

        var specification = Specification.where(specificationList.get(0));

        for (int i = 1; i < specificationList.size(); i++) {
            specification.and(specificationList.get(i));
        }

        return Optional.of(specification);
    }

    private List<Specification<Classified>> buildSpecificationList(ClassifiedSearchDto searchRequest) {
        return Stream.of(settlementExactly(
                                searchRequest.getSettlementId()),
                        regionExactly(searchRequest.getRegionId()),
                        countryExactly(searchRequest.getCountryId()),
                        priceBetween(searchRequest.getPriceFrom(), searchRequest.getPriceTo()),
                        totalAreaBetween(searchRequest.getTotalAreaFrom(), searchRequest.getTotalAreaTo()),
                        kitchenAreaBetween(searchRequest.getKitchenAreaFrom(), searchRequest.getKitchenAreaTo()),
                        livingAreaBetween(searchRequest.getLivingAreaFrom(), searchRequest.getLivingAreaTo()),
                        residentialFloorAreaBetween(
                                searchRequest.getResidentialFloorAreaFrom(),
                                searchRequest.getResidentialFloorAreaTo()),
                        ceilingHeightBetween(searchRequest.getCeilingHeightFrom(), searchRequest.getCeilingHeightTo()),
                        floorNumberBetween(searchRequest.getFloorNumberFrom(), searchRequest.getFloorNumberTo()),
                        constructionMethodAnyOf(searchRequest.getConstructionMethods()),
                        roomNumberAnyOf(searchRequest.getRoomNumbers()),
                        balconyTypeAnyOf(searchRequest.getBalconyTypes())
                )
                .filter(Objects::nonNull)
                .toList();
    }
}