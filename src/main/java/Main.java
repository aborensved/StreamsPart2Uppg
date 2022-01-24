import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<Employees> employees = getEmployees();

        // ----- Nivå 1 -----

        //1. Gör en Lista med 10 anställda som har ett id, ett namn, en lön, en kontorsplacering("Stockholm",
        //"Göteborg", eller "Malmö") samt en boolean som anger om de är inhyrda konsulter.

        //2. Plocka ut den anställda i Göteborg som har högst lön.
        System.out.println("This employee has the highest salary in Gothenburg: " +
                employees
                        .stream()
                        .filter(gbg -> gbg.getOffice().equals(Office.GÖTEBORG))
                        .sorted(Comparator.comparing(Employees::getSalary).reversed())
                        .limit(1)
                        .toList()
        );

        System.out.println();
        //3. Plocka ut den anställde som har lägst lön i Stockholm.
        List<Employees> lowestSalarySthlm = employees
                .stream()
                .filter(sthlm -> sthlm.getOffice().equals(Office.STOCKHOLM))
                .sorted(Comparator.comparing(Employees::getSalary))
                .limit(1)
                .toList();
        System.out.println("This employee has the lowest salary in Stockholm: " +lowestSalarySthlm);

        System.out.println();
        //4. Plocka ut en anställd med ett visst, hårdkodat ID
        List<Employees> specificId = employees
                .stream()
                .filter(id -> id.getId() == 3)
                .toList();
        System.out.println("An employee with the specific id of 3 : " +specificId);

        System.out.println();
        //5. Plocka ut snittlönen för anställda i Malmö (m.h.a. summaryStatistics)
        System.out.println("The average salary in Malmö is: " +
                employees
                        .stream()
                        .filter(mlm -> mlm.getOffice().equals(Office.MALMÖ))
                        .mapToDouble(Employees::getSalary)
                        .summaryStatistics()
                        .getAverage()
                +"SEK"
        );

        System.out.println();
        //6.Plocka ut den totala lönekostnaden för konsulter i Stockholm.
        System.out.println("The total salary for the consultants in Stockholm is: " +
                employees
                        .stream()
                        .filter(x -> x.getOffice().equals(Office.STOCKHOLM))
                        .filter(Employees::isHiredConsultant)
                        .sorted(Comparator.comparing(Employees::getSalary))
                        .mapToDouble(Employees::getSalary)
                        .summaryStatistics()
                        .getSum()
                + "SEK"

        );

        System.out.println();
        //7. Plocka ut antalet anställda i Stockholm och Göteborg
        System.out.println( "The total salary for the employees in Stockholm and Gothenburg: " +
                employees
                        .stream()
                        .filter(x -> x.getOffice() != Office.MALMÖ)
                        .sorted(Comparator.comparing(Employees::getSalary))
                        .mapToDouble(Employees::getSalary)
                        .sum()
                + "SEK"

        );

        System.out.println();
        // 8.Spara resultatet av summerStatistics i en variabel av typen DoubleSummarStatistics,
        // undersök först vilka metoder du därigenom har tillgång till och testas sedan att printa ut objekt.

        DoubleSummaryStatistics results =
                employees
                        .stream()
                        .mapToDouble(Employees::getSalary)
                        .summaryStatistics();

        System.out.println("The summary statistics: " + results);

        // ----- NIVÅ 2 ------

        System.out.println();
        // 1. Använd reduce för att summera ihop lönerna från alla anställda.
        System.out.println("The sum of everyone's salary: " +
                employees
                        .stream()
                        .sorted(Comparator.comparing(Employees::getSalary))
                        .mapToDouble(Employees::getSalary)
                        .reduce(0, Double::sum)
                + "SEK"
        );

        System.out.println();
        // 2. Använd collect för att returnera en map som håller varje stad som key
        // och det totala anställda för respektive stad som value.

        Map<Office, Long> howManyEmployees =
                employees
                        .stream()
                        .collect(Collectors.groupingBy(Employees::getOffice,
                                 Collectors.counting()));
        howManyEmployees.forEach((Office, count) -> System.out.printf("%s has %d employee(s)%n", Office, count));

        System.out.println();
        // 3. Samma som ovan, men byt ut totala antalet anställda mot det totala lönekostnaden.
        Map<Office, Long> totalSalary = employees
                .stream()
                .collect(Collectors.groupingBy(Employees::getOffice,
                         Collectors.summingLong(Employees::getSalary)));
        totalSalary.forEach((Office, count) -> System.out.printf("%s has a total salary of %d SEK%n", Office, count));

        System.out.println();
        //4. Samma som ovan, men byt ut den totala lönekostnaden mot snittlönen.
        Map<Office, Double> averageSalary = employees
                .stream()
                .collect(Collectors.groupingBy(Employees::getOffice,
                         Collectors.averagingDouble(Employees::getSalary)));
        averageSalary.forEach((Office, count) -> System.out.printf("%s has an average salary of %,.3f SEK%n", Office, count));

        System.out.println();
        //5. Lagra två strömmar i två olika variabler, som var för sig är strömmar av 4 eller fler siffror.

        Stream<Integer> firstStream = Stream.of(1, 3, 5, 7, 9);

        Stream<Integer> secondStream = Stream.of(2, 4, 6, 8, 10);

        System.out.println();
        //6. Slå ihop de två lagrade strömmarna till en, med hjälp av concat.
        System.out.println("Under this line are two streams combined with the use of concat:");
        Stream.concat(firstStream, secondStream)
                .forEach(System.out::println);

        System.out.println();
        //7. Skapa en Stream.of() fyra listor som argument. vad utgörs varje element i strömmen av?
        //Hur kan du istället skapa en ström bestående respektive listas element samlade direkt?
        System.out.println("This is four streams combined with flatmap: " +
        Stream.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8), List.of(9, 10, 11, 12), List.of(13, 14, 15, 16))
                .flatMap(Collection::stream)
                .toList());

        // ------ Nivå 3 -----

        System.out.println();
        //1. Returnera en Map, med varje stad som key, och där andelen(i procent) inhyrda konsulter utgör ett value
        Map<Office, Double> consultantPercentage = employees
                .stream()
                .collect(Collectors.groupingBy(Employees::getOffice,
                         Collectors.averagingDouble(employee -> employee.isHiredConsultant()? 1 : 0)));
        consultantPercentage.forEach((Office, count) -> System.out.printf("%s has a %,.1f%% of consultants%n", Office, count * 100));

        System.out.println();
        //2. Samma som ovan, men där value utgörs av ett värde som representerar hur stor andel
        // av de anställda som arbetar vid respektive stad
        Map<Office, Long> howManyWorkers = employees
                .stream()
                .collect(Collectors.groupingBy(Employees::getOffice,
                         Collectors.counting()));
        howManyWorkers.forEach((Office, count) -> System.out.printf("%s has a %d %% of employees%n", Office, count * 10));

    }

    private static List<Employees> getEmployees(){
        return List.of(
                new Employees(1, "Jonas", 25_000, Office.STOCKHOLM, true),
                new Employees(2, "Hans", 32_000, Office.MALMÖ, false),
                new Employees(3, "Ebba", 33_000, Office.MALMÖ, false),
                new Employees(4, "Dragan", 26_000, Office.GÖTEBORG, true),
                new Employees(5, "Johanna", 28_000, Office.STOCKHOLM, false),
                new Employees(6, "Sara", 33_000, Office.GÖTEBORG, true),
                new Employees(7, "Arvid", 45_000, Office.MALMÖ, true),
                new Employees(8, "Stella", 72_000, Office.GÖTEBORG, false),
                new Employees(9, "Johan", 34_000, Office.STOCKHOLM, true),
                new Employees(10, "Arnold", 38_000, Office.STOCKHOLM, true)
        );

    }


}
