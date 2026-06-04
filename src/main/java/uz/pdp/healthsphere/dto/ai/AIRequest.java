package uz.pdp.healthsphere.dto.ai;

import java.util.List;

public record AIRequest(String model, List<Message> messages) {}
