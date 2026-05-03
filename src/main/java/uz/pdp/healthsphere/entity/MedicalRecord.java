package uz.pdp.healthsphere.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.entity.template.AbsLongEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class MedicalRecord extends AbsLongEntity {

    private String diagnosis;

    private String notes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "medicalRecord",cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
}
