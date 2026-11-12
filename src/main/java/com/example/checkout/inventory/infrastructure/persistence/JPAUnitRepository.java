package com.example.checkout.inventory.infrastructure.persistence;

import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.domain.UnitRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JPAUnitRepository implements UnitRepository {
    private final UnitDao unitDao;

    public JPAUnitRepository(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Override
    public Unit save(Unit unit) {
        unitDao.save(new UnitModel(
                unit.id(),
                unit.productId(),
                unit.sku(),
                unit.status()
        ));

        return unit;
    }

    @Override
    public List<Unit> saveMany(List<Unit> units) {
        unitDao.saveAll(units.stream().map(unit -> new UnitModel(
                unit.id(),
                unit.productId(),
                unit.sku(),
                unit.status()
        )).toList());

        return units;
    }

    @Override
    public List<Unit> getValuesByProductIdAndStatus(Map<UUID, Integer> productIds, String status) {
        var ids = productIds.keySet().toArray(UUID[]::new);
        var quantities = Arrays.stream(ids).map(productIds::get).toArray(Integer[]::new);

        return unitDao.getAvailable(ids, quantities, status)
                .stream().map(data -> new Unit(data.getStatus(), data.getSku(), data.getProductId(), data.getId()))
                .toList();
    }
}
