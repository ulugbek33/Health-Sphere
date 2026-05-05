package uz.pdp.healthsphere.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.request.DoctorPerformanceDTO;
import uz.pdp.healthsphere.dto.request.RevenueReportDTO;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = {"http://localhost:5173"
//        , "https://tastelab-tawny.vercel.app"
        })
public class AdminController {

    private final ReportService reportService;

    @GetMapping("/reports/revenue")
    public ResponseEntity<RevenueReportDTO> getRevenue(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        RevenueReportDTO reportDTO = reportService.getRevenueReport(startDate, endDate);

        return ResponseEntity.ok(reportDTO);
    }

    @GetMapping("/reports/doctor-performance")
    public List<DoctorPerformanceDTO> getDoctorPerformance(@RequestParam StatusEnum status) {
        return reportService.performance(status);
    }

}
