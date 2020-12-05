import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
