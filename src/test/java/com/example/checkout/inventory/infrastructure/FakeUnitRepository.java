package com.example.checkout.inventory.infrastructure;

import com.example.checkout.inventory.domain.UnitStatus;
import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.domain.UnitRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeUnitRepository implements UnitRepository {
    private final Map<UUID, Unit> units;

    public FakeUnitRepository(List<Unit> units) {
        this.units = units.stream().collect(Collectors.toMap(
                Unit::id,
                Function.identity()
        ));
    }

    @Override
    public List<Unit> saveMany(List<Unit> units) {
        return List.of();
    }

    @Override
    public List<Unit> getValuesByProductIdAndStatus(Map<UUID, Integer> productIds, UnitStatus status) {
        return units
                .values()
                .stream()
                .filter(unit -> productIds.keySet().stream().toList().contains(unit.productId()) && unit.status() == status)
                .toList();
    }

    @Override
    public Unit save(Unit unit) {
        units.put(unit.id(), unit);

        return unit;
    }
}
