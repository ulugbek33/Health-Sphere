package uz.pdp.healthsphere.service.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeSymptoms(String userSymptoms) {
        // Prompt (Ko'rsatma)
        String prompt = "Sen tajribali tibbiy diagnostika yordamchisan. " +
                "Foydalanuvchi simptomlarini tahlil qil va unga eng mos mutaxassisni tanla. " +
                "Qoidalar:\n" +
                "1. Agar simptom yurak bilan bog'liq bo'lsa (sanchish, og'riq, puls), faqat 'KARDIOLOG' qaytar.\n" +
                "2. Agar simptom bosh, asab yoki uyqu bilan bog'liq bo'lsa, faqat 'NEVROLOG' qaytar.\n" +
                "3. Agar simptom tish bilan bog'liq bo'lsa, faqat 'STOMATOLOG' qaytar.\n" +
                "4. Agar simptom ko'z bilan bog'liq bo'lsa, faqat 'OFTALMOLOG' qaytar.\n" +
                "5. Boshqa barcha umumiy hollarda (isitma, gripp, holsizlik) 'TERAPEVT' qaytar.\n" +
                "\n" +
                "Javobing FAQAT BIR SO'Zdan iborat bo'lsin: [KARDIOLOG, NEVROLOG, TERAPEVT, STOMATOLOG, OFTALMOLOG].\n" +
                "\n" +
                "Bemor simptomi: " + userSymptoms;

        // Gemini uchun JSON struktura
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String fullUrl = apiUrl + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(fullUrl, requestBody, Map.class);

            // Gemini natijasini sug'urib olish (JSON parsing)
            List candidates = (List) response.getBody().get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            String aiResult = (String) firstPart.get("text");
            System.out.println("AI RAW RESULT: " + aiResult); //
            return aiResult.trim().toUpperCase(); // Masalan: "KARDIOLOG"
        } catch (Exception e) {
            e.printStackTrace(); // Xatolikni to'liq terminalda ko'rish uchun
            return "API_ERROR: " + e.getMessage();// Xato bo'lsa standart javob
        }
    }
}