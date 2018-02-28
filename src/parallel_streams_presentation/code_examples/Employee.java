package parallel_streams_presentation.code_examples;

public class Employee{
    String department;
    char name;

    public Employee(String department, char name){
        this.department = department;
        this.name = name;
    }
    public String getDepartment(){
        return department;
    }

    public char getName(){
        return name;
    }
}