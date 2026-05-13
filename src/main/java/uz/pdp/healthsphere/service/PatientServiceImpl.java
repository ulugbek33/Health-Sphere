package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.Patient;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityAlreadyException;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.mapper.AppointmentMapper;
import uz.pdp.healthsphere.mapper.DoctorMapper;
import uz.pdp.healthsphere.mapper.DoctorScheduleMapper;
import uz.pdp.healthsphere.mapper.PatientMapper;
import uz.pdp.healthsphere.projection.DoctorProjection;
import uz.pdp.healthsphere.repository.AppointmentRepository;
import uz.pdp.healthsphere.repository.DoctorRepository;
import uz.pdp.healthsphere.repository.DoctorScheduleRepository;
import uz.pdp.healthsphere.repository.PatientRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<DoctorProjection> getAllDoctors(String specialization, BigDecimal maxFee) {

        return doctorRepository.findByDoctorsByFilter(specialization, maxFee);
    }

    @Override
    public List<AppointmentDTO> getAppointments() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Bemor profili topilmadi : " + user.getFullName(), HttpStatus.NOT_FOUND));

        List<Appointment> appointments = appointmentRepository.findAllByPatient(patient);

        return appointmentMapper.toDTO(appointments);
    }

    @Override
    @Transactional
    public PatientDTO create(PatientDTO patientDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (patientRepository.existsByUserId(currentUser.getId()))
            throw new RuntimeException("Siz allaqachon PATIENT bo'lib ro'yhatdan o'tgansiz : " + currentUser.getFullName());

        Patient patient = patientMapper.toEntity(patientDTO);
        patient.setUser(currentUser);

        Patient saved = patientRepository.save(patient);

        return patientMapper.toDTO(saved);
    }

}
