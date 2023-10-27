package uppgift;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class BenchmarkVehicles {

public static class Vehicle {
  private String registrationNumber;

  public Vehicle(Random random) {
    this.registrationNumber = generateRandomRegNumber(random);
  }

  private static String generateRandomRegNumber(Random random) {
        char letter1 = (char) ('A' + random.nextInt(26));
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));

        int digit1 = random.nextInt(10);
        int digit2 = random.nextInt(10);
        int digit3 = random.nextInt(10);
        return "" + letter1 + letter2 + letter3 + digit1 + digit2 + digit3;
    }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  @Override
  public int hashCode() {
      int hash = 7; 
      int prime = 31;
  
      for (int i = 0; i < registrationNumber.length(); i++) {
          hash = hash * prime + registrationNumber.charAt(i);
      }
  
      return hash;
  }
  

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Vehicle vehicle = (Vehicle) obj;
    return registrationNumber.equals(vehicle.registrationNumber);
  }

  @Override
  public String toString() {
    return registrationNumber;
  }
}


    private static final int[] initialCapacities = { 10, 20, 40, 80, 160 };
    private static final int[] vehicles = { 50, 100, 200, 400, 800 };
    private static final int iterations = 10;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("0.0", symbols);

    public static void main(String[] args) {
        run();
    }

    public static void run() {

      Random random = new Random();
        for (int capacity : initialCapacities) {
            for (int vehicleCount : vehicles) {
                long totalInitialConflicts = 0;
                long totalProbingConflicts = 0;
                long totalConflicts = 0;

                for (int iteration = 0; iteration < iterations; iteration++) {
                    HashTableQuadraticProbing<Vehicle> vehicleTable = new HashTableQuadraticProbing<>(capacity);

                    for (int i = 0; i < vehicleCount; i++) {
                        Vehicle vehicle = new Vehicle(random);
                        vehicleTable.insert(vehicle);
                    }

                    totalInitialConflicts += vehicleTable.getInitialConflicts();
                    totalProbingConflicts += vehicleTable.getProbingConflicts();
                    totalConflicts += vehicleTable.getTotalConflicts();
                }

                // Calculate averages
                double avgInitialConflicts = (double) totalInitialConflicts / iterations;
                double avgProbingConflicts = (double) totalProbingConflicts / iterations;
                double avgTotalConflicts = (double) totalConflicts / iterations;

                // Print results
                System.out.println("Initial table capacity: " + capacity + ", Vehicle Count: " + vehicleCount);
                System.out.println("Average Initial Conflicts: " + df.format(avgInitialConflicts));
                System.out.println("Average Probing Conflicts: " + df.format(avgProbingConflicts));
                System.out.println("Average Total Conflicts: " + df.format(avgTotalConflicts));
                System.out.println("-------------------------------------------------");
            }
        }
    }

    
}
