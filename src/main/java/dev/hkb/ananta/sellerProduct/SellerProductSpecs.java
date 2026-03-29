package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

///  The Powerful specification used to eliminate nested query part and can be used for dynamic queries
public class SellerProductSpecs {

    // 1. Fuzzy Name Match (Postgres similarity > 0.3) using Fuzzy Matching
    public static Specification<SellerProduct> hasFuzzyName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;

            Expression<Double> similarity = cb.function("similarity", Double.class, root.get("product").get("name"),
                    cb.literal(name));

            query.orderBy(cb.desc(similarity));

            return cb.greaterThan(similarity, 0.1);
        };
    }

    // 2. Matching Catching Exactly
    public static Specification<SellerProduct> hasCategory(Long categoryId){
        return (root, query, cb) -> {
            if (categoryId == null) return null;

            return cb.equal(root.get("product").get("category").get("id"), categoryId);
        };
    }

    // 3. Tag Match Exact
    public static Specification<SellerProduct> hasTags(Set<Long> tags){
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) return null;
            // whenever there is list or set like this use join instead of cb.equal or any other comparison
            return root.join("product").join("tagSet").get("id").in(tags);
        };
    }

    // 4. Global Status Filter (Security: only show approved listings)
    public static Specification<SellerProduct> hasStatus(ProductStatus status) {
        return (root, query, cb) -> cb.equal(root.get("productStatus"), status);
    }

    // 5. Performance: Join Fetch to avoid N+1 problem
    public static Specification<SellerProduct> fetchProductAndSeller() {
        return (root, query, cb) -> {
            // Only fetch if we aren't doing a 'COUNT' query for pagination
            if (query.getResultType() != Long.class) {
                root.fetch("product", JoinType.LEFT);
                root.fetch("seller", JoinType.LEFT).fetch("user", JoinType.LEFT);
            }
            return null;
        };
    }

    // 6. TO get by seller id to show products being sold by seller
    public static Specification<SellerProduct> hasSellerId(Long sellerId){
        return (root, query, cb) -> {
            if(sellerId == null) return null;

            return cb.equal(root.get("seller").get("id"), sellerId);
        };
    }

    // 7. to get a seller product by sellerProductid
    public static Specification<SellerProduct> hasSellerProductId(Long sellerProductId){
        return (root, query, cb) -> {
            if (sellerProductId == null) return null;

            return cb.equal(root.get("id"), sellerProductId);
        };
    }

}
