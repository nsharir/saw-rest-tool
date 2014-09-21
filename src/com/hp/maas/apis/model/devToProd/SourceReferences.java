package com.hp.maas.apis.model.devToProd;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceReferences {

    //  Type        Id    list of sources
    Map<String,Map<String,List<ReferenceSource>>> map = new HashMap<String, Map<String, List<ReferenceSource>>>();

    public void addReference(String fromType, String fromId, String toType, String toId, String fromSourceField ){
        Map<String, List<ReferenceSource>> referencesMap = map.get(toType);
        if (referencesMap == null){
            referencesMap = new HashMap<String, List<ReferenceSource>>();
            map.put(toType,referencesMap);
        }

        List<ReferenceSource> sources = referencesMap.get(toId);
        if (sources == null){
            sources = new ArrayList<ReferenceSource>();
            referencesMap.put(toId,sources);
        }

        sources.add(new ReferenceSource(fromType, fromId, fromSourceField));
    }

    public Collection<String> getTypes(){
        return map.keySet();
    }

    public Collection<String> getIds(String type){
        Map<String, List<ReferenceSource>> listMap = map.get(type);
        if (listMap == null){
            listMap = new HashMap<String, List<ReferenceSource>>();
        }

        return listMap.keySet();
    }

    public Collection<ReferenceSource> getSources (String type,Object id){
        Map<String, List<ReferenceSource>> listMap = map.get(type);
        if (listMap == null){
            return new ArrayList<ReferenceSource>();
        }
        List<ReferenceSource> referenceSources = listMap.get(id);
        if (referenceSources == null){
            return new ArrayList<ReferenceSource>();
        }
        else{
            return referenceSources;
        }
    }

}
