package ECommerce.User.Payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Address {
    private Long addressId;

    private Long userId;

    private String street;
    private String city;
    private String state;
    private String zipcode;

}
