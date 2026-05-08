package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.DoctorDTO;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
@CrossOrigin(origins = {"http://localhost:5173"
//        , "https://tastelab-tawny.vercel.app"
})
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/my-appointments")
    public List<AppointmentDTO> getMyAppointments() {
        return doctorService.myAppointments();
    }

    @PostMapping("/appointments/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id,
                                                 @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        doctorService.createPrescription(id, medicalRecordDTO);
        return ResponseEntity.ok("Appointment completed successfully ");
    }

    @PostMapping("/create")
    public DoctorDTO createDoctor(@RequestBody @Valid DoctorDTO doctorDTO) {
        return doctorService.create(doctorDTO);
    }

}
