package com.noah.npardon.daos;
public interface DelegateAsyncTask {
    /**
     * @param result Le resultat de la requête envoyé au serveur web
     */
    public void whenWSIsTerminated(Object result) ;
}
