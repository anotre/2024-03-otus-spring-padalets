package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Addressee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AddresseeDaoImpl implements Dao<Addressee> {
    private final Map<Long, Addressee> addresses;

    public AddresseeDaoImpl() {
        var addressBookMap = new HashMap<Long, Addressee>();
        addressBookMap.put(1L, new Addressee(1L, "firstAddresseeFlow.input"));
        addressBookMap.put(2L, new Addressee(2L, "secondAddresseeFlow.input"));
        addressBookMap.put(3L, new Addressee(3L, "thirdAddresseeFlow.input"));
        addressBookMap.put(4L, new Addressee(4L, "fourthAddresseeFlow.input"));

        this.addresses = addressBookMap;
    }

    @Override
    public int size() {
        return addresses.size();
    }

    @Override
    public Addressee get(Long id) {
        return addresses.get(id);
    }

    @Override
    public List<Addressee> getAll() {
        return Collections.unmodifiableList(new ArrayList<Addressee>(this.addresses.values()));
    }
}
