package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Emission;
import com.greenhouse.gh_backend.entities.EmissionScope;
import com.greenhouse.gh_backend.entities.Location;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.EmissionRepository;
import com.greenhouse.gh_backend.repositories.LocationRepository;
import com.greenhouse.gh_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
@Service
public class EmissionCalculationService {
    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private UserRepository userRepository;
    // Emission factors
    // Emission factors for different fuel types (example values)
    private static final double DIESEL_CO2_EMISSION_FACTOR = 2.68; // kg CO2 per liter
    private static final double DIESEL_CH4_EMISSION_FACTOR = 0.0001; // kg CH4 per liter
    private static final double DIESEL_N2O_EMISSION_FACTOR = 0.0001; // kg N2O per liter
    private static final double CO2_EMISSION_FACTOR = 5.3;       // kg CO2 per therm
    private static final double CH4_EMISSION_FACTOR = 0.001;     // kg CH4 per therm
    private static final double N2O_EMISSION_FACTOR = 0.0001;    // kg N2O per therm
    private static final double UPSTREAM_EMISSION_FACTOR = 6.4832; // kg CO2e per therm

    // Global Warming Potentials (GWPs)
    private static final double CH4_GWP = 28;
    private static final double N2O_GWP = 265;
    // Emission factors for electricity
    private static final double ELECTRICITY_CO2_EMISSION_FACTOR = 0.45; // kg CO2 per kWh
    private static final double ELECTRICITY_CH4_EMISSION_FACTOR = 0.0001; // kg CH4 per kWh
    private static final double ELECTRICITY_N2O_EMISSION_FACTOR = 0.00002; // kg N2O per kWh


    public double calculateCO2FromNaturalGas(double naturalGasConsumption) {
        double co2Emissions = naturalGasConsumption * CO2_EMISSION_FACTOR;
        double ch4Emissions = naturalGasConsumption * CH4_EMISSION_FACTOR * CH4_GWP;
        double n2oEmissions = naturalGasConsumption * N2O_EMISSION_FACTOR * N2O_GWP;
        double upstreamEmissions = naturalGasConsumption * UPSTREAM_EMISSION_FACTOR;

        double totalEmissionsKg = co2Emissions + ch4Emissions + n2oEmissions + upstreamEmissions;
        return totalEmissionsKg / 1000; // Convert to metric tons (tCO2e)
    }
    public double calculateCO2FromVehicle(double fuelConsumption, String fuelType) {
        double co2Emissions = 0;
        double ch4Emissions = 0;
        double n2oEmissions = 0;

        switch (fuelType.toLowerCase()) {
            case "diesel fuel":
                co2Emissions = fuelConsumption * DIESEL_CO2_EMISSION_FACTOR;
                ch4Emissions = fuelConsumption * DIESEL_CH4_EMISSION_FACTOR * CH4_GWP;
                n2oEmissions = fuelConsumption * DIESEL_N2O_EMISSION_FACTOR * N2O_GWP;
                break;
            // Add more cases for other fuel types
        }

        double totalEmissionsKg = co2Emissions + ch4Emissions + n2oEmissions;
        double totalEmissionsTons = totalEmissionsKg / 1000; // Convert to metric tons (tCO2e)

        System.out.println("Fuel Type: " + fuelType);
        System.out.println("Fuel Consumption: " + fuelConsumption);
        System.out.println("CO2 Emissions: " + co2Emissions);
        System.out.println("CH4 Emissions: " + ch4Emissions);
        System.out.println("N2O Emissions: " + n2oEmissions);
        System.out.println("Total Emissions (kg): " + totalEmissionsKg);
        System.out.println("Total Emissions (tCO2e): " + totalEmissionsTons);

        return totalEmissionsTons;
    }
    public double calculateCO2FromElectricity(double electricityConsumption) {
        double co2Emissions = electricityConsumption * ELECTRICITY_CO2_EMISSION_FACTOR;
        double ch4Emissions = electricityConsumption * ELECTRICITY_CH4_EMISSION_FACTOR * CH4_GWP;
        double n2oEmissions = electricityConsumption * ELECTRICITY_N2O_EMISSION_FACTOR * N2O_GWP;

        double totalEmissionsKg = co2Emissions + ch4Emissions + n2oEmissions;
        return totalEmissionsKg / 1000; // Convert to metric tons (tCO2e)
    }
    public Double getTotalCo2EmissionsForUserAndScope1(long userId) {
        return emissionRepository.findTotalCo2EmissionsByUserIdAndScope1(userId);
    }

    public Double getTotalCo2EmissionsForUserAndScope2(long userId) {
        return emissionRepository.findTotalCo2EmissionsByUserIdAndScope2(userId);
    }

    public Double getTotalCo2EmissionsForUserAndScope3(long userId) {
        return emissionRepository.findTotalCo2EmissionsByUserIdAndScope3(userId);
    }

    public Map<String, Double> getTotalFootprintForUser(long userId) {
        Double scope1Total = getTotalCo2EmissionsForUserAndScope1(userId);
        Double scope2Total = getTotalCo2EmissionsForUserAndScope2(userId);
        Double scope3Total = getTotalCo2EmissionsForUserAndScope3(userId);

        // Handle null values if there are no emissions for a scope
        scope1Total = (scope1Total != null) ? scope1Total : 0;
        scope2Total = (scope2Total != null) ? scope2Total : 0;
        scope3Total = (scope3Total != null) ? scope3Total : 0;

        Double totalFootprint = scope1Total + scope2Total + scope3Total;

        // Round to 3 decimal places
        final Double finalScope1Total = new BigDecimal(scope1Total).setScale(3, RoundingMode.HALF_UP).doubleValue();
        final Double finalScope2Total = new BigDecimal(scope2Total).setScale(3, RoundingMode.HALF_UP).doubleValue();
        final Double finalScope3Total = new BigDecimal(scope3Total).setScale(3, RoundingMode.HALF_UP).doubleValue();
        final Double finalTotalFootprint = new BigDecimal(totalFootprint).setScale(3, RoundingMode.HALF_UP).doubleValue();

        // Save the total footprint to the user's totalFootprint field
        userRepository.findById(userId).ifPresent(user -> {
            user.setTotalFootprint(finalTotalFootprint);
            userRepository.save(user);
        });

        Map<String, Double> footprintData = new HashMap<>();
        footprintData.put("scope1Total", finalScope1Total);
        footprintData.put("scope2Total", finalScope2Total);
        footprintData.put("scope3Total", finalScope3Total);
        footprintData.put("totalFootprint", finalTotalFootprint);

        return footprintData;
    }
}
