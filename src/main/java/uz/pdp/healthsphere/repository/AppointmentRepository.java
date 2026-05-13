package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.Patient;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    default Appointment getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No appointment found with id: " + id, HttpStatus.NOT_FOUND));
    }

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, LocalDate appointmentDate, StatusEnum status);

    @Query(value = "SELECT COUNT(a) > 0 FROM Appointment a " +
            "WHERE a.doctor = :doctor " +
            "AND a.appointmentDate = :date " +
            "AND a.startTime < :newEnd " +      // mavjud boshlanish yangi tugashdan oldin
            "AND a.endTime > :newStart")
    boolean existsOverlappingAppointment(@Param("doctor") Doctor doctor,
                                         @Param("date") LocalDate date,
                                         @Param("newStart") LocalTime newStart,
                                         @Param("newEnd") LocalTime newEnd);

    @Query(value = "select a from Appointment a " +
            "where a.doctor = :doctor" +
            " and a.status = :status" +
            " and a.appointmentDate in :dates " +
            "order by a.appointmentDate asc ," +
            "a.startTime asc ")
    List<Appointment> findByDoctorAndStatusAndDates(
            @Param("doctor") Doctor doctor,
            @Param("status") StatusEnum status,
            @Param("dates") List<LocalDate> dates);

    List<Appointment> findAllByPatient(Patient patient);
}