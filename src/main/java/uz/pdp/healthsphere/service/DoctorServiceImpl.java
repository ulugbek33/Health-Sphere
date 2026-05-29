package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.*;
import uz.pdp.healthsphere.entity.*;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;
import uz.pdp.healthsphere.enums.InvoiceStatus;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.enums.ToggleStatus;
import uz.pdp.healthsphere.exceptions.AppointmentStatusException;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.mapper.*;
import uz.pdp.healthsphere.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Override
    public List<AppointmentDTO> myAppointments() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getToggleStatus() == ToggleStatus.DISABLED)
            throw new AccessDeniedException("Sizning hisobingiz bloklangan! Amalni bajarishga ruxsat yo'q.");

        Doctor doctor = doctorRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Bu foydalanuvchi doctor emas ", HttpStatus.NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<DayOfWeekEnum> workDays = doctorScheduleRepository.findByDoctor(doctor)
                .stream()
                .map(DoctorSchedule::getDayOfWeek)
                .toList();

        List<LocalDate> targetDates = Stream.of(today, tomorrow)
                .filter(date -> workDays.contains(DayOfWeekEnum.valueOf(date.getDayOfWeek().name())))
                .toList();

        if (targetDates.isEmpty())
            throw new EntityNotFoundException("Bugun va ertaga ish kuningiz yo'q. " + "Yaqin ish kunlaringiz: " + workDays, HttpStatus.NOT_FOUND);

        List<Appointment> appointments = appointmentRepository.findByDoctorAndStatusAndDates(
                doctor,
                StatusEnum.CONFIRMED,
                targetDates);

        return appointmentMapper.toDTO(appointments);
    }

    @Override
    @Transactional
    public MedicalRecordDTO createPrescription(Long id, MedicalRecordDTO medicalRecordDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getToggleStatus() == ToggleStatus.DISABLED)
            throw new AccessDeniedException("Sizning hisobingiz bloklangan! Amalni bajarishga ruxsat yo'q.");

        Doctor doctor = doctorRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Doctor topilmadi ", HttpStatus.NOT_FOUND));

        Appointment appointment = appointmentRepository.getByIdOrThrow(id);

        if (!appointment.getDoctor().getId().equals(doctor.getId()))
            throw new SecurityException(" : Bu qabul sizga tegishli emas !!!");

        if (appointment.getStatus() != StatusEnum.CONFIRMED)
            throw new AppointmentStatusException("Faqat confirmed qabulni yakunlashi mumkin ", HttpStatus.BAD_REQUEST);

        appointment.setStatus(StatusEnum.COMPLETED);
        appointmentRepository.save(appointment);

        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord.setAppointment(appointment);
        medicalRecord = medicalRecordRepository.save(medicalRecord);

        List<Prescription> prescriptions = new ArrayList<>();
        for (PrescriptionDTO prescriptionDTO : medicalRecordDTO.getPrescriptions()) {

            Medicine medicine = medicineRepository.getByIdOrThrow(prescriptionDTO.getMedicineId());

            Prescription prescription = new Prescription();
            prescription.setMedicine(medicine);
            prescription.setDurationDays(prescriptionDTO.getDurationDays());
            prescription.setDosage(prescriptionDTO.getDosage());
            prescription.setMedicalRecord(medicalRecord);

            prescriptions.add(prescription);
        }
        prescriptionRepository.saveAll(prescriptions);

        Invoice invoice = new Invoice();
        invoice.setPatient(appointment.getPatient());
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setTotalAmount(appointment.getDoctor().getConsultationFee());
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setAppointment(appointment);

        invoiceRepository.save(invoice);

        return medicalRecordMapper.toDTO(medicalRecord);
    }

    @Override
    @Transactional
    public DoctorDTO create(DoctorDTO doctorDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getToggleStatus() == ToggleStatus.DISABLED)
            throw new AccessDeniedException("Sizning hisobingiz bloklangan! Amalni bajarishga ruxsat yo'q.");

        if (doctorRepository.existsByUserId(currentUser.getId()))
            throw new RuntimeException("Siz allaqachon shifokor sifatida ro'yhatdan o'tgansiz : " + currentUser.getUsername());

        Specialization specialization = specializationRepository.getByIdOrThrow(doctorDTO.getSpecialtyId());

        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor.setUser(currentUser);
        doctor.setSpecialization(specialization);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return doctorMapper.toDTO(savedDoctor);
    }

    @Override
    public List<SpecializationDTO> getSpeciality() {

        List<Specialization> specializations = specializationRepository.findAll();

        return specializationMapper.toDTO(specializations);
    }

    @Override
    @Transactional
    public DoctorScheduleDTO createSlots(DoctorScheduleDTO scheduleDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getToggleStatus() == ToggleStatus.DISABLED)
            throw new AccessDeniedException("Sizning hisobingiz bloklangan! Amalni bajarishga ruxsat yo'q.");

        Doctor doctor = doctorRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Doctor topilmadi ", HttpStatus.NOT_FOUND));

        DoctorSchedule schedule = doctorScheduleMapper.toEntity(scheduleDTO);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());

        DoctorSchedule saved = doctorScheduleRepository.save(schedule);

        return doctorScheduleMapper.toDTO(saved);
    }

}
