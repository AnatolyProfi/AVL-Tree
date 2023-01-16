import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class MenuHashMap {

    private static final Scanner scanner = new Scanner(System.in);
    private static final MyHashMap<String, Man> myHashMap = new MyHashMap<>();

    public static void menu() {
        try {
            System.out.println("""
                    Choose method HashMap:
                    1. Put;   2. Get;   3. Remove;   4.Clear;   5. Size;
                    6. PutAll;   7. ContainsKey;   8. Values;   9. ContainsValue;   10. IsEmpty.
                    """);
            int choiceAction = scanner.nextInt();
            switch (choiceAction) {
                case 1:
                    putVoid();
                case 2:
                    get();
                case 3:
                    remove();
                case 4:
                    myHashMap.clear();
                case 5:
                    System.out.println(myHashMap.size());
                case 6:
                    putAll();
                case 7:
                    containsKey();
                case 8:
                    System.out.println(myHashMap.values());
                case 9:
                    containsValue();
                case 10:
                    System.out.println(myHashMap.isEmpty());
            }
        } catch (InputMismatchException e){
            System.out.println("Input format is not correct!");
            menu();
        }
    }

    private static Man put() {
        System.out.println("Enter key:");
        String key = scanner.next();
        System.out.println("Enter race man:");
        String race = scanner.next();
        System.out.println("Enter name man:");
        String name = scanner.next();
        System.out.println("Enter date of birth man:");
        String dateOfBirth = scanner.next();
        System.out.println("Enter height man:");
        int height = scanner.nextInt();
        Man man = new Man(race, name, dateOfBirth, height);
        myHashMap.put(key, man);
        return man;
    }

    private static void putVoid() {
        put();
        menu();
    }

    private static void get() {
        System.out.println("Enter key:");
        String key = scanner.next();
        System.out.println(myHashMap.get(key));
        menu();
    }

    private static void remove() {
        System.out.println("Enter key:");
        String key = scanner.next();
        myHashMap.remove(key);
        menu();
    }

    private static void putAll() {
        boolean isStop = false;
        while (!isStop) {
            Map<String, Man> map = new HashMap<>();
            map.put(" ", put());
            System.out.println("If you want to continue enter 1, otherwise enter any other number:");
            int action = scanner.nextInt();
            if (action != 1) {
                myHashMap.putAll(map);
                isStop = true;
            }
        }
        menu();
    }

    private static void containsKey() {
        System.out.println("Enter key:");
        String key = scanner.next();
        System.out.println(myHashMap.containsKey(key));
        menu();
    }

    private static void containsValue() {
        System.out.println("Enter race man:");
        String race = scanner.next();
        System.out.println("Enter name man:");
        String name = scanner.next();
        System.out.println("Enter date of birth man:");
        String dateOfBirth = scanner.next();
        System.out.println("Enter height man:");
        int height = scanner.nextInt();
        Man man = new Man(race, name, dateOfBirth, height);
        System.out.println(myHashMap.containsValue(man));
    }
}
