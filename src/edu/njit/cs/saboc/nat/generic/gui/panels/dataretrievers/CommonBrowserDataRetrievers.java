package edu.njit.cs.saboc.nat.generic.gui.panels.dataretrievers;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.comparators.ConceptNameComparator;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel.DataRetriever;
import java.util.ArrayList;
import java.util.Set;

/**
 * Defines a set of focus concept data retrievers that are common to all NAT 
 * instances
 * 
 * @author Chris O
 */
public class CommonBrowserDataRetrievers {
    
    /**
     * Creates a retriever for obtaining the children of the given concept
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getChildrenRetriever(NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
                if(browserPanel.getDataSource().isPresent()) {
                     return getSortedConceptList(browserPanel.getDataSource().get().getOntology().getConceptHierarchy().getChildren(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Children";
            }
            
        };
    }
    
    /**
     * Creates a retriever for obtaining the parents of the given concept
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getParentsRetriever(
            NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
                
                if(browserPanel.getDataSource().isPresent()) {
                     return getSortedConceptList(browserPanel.getDataSource().get().
                             getOntology().getConceptHierarchy().getParents(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Parents";
            }
        };
    }
    
    /**
     * Creates a retriever for obtaining the siblings (i.e., concepts with at least one 
     * same parent) as the given concept.
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getSiblingsRetriever(
            NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
                
                if(browserPanel.getDataSource().isPresent()) {
                    return getSortedConceptList(browserPanel.getDataSource().get().
                            getOntology().getConceptHierarchy().getSiblings(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Siblings";
            }
            
        };
    }
    
    /**
     * Creates a retriever for obtaining the strict siblings (i.e., concepts that 
     * share all of the same parents) of a the given concept
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getStrictSiblingsRetriever(
            NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
                
                if(browserPanel.getDataSource().isPresent()) {
                    return getSortedConceptList(browserPanel.getDataSource().get().
                            getOntology().getConceptHierarchy().getStrictSiblings(concept));
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Strict Siblings";
            }
            
        };
    }
    
    /**
     * Creates a retriever for obtaining the grandparents of a given concept, organized into 
     * Grandparent results based on which parent of the given concept they are a parent of.
     * 
     * A given grandparent may be included in multiple grandparent results, if it is the 
     * parent of multiple parents
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<GrandparentResult<T>>> getGrandparentsRetriever(
             NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<GrandparentResult<T>>>() {

            @Override
            public ArrayList<GrandparentResult<T>> getData(T concept) {
                
                if (browserPanel.getDataSource().isPresent()) {
                    ConceptBrowserDataSource<T> dataSource = browserPanel.getDataSource().get();
                    
                    ArrayList<T> parents = getSortedConceptList(dataSource.getOntology().getConceptHierarchy().getParents(concept));

                    ArrayList<GrandparentResult<T>> grandParentResults = new ArrayList<>();

                    parents.forEach((parent) -> {
                        grandParentResults.add(new GrandparentResult<>(parent,
                                getSortedConceptList(dataSource.getOntology().getConceptHierarchy().getParents(parent))));
                    });

                    return grandParentResults;
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Parents and Grandparents";
            }
        };
    }
    
    /**
     * Creates a retriever for obtaining the grandchildren of a given concept, organized into 
     * grandchild results based on which child of the given concept they are a child of.
     * 
     * A given grandchild may be included in multiple grandchild results, if it is the 
     * child of multiple children
     * 
     * @param <T>
     * @param browserPanel
     * 
     * @return 
     */
    public static <T extends Concept> DataRetriever<T, ArrayList<GrandchildResult<T>>> getGrandchildrenRetriever(
            NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<GrandchildResult<T>>>() {

            @Override
            public ArrayList<GrandchildResult<T>> getData(T concept) {

                if (browserPanel.getDataSource().isPresent()) {
                    ConceptBrowserDataSource<T> dataSource = browserPanel.getDataSource().get();

                    ArrayList<T> children = getSortedConceptList(dataSource.getOntology().getConceptHierarchy().getChildren(concept));

                    ArrayList<GrandchildResult<T>> grandParentResults = new ArrayList<>();

                    children.forEach((child) -> {
                        grandParentResults.add(new GrandchildResult<>(child,
                                getSortedConceptList(dataSource.getOntology().getConceptHierarchy().getChildren(child))));
                    });

                    return grandParentResults;
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public String getDataType() {
                return "Parents and Grandparents";
            }
        };
    }
    
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getTopologicalAncestorsRetriever(
        NATBrowserPanel<T> browserPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
                
                if (browserPanel.getDataSource().isPresent()) {
                    ArrayList<T> topologicalAncestors = browserPanel.getDataSource().get().
                            getOntology().getConceptHierarchy().getAncestorHierarchy(concept).getTopologicalOrdering();
                    
                    topologicalAncestors.remove(concept);

                    if (!topologicalAncestors.isEmpty()) {
                        topologicalAncestors.remove(topologicalAncestors.get(0));
                    }

                    return topologicalAncestors;
                } else {
                    return new ArrayList<>();
                }

            }

            @Override
            public String getDataType() {
                return "Ancestors (Topological order)";
            }
        };
    }
    
    public static <T extends Concept> DataRetriever<T, ArrayList<T>> getCurrentAuditSet(NATBrowserPanel<T> mainPanel) {
        
        return new DataRetriever<T, ArrayList<T>>() {

            @Override
            public ArrayList<T> getData(T concept) {
//                if(mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {
//                    
//                    ArrayList<T> concepts = new ArrayList<>(mainPanel.getAuditDatabase().getLoadedAuditSet().get().getConcepts());
//                    concepts.sort(new ConceptNameComparator());
//                    
//                    return concepts;
//                    
//                } else {
//                    return new ArrayList<>();
//                }
                
                return new ArrayList<>();
            }

            @Override
            public String getDataType() {
                return "Current Audit Set";
            }
        };
    }
    
    public static <T extends Concept> DataRetriever<T, ArrayList<?>> getDoNothingRetriever() {
        
        return new DataRetriever<T, ArrayList<?>>() {

            @Override
            public ArrayList<?> getData(T concept) {                
                return new ArrayList<>();
            }

            @Override
            public String getDataType() {
                return "";
            }
        };
    }
    
    public static <T extends Concept> ArrayList<T> getSortedConceptList(Set<T> conceptSet) {
        ArrayList<T> concepts = new ArrayList<>(conceptSet);

        concepts.sort(new ConceptNameComparator());

        return concepts;
    }
}
