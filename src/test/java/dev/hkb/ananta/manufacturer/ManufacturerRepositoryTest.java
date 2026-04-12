package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.constants.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Test
    void getManufacturerByBrandName_ShouldReturnManufacturer_WhenBrandExists(){
        // Arrange: Save a REAL record into the temporary test database
        Manufacturer m1 = new Manufacturer("ubu","28jgel34b6js", StatusEnum.ACTIVE);
        manufacturerRepository.save(m1);

        // Act: Try to fetch it using your custom method
        Manufacturer test = manufacturerRepository.getManufacturerByBrandName(m1.getBrandName()).get();

        // Assert: Did your query actually find it?
        // (Note: We use AssertJ's 'assertThat' here, it's the standard for DAO tests!)
        assertThat(test).isNotNull();
        assertThat(test.getBrandName()).isEqualTo("ubu");
    }
}
