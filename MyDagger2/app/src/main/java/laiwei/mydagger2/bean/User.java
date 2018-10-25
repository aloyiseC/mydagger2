package laiwei.mydagger2.bean;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
public class User {
    private Person person;

    public String getName(){
        return person.getName();
    }

    public int getAge(){
        return person.getAge();
    }

    public User(Person person) {
        this.person = person;
    }
}
