package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.projection.DoctorProjection;

import java.math.BigDecimal;
import java.util.List;

public interface PatientService {

    List<DoctorProjection> getAllDoctors(String specialization, BigDecimal maxFee);

    PatientDTO create(PatientDTO patientDTO);

    List<AppointmentDTO> getAppointments();

}
