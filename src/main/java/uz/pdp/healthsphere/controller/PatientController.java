package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.dto.request.AppointmentRequestDTO;
import uz.pdp.healthsphere.projection.DoctorProjection;
import uz.pdp.healthsphere.service.AppointmentService;
import uz.pdp.healthsphere.service.PatientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
@CrossOrigin(origins = {"http://localhost:5173"
        , "https://healthsphere-kappa.vercel.app"
})
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @GetMapping("/doctors")
    public List<DoctorProjection> getDoctors(@RequestParam(required = false) String specialization,
                                             @RequestParam(required = false) BigDecimal maxFee) {
        return patientService.getAllDoctors(specialization, maxFee);
    }

    @GetMapping("/{doctorId}/slots")
    public List<LocalTime> getSlots(@PathVariable Long doctorId,
                                    @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableSlots(doctorId, date);
    }

    @PostMapping("/appointments")
    public AppointmentDTO createAppointment(@RequestBody @Valid AppointmentRequestDTO appointmentRequestDTO) {
        return appointmentService.createAppointment(appointmentRequestDTO);
    }

}
