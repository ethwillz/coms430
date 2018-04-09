package hw4.employees;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import hw4.employees.EmployeeDatabase.Tuple;

/**
 * @author dwtj
 */
public class EmployeeTest {
  
    public static void main(String[] args) {

      final String filename = "src/hw4/employees/employees1000.txt";
      EmployeeDatabase db = new EmployeeDatabase(filename);

      // useful for testing, same list
      ArrayList<Employee> arr = EmployeeDatabase.parseDB(filename);
      
      double avg = db.averageSalaryForDepartment("Finance");
      System.out.println(avg);
      
      // do it the hard way, to check the result
      double total = 0;
      int count = 0;
      for (Employee e : arr)
      {
        if (e.getDepartment().equals("Finance"))
        {
          total += e.getSalary();
          count += 1;
        }
      }
      double expectedAvg = total / count;
      System.out.println("Expected " + expectedAvg + "\n");
      
      Map<String, Double> avgMap = db.averageSalariesByDepartment();
      System.out.println(avgMap.get("Finance"));
      System.out.println("Expected " + expectedAvg + "\n");
      
      List<Tuple<String, String>> productionList = db.listOfEmployeesInProduction();
      List<Tuple<String, String>> checkProductionList = new ArrayList<>();
      for(Employee e: arr){
        if(e.getDepartment().equals("Production")) checkProductionList.add(new Tuple<>(e.getFirstName(), e.getLastName()));
      }
      boolean failedFlag = false;
      for(int i = 0; i < productionList.size(); i++){
        if(!productionList.get(i).getFirst().equals(checkProductionList.get(i).getFirst())
                || !productionList.get(i).getSecond().equals(checkProductionList.get(i).getSecond())){
          System.out.println(productionList.get(i) + "  " + checkProductionList.get(i));
          failedFlag = true;
        }
      }
      if(!failedFlag) System.out.println("Production lists match\n");
      
      List<Tuple<String, Integer>> highestPaid = db.getHighestPaidEmployees(10);
      arr.sort(Comparator.comparing(Employee::getSalary));
      List<Tuple<String, Integer>> checkHighestPaid = new ArrayList<>();
      for(Employee e: arr){
        checkHighestPaid.add(new Tuple<>(e.getLastName(), e.getSalary()));
      }
      failedFlag = false;
      for(int i = 0; i < highestPaid.size(); i++){
        if(!highestPaid.get(i).getFirst().equals(checkHighestPaid.get(i).getFirst())
                || !highestPaid.get(i).getSecond().equals(checkHighestPaid.get(i).getSecond())){
          System.out.println(highestPaid.get(i) + "  " + checkHighestPaid.get(i));
          failedFlag = true;
        }
      }
      if(!failedFlag) System.out.println("Highest paid lists match\n");
      
      Map<String, List<String>> byPosition = db.listEmployeesByPosition();
      Map<String, List<String>> checkByPosition = new HashMap<>();
      for(Employee e: arr){
        if(checkByPosition.containsKey(e.getPosition())){
          List<String> existingPlusNew = checkByPosition.get(e.getPosition());
          existingPlusNew.add(e.getLastName());
          checkByPosition.put(e.getPosition(), existingPlusNew);
        }
        else
          checkByPosition.put(e.getPosition(), new ArrayList<String>(){{ add(e.getLastName()); }});
      }
      for(String position: checkByPosition.keySet()){
        checkByPosition.get(position).sort(String::compareTo);
      }
      failedFlag = false;
      for(String position: byPosition.keySet()){
        for(int i = 0; i < byPosition.get(position).size(); i++) {
          if (!byPosition.get(position).get(i).equals(checkByPosition.get(position).get(i))) {
            System.out.println(byPosition.get(position).get(i) + "  " + checkByPosition.get(position).get(i));
            failedFlag = true;
          }
        }
      }
      if(!failedFlag) System.out.println("Position lists V1 match\n");
      
      // for part (c)
      Map<String, List<String>> byPosition2 = db.listEmployeesByPositionAlt();
      failedFlag = false;
      for(String position: byPosition2.keySet()){
        for(int i = 0; i < byPosition2.get(position).size(); i++) {
          if (!byPosition2.get(position).get(i).equals(checkByPosition.get(position).get(i))) {
            System.out.println(byPosition2.get(position).get(i) + "  " + checkByPosition.get(position).get(i));
            failedFlag = true;
          }
        }
      }
      if(!failedFlag) System.out.println("Position lists V2 match");

    }
    

}
