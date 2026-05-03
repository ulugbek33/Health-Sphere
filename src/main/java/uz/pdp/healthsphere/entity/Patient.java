package uz.pdp.healthsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.entity.template.AbsLongEntity;
import uz.pdp.healthsphere.enums.BloodGroupEnum;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Patient extends AbsLongEntity {

    private String fullName;

    private LocalDateTime birthDate;

    private String allergies;

    @Enumerated(EnumType.STRING)
    private BloodGroupEnum bloodGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
