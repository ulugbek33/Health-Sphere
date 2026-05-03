package uz.pdp.healthsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.entity.template.AbsLongEntity;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DoctorSchedule extends AbsLongEntity {

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer slotDuration;

    @Enumerated(EnumType.STRING)
    private DayOfWeekEnum dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}
