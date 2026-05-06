package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.request.AppointmentRequestDTO;
import uz.pdp.healthsphere.entity.*;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.exceptions.AppointmentConflictException;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.exceptions.InvalidTimeRangeException;
import uz.pdp.healthsphere.mapper.AppointmentMapper;
import uz.pdp.healthsphere.repository.AppointmentRepository;
import uz.pdp.healthsphere.repository.DoctorRepository;
import uz.pdp.healthsphere.repository.DoctorScheduleRepository;
import uz.pdp.healthsphere.repository.PatientRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

        if (date.isBefore(LocalDate.now()))
            return Collections.emptyList();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        DayOfWeekEnum dayOfWeekEnum = DayOfWeekEnum.valueOf(dayOfWeek.name());

        DoctorSchedule schedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeekEnum)
                .orElseThrow(() -> new RuntimeException("Shifokor bu kuni ishlamaydi :"));

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, StatusEnum.CANCELLED);

        Set<LocalTime> busyStartTimes = appointments.stream()
                .map(Appointment::getStartTime)
                .collect(Collectors.toSet());

        List<LocalTime> availableSlots = new ArrayList<>();

        LocalTime currentSlot = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();
        Integer duration = schedule.getSlotDuration();

        if (duration <= 0)
            return availableSlots;

        while (true) {
            LocalTime nextSlot = currentSlot.plusMinutes(duration);
            if (nextSlot.isAfter(endTime)) break;

            // "isBusy"ni faqat start_time bilan emas, balki oraliq bilan tekshiramiz
            LocalTime finalCurrentSlot = currentSlot;
            LocalTime finalNextSlot = nextSlot;

            boolean isBusy = appointments.stream().anyMatch(app ->
                    // Agar band bo'lgan vaqt bizning slotimiz bilan kesishsa:
                    (app.getStartTime().isBefore(finalNextSlot) && app.getEndTime().isAfter(finalCurrentSlot))
            );

            boolean isPast = false;
            if (date.equals(LocalDate.now())) {
                if (currentSlot.isBefore(LocalTime.now()))
                    isPast = true;
            }

            if (!isBusy && !isPast) {
                availableSlots.add(currentSlot);
            }

            currentSlot = nextSlot;
            if (currentSlot.equals(endTime))
                break;
        }

        return availableSlots;
    }

    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found :", HttpStatus.NOT_FOUND));

        Doctor doctor = doctorRepository.getByIdOrThrow(appointmentRequestDTO.getDoctorId());

        DayOfWeek dayOfWeek = appointmentRequestDTO.getDate().getDayOfWeek();
        DayOfWeekEnum dayOfWeekEnum = DayOfWeekEnum.valueOf(dayOfWeek.name());

        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorAndDayOfWeek(doctor, dayOfWeekEnum)
                .orElseThrow(() -> new RuntimeException("Shifokor bu kuni ishlamaydi :"));

        LocalTime newStart = appointmentRequestDTO.getStartTime();
        LocalTime newEnd = newStart.plusMinutes(doctorSchedule.getSlotDuration());

        boolean isBooked = appointmentRepository.existsOverlappingAppointment(doctor,
                appointmentRequestDTO.getDate(),
                newStart,
                newEnd);

        if (newStart.isBefore(doctorSchedule.getStartTime()) || newEnd.isAfter(doctorSchedule.getEndTime()))
            throw new InvalidTimeRangeException("Shifokorning ish vaqti: " + doctorSchedule.getStartTime() + " - " + doctorSchedule.getEndTime(), HttpStatus.BAD_REQUEST);

        if (isBooked)
            throw new AppointmentConflictException("Bu vaqtga allaqachon yozilgan, boshqa vaqt tanlang", HttpStatus.CONFLICT);

        if (appointmentRequestDTO.getDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Bu sana o'tib ketgan :");

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentRequestDTO.getDate());
        appointment.setStartTime(newStart);
        appointment.setEndTime(newEnd);
        appointment.setStatus(StatusEnum.PENDING);

        appointmentRepository.save(appointment);

        return appointmentMapper.toDTO(appointment);
    }

}
