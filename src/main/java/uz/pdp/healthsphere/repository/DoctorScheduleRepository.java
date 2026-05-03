package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.DoctorSchedule;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeekEnum dayOfWeekEnum);

    Optional<DoctorSchedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeekEnum dayOfWeekEnum);

    List<DoctorSchedule> findByDoctor(Doctor doctor);
}