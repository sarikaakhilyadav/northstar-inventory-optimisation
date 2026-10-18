
package com.northstar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleRequest(

        @NotNull(message = "Sale quantity is required") @Positive(message = "Sale quantity must be greater than zero") Integer quantity

) {
}
