package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.*;
import uz.pdp.healthsphere.dto.request.AppointmentRequestDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.mapper.DoctorMapper;
import uz.pdp.healthsphere.projection.DoctorProjection;
import uz.pdp.healthsphere.repository.DoctorRepository;
import uz.pdp.healthsphere.service.AppointmentService;
import uz.pdp.healthsphere.service.PatientService;
import uz.pdp.healthsphere.service.ai.AIService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final AIService aiService;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @GetMapping("/recommend-doctors")
    public ResponseEntity<?> recommendDoctors(@RequestParam String symptoms) {
        // 1. AI orqali mutaxassisni aniqlaymiz
        String recommendedSpec = aiService.analyzeSymptoms(symptoms);

        // 2. Bazadan shifokorlarni topamiz
        List<Doctor> doctors = doctorRepository.findAllBySpecializationNameIgnoreCase(recommendedSpec.trim());

        // 3. MUHIM: Entity ro'yxatini DTO ro'yxatiga o'giramiz
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(doctorMapper::toDTO)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("aiRecommendation", recommendedSpec);
        result.put("suggestedDoctors", doctorDTOs); // Endi DTO qaytadi

        return ResponseEntity.ok(result);
    }

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

    @GetMapping("/my-appointments")
    public List<AppointmentDTO> getMyAppointments() {
        return patientService.getAppointments();
    }

    @GetMapping("/my-invoices")
    public List<InvoiceDTO> getMyInvoices() {
        return patientService.getMyInvoices();
    }

    @PostMapping("/appointments")
    public AppointmentDTO createAppointment(@RequestBody @Valid AppointmentRequestDTO appointmentRequestDTO) {
        return appointmentService.createAppointment(appointmentRequestDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody @Valid PatientDTO patientDTO) {
        return ResponseEntity.ok(patientService.create(patientDTO));
    }

    @DeleteMapping("/appointments/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(" Qabulingiz muvaffaqiyatli bekor qilindi ☺️");
    }

}
