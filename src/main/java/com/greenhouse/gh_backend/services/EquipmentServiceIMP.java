package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Equipment;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.EquipmentRepository;
import com.greenhouse.gh_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceIMP implements IEquipment {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Equipment> getEquipmentsByUserId(Long userId) {
        return equipmentRepository.findByUserIdUser(userId);
    }

    @Override
    public Equipment saveEquipment(Equipment equipment, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        equipment.setUser(user);
        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public Equipment updateEquipment(Equipment equipment, Long id) {
        Equipment existingEquipment = equipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Equipment not found"));
        existingEquipment.setEquipmentType(equipment.getEquipmentType());
        existingEquipment.setQuantity(equipment.getQuantity());
        existingEquipment.setPowerRating(equipment.getPowerRating());
        existingEquipment.setAnnualUsageHours(equipment.getAnnualUsageHours());
        existingEquipment.setEmissionFactor(equipment.getEmissionFactor());
        existingEquipment.setCo2Emissions(equipment.getCo2Emissions());
        return equipmentRepository.save(existingEquipment);
    }
}
