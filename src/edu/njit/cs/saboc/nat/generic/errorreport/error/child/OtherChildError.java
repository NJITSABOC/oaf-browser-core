package edu.njit.cs.saboc.nat.generic.errorreport.error.child;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.ontology.Ontology;
import org.json.simple.JSONObject;

/**
 *
 * @author Chris O
 * 
 * @param <T>
 */
public class OtherChildError<T extends Concept> extends IncorrectChildError<T> {

    public OtherChildError(Ontology<T> ontology, T erroneousChild) {
        super(ontology, erroneousChild);
    }

    public OtherChildError(Ontology<T> ontology, T erroneousChild, String comment, Severity severity) {
        super(ontology, erroneousChild, comment, severity);
    }

    @Override
    public JSONObject toJSON() {
        return super.getBaseJSON("OtherChildError");
    }
}