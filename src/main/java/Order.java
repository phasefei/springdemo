import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Order.
 * Maps the order structure from json file
 */
@Getter
@Setter
@ToString
public class Order {
	private String id;
	private String name;
	private String temp;
	private Integer shelfLife;
	private Double decayRate;
}
