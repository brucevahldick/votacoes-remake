package controller;

import model.Config;
import model.Vote;
import observer.Observer;
import observer.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionFacade implements Subject {
    private Client client;
    private Config config;
    private List<Observer> observers;
    private List<Vote> votos;

    public List<Vote> getVotos() {
        return votos;
    }

    public ConnectionFacade() {
        observers = new ArrayList<>();
    }
    public void connectWithConfig(String ip, String nome) throws IOException {
        notifyObservers();
//        try (Socket socket = new Socket(ip, 80)) {
//            config = new Config(new Usuario(nome), ip);
//            client = new Client(socket, nome);
//            notifyObservers();
//        }

    }

    public void votar(boolean voto)
    {
        Vote votoObjeto = new Vote();
        votoObjeto.setUsuario(config.getUsuario());
        votoObjeto.setParecer(voto);
        client.sendData(votoObjeto);
    }

    public void atualizarVotacao(List<Vote> votes)
    {
        votos = votes;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }
}
