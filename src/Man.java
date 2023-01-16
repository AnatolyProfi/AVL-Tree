import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Man {

    private String race;

    private String name;

    private String dateOfBirth;

    private int height;

    @Override
    public String toString() {
        return "Man{" +
                "" + race  +
                ", " + name  +
                ", " + dateOfBirth  +
                ", " + height +
                '}';
    }
}
