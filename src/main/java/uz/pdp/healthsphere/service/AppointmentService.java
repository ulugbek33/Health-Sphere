package uz.pdp.healthsphere.service;

import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.request.AppointmentRequestDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date);

    AppointmentDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO);

    void deleteAppointment(Long id);

}
