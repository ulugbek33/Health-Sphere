package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.*;
import uz.pdp.healthsphere.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"
        , "https://healthsphere-kappa.vercel.app"
})
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/specialty/open")
    public List<SpecializationDTO> getSpecialty() {
        return doctorService.getSpeciality();
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<AppointmentDTO> getMyAppointments() {
        return doctorService.myAppointments();
    }

    @PostMapping("/appointments/{id}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id,
                                                 @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        doctorService.createPrescription(id, medicalRecordDTO);
        return ResponseEntity.ok("Appointment completed successfully ");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DOCTOR')")
    public DoctorDTO createDoctor(@RequestBody @Valid DoctorDTO doctorDTO) {
        return doctorService.create(doctorDTO);
    }

    @PostMapping("/create/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public DoctorScheduleDTO createDoctorSlots(@RequestBody @Valid DoctorScheduleDTO scheduleDTO) {
        return doctorService.createSlots(scheduleDTO);
    }

}
