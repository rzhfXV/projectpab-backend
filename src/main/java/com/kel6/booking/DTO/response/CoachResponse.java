package com.kel6.booking.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachResponse {
    private Long id;
    private String name;
    private String profileImageUrl;
    
    // List ini akan menampung jam operasional pelatih secara dinamis
    // Contoh isi JSON nanti: ["06.00", "14.00"]
    private List<String> availableHours; 
}