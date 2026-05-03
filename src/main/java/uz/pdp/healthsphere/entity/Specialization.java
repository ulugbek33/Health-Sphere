package uz.pdp.healthsphere.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.entity.template.AbsLongEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Specialization extends AbsLongEntity {

    private String name;

    private String description;
}
