package hw4.employees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeDatabase
{
  private final ArrayList<Employee> employees;

  /**
   * Constructor from file. The file should be in CSV format where the first
   * row is headings. The valid headings are "First", "Last", "Salary",
   * "Department", and "Position".
   */
  public EmployeeDatabase(String filename)
  {
    this.employees = parseDB(filename);
  }

  /**
   * Alternate constructor for testing.
   */
  public EmployeeDatabase(ArrayList<Employee> employees) 
  {
    this.employees = employees;
  }

  public static ArrayList<Employee> parseDB(String filename) 
  {
    try
    {
      File f = new File(filename);
      Scanner s = new Scanner(f);
      int[] map = parseHeadings(s.nextLine());
      ArrayList<Employee> employees = new ArrayList<>();
      while (s.hasNextLine()) 
      {
        employees.add(parseEmployee(s.nextLine(), map));
      }
      s.close();
      return employees;
    }
    catch (FileNotFoundException e) 
    {
      System.out.println("Could not find " + filename);
      return null;
    }
  }

  public static int[] parseHeadings(String line)
  {
    String[] headings = line.split(",");
    int[] map = new int[headings.length];
    for (int i = 0; i < headings.length; i++) 
    {
      if (headings[i].equals("First")) 
      {
        map[0] = i;
      }
      else if (headings[i].equals("Last")) 
      {
        map[1] = i;
      }
      else if (headings[i].equals("Salary")) 
      {
        map[2] = i;
      }
      else if (headings[i].equals("Department"))
      {
        map[3] = i;
      } 
      else
      {
        map[4] = i;
      }
    }
    return map;
  }

  public static Employee parseEmployee(String line, int[] map) 
  {
    String[] words = line.split(",");
    return new Employee(words[map[0]], words[map[1]],
        Integer.parseInt(words[map[2]]), words[map[3]], words[map[4]]);
  }
  
  /**
   * Returns the average salary for all employees in the given department.
   * @param department
   * @return
   */
  public double averageSalaryForDepartment(String department)
  {
    return employees.stream()
            .filter(e -> e.department.equals(department))
            .collect(Collectors.averagingInt(Employee::getSalary));
  }

  /** Returns map of of average salary by department. Each department
   * should be mapped to one double which is the average salary of the people
   * in that department.
   */
  public Map<String, Double> averageSalariesByDepartment()
  {
    return employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingInt(Employee::getSalary)));
  }

  /**
   * Returns a list of the names of employees in the "Production" department,
   * sorted by last name.  Each tuple should be in the form 
   * (first_name, last_name). 
   */
  public List<Tuple<String, String>> listOfEmployeesInProduction()
  {
    return employees.stream()
            .filter(e -> e.getDepartment().equals("Production"))
            .map(e -> new Tuple<>(e.getFirstName(), e.getLastName()))
            .collect(Collectors.toList());
  }

  /**
   * Returns a list of the highest paid employees, ordered from highest salary
   * to lowest, as a list of tuples containing a last name and salary.
   * The length of returned list is determined by the 'howMany' parameter.
   * @param howMany
   * @return
   */
  public List<Tuple<String, Integer>> getHighestPaidEmployees(int howMany)
  {
    return employees.stream()
            .sorted((o1, o2) -> {
              if(o1.getSalary() == o2.getSalary()) return 0;
              else return o1.getSalary() > o2.getSalary() ? 1 : -1;
            })
            .map(e -> new Tuple<>(e.getLastName(), e.getSalary()))
            .collect(Collectors.toList());
  }
  
  /**
   * Returns a map in which each position is mapped to a
   * list of last names of employees in that position, 
   * sorted alphabetically.
   * @return
   */
  public Map<String, List<String>> listEmployeesByPosition()
  {
    return employees.stream()
            .sorted(Comparator.comparing(Employee::getLastName))
            .collect(Collectors.groupingBy(Employee::getPosition,
                    Collectors.mapping(Employee::getLastName, Collectors.toList())));
  }
  
  /**
   * Alternate implementation of above.  Do not modify this method; rather, 
   * implement the PositionLister consumer class below.
   * @return
   */
  public Map<String, List<String>> listEmployeesByPositionAlt()
  {
    PositionLister combiner = employees.parallelStream()
        .sorted((lhs, rhs) -> lhs.getLastName().compareTo(rhs.getLastName()))
        .collect(PositionLister::new, PositionLister::accept, PositionLister::combine);
    return combiner.getMap();
  }
  
  public static class PositionLister implements Consumer<Employee>
  {
    Map<String, List<String>> map = new ConcurrentHashMap<>();

    public Map<String, List<String>> getMap() { return map; }

    public void accept(Employee e)
    {
      if(map.computeIfAbsent(e.getPosition(), (s) -> new ArrayList<String>(){{ add(e.getLastName()); }}) == null){
        map.computeIfPresent(e.getPosition(), (s, l) -> {
          l.add(e.getLastName());
          return l;
        });
      }
    }

    public void combine(PositionLister other)
    {
      other.getMap().forEach((s, l) -> {
        if(map.computeIfAbsent(s, (x) -> l) == null){
          map.computeIfPresent(s, (k, v) -> Stream.of(l, v)
                  .flatMap(Collection::stream)
                  .sorted()
                  .collect(Collectors.toList()));
        }
      });
    }
  }
  

  /**
   * Generic pair class.
   * @param <T1>
   * @param <T2>
   */
  public static class Tuple<T1, T2> 
  {
    final T1 a;
    final T2 b;
    
    public Tuple(T1 a, T2 b) 
    {
      this.a = a;
      this.b = b;
    }    
    public T1 getFirst() { return a; }
    public T2 getSecond() { return b; }   
    public String toString()
    {
      return "<" + a.toString() + ", " + b.toString() + ">";
    }
  }
  

}
