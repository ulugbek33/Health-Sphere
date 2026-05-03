package uz.pdp.healthsphere.service;

import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.projection.DoctorProjection;

import java.math.BigDecimal;
import java.util.List;

public interface PatientService {

    List<DoctorProjection> getAllDoctors(String specialization, BigDecimal maxFee);

}
