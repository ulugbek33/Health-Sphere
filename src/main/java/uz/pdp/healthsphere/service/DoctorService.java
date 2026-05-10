package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import uz.pdp.healthsphere.dto.*;

import java.util.List;

public interface DoctorService {

    List<AppointmentDTO> myAppointments();

    MedicalRecordDTO createPrescription(Long id, MedicalRecordDTO medicalRecordDTO);

    DoctorDTO create(DoctorDTO doctorDTO);

    List<SpecializationDTO> getSpeciality();

    DoctorScheduleDTO createSlots(DoctorScheduleDTO scheduleDTO);
}
