package repository;

import java.time.LocalDate;
import java.util.Map;

public interface IRepositoryRapoarte {

    int getNumarVizitatoriPeZi(LocalDate data);

    Map<String, Integer> vizitatoriPeVarsteZi(LocalDate data);

    double getIncasariInterval(LocalDate start, LocalDate end);

    double getTotalHranaInterval(LocalDate start, LocalDate end);

    void close();
}
