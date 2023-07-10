package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProducerDataDTO {
    private Double marketPriceMIN;
    private Double marketPriceAVG;
    private Double marketPriceMAX;
    private Double sellersMIN;
    private Double sellersAVG;
    private Double sellersMAX;
    private Double recommendedPrice;
}
