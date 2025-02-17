package repository;

import model.Exceptions.MyException;
import model.State.PrgState;

import java.util.List;

public interface IRepository {
//    PrgState getCurrentPrgState() throws MyException;
    void logProgramState(PrgState program) throws MyException;
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
}
