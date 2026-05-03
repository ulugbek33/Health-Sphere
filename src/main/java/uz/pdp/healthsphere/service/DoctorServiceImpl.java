package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.entity.*;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;
import uz.pdp.healthsphere.enums.InvoiceStatus;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.exceptions.AppointmentStatusException;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.mapper.AppointmentMapper;
import uz.pdp.healthsphere.mapper.MedicalRecordMapper;
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

    @Override
    public List<AppointmentDTO> myAppointments() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

}
