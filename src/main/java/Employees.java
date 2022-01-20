public class Employees {
    private int id;
    private String name;
    private int salary;
    private Office office;
    private boolean hiredConsultant;

    public Employees(int id, String name, int salary, Office office, boolean hiredConsultant) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.office = office;
        this.hiredConsultant = hiredConsultant;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public Office getOffice() {
        return office;
    }

    public boolean isHiredConsultant() {
        return hiredConsultant;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", office=" + office +
                ", hiredConsultant=" + hiredConsultant +
                '}';
    }
}

