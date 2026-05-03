package uz.pdp.healthsphere.service;

import uz.pdp.healthsphere.dto.request.DoctorPerformanceDTO;
import uz.pdp.healthsphere.dto.request.RevenueReportDTO;
import uz.pdp.healthsphere.enums.StatusEnum;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate);

    List<DoctorPerformanceDTO> performance(StatusEnum status);

}
