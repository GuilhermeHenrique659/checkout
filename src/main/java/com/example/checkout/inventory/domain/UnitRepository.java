package com.example.checkout.inventory.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UnitRepository {
    Unit save(Unit unit);

    List<Unit> saveMany(List<Unit> units);

    List<Unit> getValuesByProductIdAndStatus(Map<UUID, Integer> productIds, UnitStatus status);
}
