package uz.pdp.healthsphere.service;

import org.springframework.http.ResponseEntity;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;

import java.util.List;

public interface DoctorService {

    List<AppointmentDTO> myAppointments();

    MedicalRecordDTO createPrescription(Long id, MedicalRecordDTO medicalRecordDTO);

}
