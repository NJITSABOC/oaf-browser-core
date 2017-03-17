package edu.njit.cs.saboc.nat.generic.errorreport.error.parent;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.ontology.Ontology;
import edu.njit.cs.saboc.nat.generic.errorreport.error.OntologyError;
import org.json.simple.JSONObject;

/**
 * A report of a parent that is redundant and should be removed
 * 
 * @author Chris O
 * 
 * @param <T>
 */
public class RedundantParentError<T extends Concept> extends RemoveParentError<T> {
    
    public RedundantParentError(Ontology<T> ontology, T erroneousParent) {
        super(ontology, erroneousParent);
    }
    
    public RedundantParentError(
            Ontology<T> ontology, 
            T erroneousParent, 
            String comment, 
            OntologyError.Severity severity) {
        
        super(ontology, erroneousParent, comment, severity);
    }

    @Override
    public JSONObject toJSON() {
        return super.getBaseJSON("RedundantParent");
    }
}