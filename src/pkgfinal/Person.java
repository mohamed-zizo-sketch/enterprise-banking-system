package pkgfinal;

/**
 * Abstract base class representing personal identity information.
 */
public abstract class Person {

    protected String name;
    protected String nationalId;
    protected String phone;

    public Person(String name, String nationalId, String phone) {
        this.name = name;
        this.nationalId = nationalId;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
