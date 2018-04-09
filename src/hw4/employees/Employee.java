package hw4.employees;

public class Employee
{
  public final String firstName;
  public final String lastName;
  public final int salary;
  public final String department;
  public final String position;

  public Employee(String firstName, String lastName, int salary,
      String department, String position)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.department = department;
    this.position = position;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getSalary() {
    return salary;
  }

  public String getDepartment() {
    return department;
  }

  public String getPosition() {
    return position;
  }
}
