package dev.hkb.ananta.constants;

public enum ProductStatus {
    PENDING,    // Seller applied, but Admin/Manufacturer hasn't verified the stock yet
    ACTIVE,     // Approved and visible to Customers
    HIDDEN,     // Seller wants to temporarily stop selling (vacation mode)
    REJECTED,   // Admin denied the seller's application (e.g., suspected fake goods)
    OUT_OF_STOCK,// Automatically set when quantity hits 0
    DISCONTINUED
}
