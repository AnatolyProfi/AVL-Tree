import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Node<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
