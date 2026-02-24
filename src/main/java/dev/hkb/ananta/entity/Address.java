package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users user;

    @Pattern(regexp = "^[6-9]//d{9}", message = "Enter a valid phone number")
    @Column(name = "phone_num")
    private String phoneNumber;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StateEnum state;

    @Column(name = "pin_code")
    private String pinCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private CountryEnum country;

    //Constructor
    public Address() {
    }

    public Address(Users user, String phoneNumber, String addressLine,
                   String city, StateEnum state, String pinCode, CountryEnum country) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }
}
